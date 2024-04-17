package com.example.currencyexchange

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
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Offer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Myoffers : AppCompatActivity() {
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
        val view: View =inflater.inflate(R.layout.item_trading,
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
            .getacceptedoffers("Bearer ${Authentication.getToken()}")
            .enqueue(object : Callback<List<Offer>> {
                override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                    Toast.makeText(this@Myoffers,"Failed to fetch offers.", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<Offer>>,
                    response: Response<List<Offer>>
                ) {
                    if(response.body()==null){
                        Toast.makeText(this@Myoffers, "No offers found.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        offers?.addAll(response.body()!!)
                        adapter?.notifyDataSetChanged()
                    }}
            })
    }
}}