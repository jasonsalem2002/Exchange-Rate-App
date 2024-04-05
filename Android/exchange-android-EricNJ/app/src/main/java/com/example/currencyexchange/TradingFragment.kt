package com.example.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TradingFragment : Fragment() {
    private var fab: FloatingActionButton? = null
    private var tradingDialog: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return view
    }
    private fun showDialog() {
        tradingDialog= LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(requireContext()).setView(tradingDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                var errorMessage: String? = null
                try {
                    val usdAmountText = tradingDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)?.editText?.text.toString()
                    val lbpAmountText = tradingDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)?.editText?.text.toString()
                    val usdAmount = usdAmountText.toFloatOrNull() ?: throw IllegalArgumentException("USD Amount is not valid")
                    val lbpAmount = lbpAmountText.toFloatOrNull() ?: throw IllegalArgumentException("LBP Amount is not valid")

                    if (usdAmount < 0 || lbpAmount < 0) {
                        throw IllegalArgumentException("Amounts must not be negative")
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
                    }/*
                    val transaction = Transaction().apply {
                        this.usdAmount = usdAmount
                        this.lbpAmount = lbpAmount
                        this.usdToLbp = usdToLbp
                    }*/
                  //  addTransaction(transaction)


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
   /* private fun addTransaction(transaction: Transaction) {
        ExchangeService.exchangeApi().addTransaction(transaction, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object :
            Callback<Any> {

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

    }*/

}