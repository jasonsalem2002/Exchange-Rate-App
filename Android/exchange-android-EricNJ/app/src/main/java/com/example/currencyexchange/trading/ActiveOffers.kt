package com.example.currencyexchange.trading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Offer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActiveOffers : AppCompatActivity() {
    private var listview: ListView? = null
    private var fab: FloatingActionButton? = null
    private var offers: ArrayList<Offer>? = ArrayList()
    private var adapter: TradingAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_offers)
        listview = findViewById(R.id.listview1)
        adapter = TradingAdapter(layoutInflater, offers!!)
        listview?.adapter = adapter
        listview?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val offer = adapter?.getItem(position) as? Offer // Use adapter's getItem method to ensure correct data
            offer?.let {
                MaterialAlertDialogBuilder(this@ActiveOffers)
                    .setTitle("Select an Action")
                    .setMessage("Delete an Offer.")
                    .setPositiveButton("Delete") { dialog, _ ->
                        it.id?.let { offerId -> deleteOffer(offerId) }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        var imgback:ImageView=findViewById(R.id.backButton)
        imgback.setOnClickListener {
            finish()
        }
        fetchoffers()

    }


    class TradingAdapter(
        private val inflater: LayoutInflater,
        var dataSource: List<Offer>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view: View =inflater.inflate(
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

    }    private fun fetchoffers() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getOffers("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Offer>> {
                    override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                        Toast.makeText(this@ActiveOffers,"Failed to fetch active offers.Make sure you are connected to the internet.", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<List<Offer>>,
                        response: Response<List<Offer>>
                    ) {
                        if(response.body()?.isEmpty() == true){
                            Toast.makeText(this@ActiveOffers, "No offers yet.", Toast.LENGTH_LONG).show()
                        }
                        else{
                            offers?.clear()
                            offers?.addAll(response.body()!!)
                            adapter?.notifyDataSetChanged()

                            Authentication.getUsername()?.let { filterOffers(it) }

                        }}
                })
        }
    } private fun filterOffers(name:String) {
        adapter?.let {
            it.dataSource = offers?.filter { offer -> offer.user == name  } ?: listOf()
            it.notifyDataSetChanged()
        }
    }


    private fun deleteOffer(offerid: Int) {
        ExchangeService.exchangeApi().deleteOffer(offerid, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object :
            Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Snackbar.make(fab as View, "Trade Deleted!", Snackbar.LENGTH_LONG).show()
                    finish()

                } else {
                    Log.e("API Error", "Error Code: ${response.code()} - Message: ${response.message()}")
                    Snackbar.make(fab as View, "Failed to accept trade.", Snackbar.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not delete trade.Try Again", Snackbar.LENGTH_LONG).show()
            }
        })

    }}