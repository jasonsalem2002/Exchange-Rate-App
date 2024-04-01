package com.example.currencyexchange

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
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
        setupConversionUI(view)
        return view
    }
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
                Log.e("Error","Failed")
            }
        })
    }

}