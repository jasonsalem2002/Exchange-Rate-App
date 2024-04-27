package com.example.currencyexchange.trading

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.chatting.Convo
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Offer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
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
    private var adapter: TradingAdapter? = null
    private var switchTradeDirection: Switch? = null
    private lateinit var toggle:TextView

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
        toggle=view.findViewById(R.id.toggletext)
        fabrefresh=view.findViewById(R.id.fab_refresh)
        fabrefresh?.setOnClickListener {
            offers?.clear()
            adapter?.notifyDataSetChanged()
            fetchoffers()
        }


        var viewmyoffersbutton: Button =view.findViewById(R.id.button_view_my_offers)
        viewmyoffersbutton.setOnClickListener {
            var intent90=Intent(requireContext(), Myacceptedoffers::class.java)
            startActivity(intent90)
        }
        var viewActiveoffersbutton: Button =view.findViewById(R.id.button4)
        viewActiveoffersbutton.setOnClickListener {
            var intent900 = Intent(requireContext(), ActiveOffers::class.java)
            startActivity(intent900)
        }

        switchTradeDirection = view.findViewById(R.id.switch_trade_direction)
        switchTradeDirection?.setOnCheckedChangeListener { _, isChecked ->
            filterOffers(isChecked)
        }

        listview = view.findViewById(R.id.listview1)
        adapter = TradingAdapter(layoutInflater, offers!!)
        listview?.adapter = adapter

        listview?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val offer = adapter?.getItem(position) as? Offer // Use adapter's getItem method to ensure correct data
            offer?.let {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Select an Action")
                    .setMessage("Choose to accept the offer or start a chat.")
                    .setPositiveButton("Accept") { dialog, _ ->
                        it.id?.let { offerId -> acceptOffer(offerId) }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Chat") { dialog, _ ->
                        val intent = Intent(activity, Convo::class.java).apply {
                            putExtra("username", it.user) // Assuming you want to pass the offer ID to the chat activity
                        }
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        return view
    }

    class TradingAdapter(
        private val inflater: LayoutInflater,
        var dataSource: List<Offer>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view:View=inflater.inflate(
                R.layout.item_trading,
                parent, false)
            view.findViewById<TextView>(R.id.txtView1).text = dataSource[position].user.toString()
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

    }
    private fun filterOffers(usdToLbp: Boolean) {
        if(usdToLbp){
            toggle.text="Usd to Lbp"
        }
        else{
            toggle.text="Lbp to Usd"
        }
        adapter?.let {
            it.dataSource = offers?.filter { offer -> offer.usdToLbp == usdToLbp && offer.user != Authentication.getUsername() } ?: listOf()

            it.notifyDataSetChanged()
        }
    }
    private fun fetchoffers() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getOffers("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Offer>> {
                    override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                        Toast.makeText(requireContext(),"Failed to fetch offers.Make sure you are connected to the internet.", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<List<Offer>>,
                        response: Response<List<Offer>>
                    ) {
                        if(response.body()?.isEmpty() == true){
                            Toast.makeText(requireContext(), "No offers found.", Toast.LENGTH_LONG).show()
                        }
                        else{
                            offers?.clear()
                            offers?.addAll(response.body()!!)
                            adapter?.notifyDataSetChanged()
                            switchTradeDirection?.isChecked?.let {
                                filterOffers(it)
                            }
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
            //        val exchangerate = tradingDialog?.findViewById<TextInputEditText>(R.id.ExchangeRate)
                    val ttrade= amounttotrade.toFloatOrNull() ?: throw IllegalArgumentException("Amount To Trade is not valid")
                    val trequest = amountrequested.toFloatOrNull() ?: throw IllegalArgumentException("Amount Requested is not valid")
                    val radioGroup = tradingDialog?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)
            /*        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
                        val isUsdToLbp = checkedId == R.id.rdGrpTransactionType
                        updateExchangeRate(amounttotrade, amountrequested,exchangerate, isUsdToLbp)
                    }*/
                    if (ttrade <= 0 || trequest <=0) {
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
    private fun updateExchangeRate(amountToTradeInput:String?, amountRequestedInput: String?, exchangeRateInput: TextInputEditText?, usdToLbp: Boolean) {
        val amountToTrade = amountToTradeInput?.toFloatOrNull()
        val amountRequested = amountRequestedInput?.toFloatOrNull()

        if (amountToTrade != null && amountRequested != null && amountToTrade > 0 && amountRequested > 0) {
            val exchangeRate = if (usdToLbp) amountToTrade / amountRequested else amountRequested / amountToTrade
            exchangeRateInput?.setText(exchangeRate.toString())
        }
    }
    private fun acceptOffer(offerid: Int) {
        ExchangeService.exchangeApi().acceptOffer(offerid, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object :
            Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Snackbar.make(fab as View, "Trade Accepted!", Snackbar.LENGTH_LONG).show()
                    fetchoffers()
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