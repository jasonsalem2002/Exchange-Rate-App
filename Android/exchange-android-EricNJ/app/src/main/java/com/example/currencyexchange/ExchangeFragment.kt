package com.example.currencyexchange


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.chatting.ChatsActivity
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.ExchangeRates
import com.example.currencyexchange.api.model.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView? = null
    private var fab: FloatingActionButton? = null
    private var sellUsdTextView: TextView? = null
    private var transactionDialog: View? = null
    private lateinit var data:String
    private lateinit var data2:String
    private var buttonstats :Button?=null
    private var buttonprediction :Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchrates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_exchange, container, false)

        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)

        buttonstats=view.findViewById<Button?>(R.id.buttonstats)
        buttonstats?.setOnClickListener {
            var intentstat=Intent(requireContext(),stats::class.java)
            startActivity(intentstat)
        }
        buttonprediction=view.findViewById<Button?>(R.id.buttonprediction)
        buttonprediction?.setOnClickListener {
            var intentpred=Intent(requireContext(),Prediction::class.java)
            startActivity(intentpred)
        }
        if (savedInstanceState != null) {
            data = savedInstanceState.getString("usd-lbp", "")
            data2 = savedInstanceState.getString("lbp-usd", "")
            buyUsdTextView?.text = data2
            sellUsdTextView?.text=data
        }
        else{
            fetchrates()
        }


        var buttchat:Button
        buttchat=view.findViewById(R.id.Chatbutton)
        buttchat.setOnClickListener {
            val intent=Intent(activity, ChatsActivity::class.java)
            startActivity(intent)
        }
        fab = view.findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }
        var calcbutton:Button=view.findViewById(R.id.buttonforcalculator)
        calcbutton.setOnClickListener {
            var intentcalc=Intent(requireContext(),Calculator::class.java)
            intentcalc.putExtra("sell", sellUsdTextView?.text.toString())
            intentcalc.putExtra("buy",buyUsdTextView?.text.toString())

            startActivity(intentcalc)
        }
        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save data to the bundle
        outState.putString("usd-lbp", sellUsdTextView?.text.toString())
        outState.putString("lbp-usd", buyUsdTextView?.text.toString())
    }
    fun fetchrates(){
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>
            ) {
                val responseBody: ExchangeRates? = response.body()
                responseBody?.let {
                    val buyRate = it.lbpToUsd.toString()
                    val sellRate = it.usdToLbp.toString()
                        buyUsdTextView?.text = buyRate
                        sellUsdTextView?.text = sellRate

                }
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                Toast.makeText(requireContext(),"Could not fetch rates, Check your internet connection.",Toast.LENGTH_LONG)
            }
        })
    }
    private fun showDialog() {
        transactionDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(requireContext()).setView(transactionDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                var errorMessage: String? = null
                try {
                    val usdAmountText = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)?.editText?.text.toString()
                    val lbpAmountText = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)?.editText?.text.toString()
                    val usdAmount = usdAmountText.toFloatOrNull() ?: throw IllegalArgumentException("USD Amount is not valid")
                    val lbpAmount = lbpAmountText.toFloatOrNull() ?: throw IllegalArgumentException("LBP Amount is not valid")

                    if (usdAmount < 0 || lbpAmount < 0) {
                        throw IllegalArgumentException("Amounts must not be negative")
                    }

                    var usdToLbp = false
                    val rg = transactionDialog?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)
                    val selectedId = rg?.checkedRadioButtonId ?: -1
                    if (selectedId == -1) {
                        Snackbar.make(fab!!, "Please select a transaction type.", Snackbar.LENGTH_LONG).show()
                        return@setPositiveButton
                    }
                    else {
                        val selectedRadioButton = transactionDialog?.findViewById<RadioButton>(selectedId)
                        val selectedValue = selectedRadioButton?.text.toString()
                        if (selectedValue == "Sell USD") {
                            usdToLbp = true
                        }
                    }
                    val transaction = Transaction().apply {
                        this.usdAmount = usdAmount
                        this.lbpAmount = lbpAmount
                        this.usdToLbp = usdToLbp
                    }
                    addTransaction(transaction)
                    fetchrates()
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
    private fun addTransaction(transaction: Transaction) {
        ExchangeService.exchangeApi().addTransaction(transaction, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Snackbar.make(fab as View, "Transaction added!", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(fab as View, "Failed to add transaction.", Snackbar.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add transaction.", Snackbar.LENGTH_LONG).show()
            }
        })

    }

}