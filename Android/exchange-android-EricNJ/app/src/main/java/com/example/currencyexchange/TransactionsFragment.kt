package com.example.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionsFragment : Fragment() {
    private var listview: ListView? = null
    private var transactions: ArrayList<Transaction>? = ArrayList()
    private var adapter: TransactionAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    fetchTransactions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transactions,
            container, false)
        listview = view.findViewById(R.id.listview)
        adapter = TransactionAdapter(layoutInflater, transactions!!)
        listview?.adapter = adapter
        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       // outState.putParcelableArrayList("trans",transactions)
    }
    class TransactionAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<Transaction>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view:View=inflater.inflate(R.layout.item_transaction,
                parent, false)
            view.findViewById<TextView>(R.id.txtView).text = dataSource[position].id.toString()
            view.findViewById<TextView>(R.id.tvTransactionDate).text =  dataSource[position].addedDate
            view.findViewById<TextView>(R.id.tvUsdAmount).text = "USD: ${ dataSource[position].usdAmount}"
            view.findViewById<TextView>(R.id.tvLbpAmount).text = "LBP: ${ dataSource[position].lbpAmount}"

            // Determine and set the transaction direction
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

}private fun fetchTransactions() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getTransactions("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Transaction>> {
                    override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                        Toast.makeText(requireContext(),"Failed to fetch transactions.Make sure you are connected to the internet.", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<List<Transaction>>,
                        response: Response<List<Transaction>>
                    ) {
                        if(response.body()==null){
                            Toast.makeText(requireContext(), "No transactions found.", Toast.LENGTH_LONG).show()
                        }
                        else{
                        transactions?.addAll(response.body()!!)
                        adapter?.notifyDataSetChanged()
                    }}
                })
        }
    }
}