package com.example.currencyexchange.trading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Offer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Myacceptedoffers : AppCompatActivity() {
    private var listview: ListView? = null
    private var offers: ArrayList<Offer>? = ArrayList()
    private var adapter: TradingAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myoffers)
        listview = findViewById(R.id.listview1)
        adapter = TradingAdapter(layoutInflater, offers!!)
        listview?.adapter = adapter

        var imgback:ImageView=findViewById(R.id.backButton)
        imgback.setOnClickListener {
            finish()
        }
        fetchoffers()

    }


class TradingAdapter(
    private val inflater: LayoutInflater,
    private val dataSource: List<Offer>
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

}private fun fetchoffers() {
    if (Authentication.getToken() != null) {
        ExchangeService.exchangeApi()
            .getacceptedoffers("Bearer ${Authentication.getToken()}")
            .enqueue(object : Callback<List<Offer>> {
                override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                    Toast.makeText(this@Myacceptedoffers,"Failed to fetch accepted offers.Make sure you are connected to teh internet.", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<Offer>>,
                    response: Response<List<Offer>>
                ) {
                    if(response.body()?.isEmpty() == true){
                        Toast.makeText(this@Myacceptedoffers, "No offers yet.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        offers?.addAll(response.body()!!)
                        adapter?.notifyDataSetChanged()
                    }}
            })
    }
}}