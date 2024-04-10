package com.example.currencyexchange

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Offer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TradingFragment : Fragment() {
    private var fab: FloatingActionButton? = null
    private var fabrefresh:FloatingActionButton? = null
    private var tradingDialog: View? = null
    private var listview: ListView? = null
    private var offers: ArrayList<Offer>? = ArrayList()
    private var adapter: TradingFragment.TradingAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchoffers()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =  inflater.inflate(R.layout.fragment_trading, container, false)

        fab = view.findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }
        fabrefresh=view.findViewById(R.id.fab_refresh)
        fabrefresh?.setOnClickListener {
            offers?.clear()
            adapter?.notifyDataSetChanged()
            fetchoffers()
        }
        listview = view.findViewById(R.id.listview1)
        adapter = TradingFragment.TradingAdapter(layoutInflater, offers!!)
        listview?.adapter = adapter

        listview?.onItemClickListener =AdapterView.OnItemClickListener { parent, view, position, id ->

            val offer = offers?.get(position)
            if (offer != null) {
                offer.id?.let { acceptOffer(it) }
            }
        }
        return view
    }

    class TradingAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<Offer>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view:View=inflater.inflate(R.layout.item_trading,
                parent, false)
            view.findViewById<TextView>(R.id.txtView1).text = dataSource[position].id.toString()
            view.findViewById<TextView>(R.id.tvTransactionDate).text =  dataSource[position].addedDate
            view.findViewById<TextView>(R.id.tvUsdAmount).text = "USD: ${ dataSource[position].amountrequested}"
            view.findViewById<TextView>(R.id.tvLbpAmount).text = "LBP: ${ dataSource[position].amounttotrade}"
            val TYPEOFEXCHANGE = if ( dataSource[position].usdToLbp == true) "USD to LBP" else "LBP to USD"
            view.findViewById<TextView>(R.id.tvExchangeDirection).text = TYPEOFEXCHANGE
            return view
        }
        override fun getItem(position: Int): Any {
            return dataSource[position]
        }
        override fun getItemId(position: Int): Long {
            return dataSource[position].id?.toLong() ?: 0
        }
        override fun getCount(): Int {
            return dataSource.size
        }

    }private fun fetchoffers() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getOffers("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Offer>> {
                    override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                        Toast.makeText(requireContext(),"Failed to fetch offers.", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<List<Offer>>,
                        response: Response<List<Offer>>
                    ) {
                        if(response.body()==null){
                            Toast.makeText(requireContext(), "No offers found.", Toast.LENGTH_LONG).show()
                        }
                        else{
                            offers?.addAll(response.body()!!)
                            adapter?.notifyDataSetChanged()
                        }}
                })
        }
    }
    private fun showDialog() {
        tradingDialog= LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_trading, null, false)
        MaterialAlertDialogBuilder(requireContext()).setView(tradingDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                var errorMessage: String? = null
                try {
                    val amounttotrade = tradingDialog?.findViewById<TextInputLayout>(R.id.AmountToTrade)?.editText?.text.toString()
                    val amountrequested = tradingDialog?.findViewById<TextInputLayout>(R.id.AmountRequested)?.editText?.text.toString()
                    val exchangerate = tradingDialog?.findViewById<TextInputLayout>(R.id.ExchangeRate)?.editText?.text.toString()
                    val ttrade= amounttotrade.toFloatOrNull() ?: throw IllegalArgumentException("Amount To Trade is not valid")
                    val trequest = amountrequested.toFloatOrNull() ?: throw IllegalArgumentException("Amount Requested is not valid")
                    val trate = exchangerate.toFloatOrNull() ?: throw IllegalArgumentException("Exchnage Rate is not valid")

                    if (ttrade <= 0 || trequest <=0 || trate<=0) {
                        throw IllegalArgumentException("Inputs must not be negative")
                    }

                    var usdToLbp = false
                    val rg = tradingDialog?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)
                    val selectedId = rg?.checkedRadioButtonId ?: -1
                    if (selectedId == -1) {
                        Snackbar.make(fab!!, "Please select a transaction type.", Snackbar.LENGTH_LONG).show()
                        return@setPositiveButton
                    }
                    else {
                        val selectedRadioButton = tradingDialog?.findViewById<RadioButton>(selectedId)
                        val selectedValue = selectedRadioButton?.text.toString()
                        if (selectedValue == "Sell USD") {
                            usdToLbp = true
                        }
                    }
                    val offer=Offer().apply {
                        this.amountrequested=trequest
                        this.amounttotrade=ttrade
                        this.usdToLbp = usdToLbp
                    }
                    addOffer(offer)


                } catch (e: NumberFormatException) {
                    errorMessage = "Please enter a valid number."
                } catch (e: IllegalArgumentException) {
                    errorMessage = e.message
                }

                errorMessage?.let {
                    Snackbar.make(fab!!, it, Snackbar.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun acceptOffer(offerid: Int) {
        ExchangeService.exchangeApi().acceptOffer(offerid, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object :
            Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Snackbar.make(fab as View, "Trade Accepted!", Snackbar.LENGTH_LONG).show()
                } else {
                    Log.e("API Error", "Error Code: ${response.code()} - Message: ${response.message()}")
                    Snackbar.make(fab as View, "Failed to accept trade.", Snackbar.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not aaccept trade.", Snackbar.LENGTH_LONG).show()
            }
        })

    }
    private fun addOffer(offer: Offer) {
        ExchangeService.exchangeApi().addoffer(offer, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object :
            Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Snackbar.make(fab as View, "Trade added!", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(fab as View, "Failed to add trade.", Snackbar.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add trade.", Snackbar.LENGTH_LONG).show()
            }
        })

    }

}