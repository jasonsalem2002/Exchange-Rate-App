package com.example.currencyexchange

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
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
    private lateinit var etAmount: EditText
    private lateinit var tvConvertedAmount: TextView
    private lateinit var rgConversionDirection: RadioGroup
    private lateinit var btnConvert: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchrates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_exchange, container, false)
        fetchrates()
        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)
        fab = view.findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }
        return view
    }
    /*
    private fun setupConversionUI(view: View) {
        etAmount = view.findViewById(R.id.etAmount)
        tvConvertedAmount = view.findViewById(R.id.tvConvertedAmount)
        rgConversionDirection = view.findViewById(R.id.rgConversionDirection)
        btnConvert = view.findViewById(R.id.btnConvert)

        btnConvert.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amount = etAmount.text.toString().toDoubleOrNull()
        val sellRate = sellUsdTextView?.text.toString().toDoubleOrNull()
        val buyRate = buyUsdTextView?.text.toString().toDoubleOrNull()

        if (amount == null || amount <= 0) {
            tvConvertedAmount.text = "Please enter a valid, positive amount"
            return
        }

        if (sellRate == null  || buyRate == null ) {
            tvConvertedAmount.text = "Exchange rates are not available. Please try again later."
            return
        }
        val convertedAmount = when (rgConversionDirection.checkedRadioButtonId) {
            R.id.rbUsdToLbp -> amount * sellRate
            R.id.rbLbpToUsd -> amount / buyRate
            else -> null
        }

        if (convertedAmount != null) {
            tvConvertedAmount.text = "Calculated:" +String.format("%.2f", convertedAmount)
        } else {
            tvConvertedAmount.text = "Please select a conversion type" +
                    "."
        }
    }*/

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
                Log.e("Error","Failed")
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