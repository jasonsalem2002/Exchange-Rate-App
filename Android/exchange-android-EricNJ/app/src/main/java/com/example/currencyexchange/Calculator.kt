package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView

class Calculator : AppCompatActivity() {
    private lateinit var etAmount: EditText
    private lateinit var tvConvertedAmount: TextView
    private lateinit var rgConversionDirection: RadioGroup
    private lateinit var btnConvert: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        setupConversionUI()
    }
    private fun setupConversionUI() {
        etAmount = findViewById(R.id.etAmount)
        tvConvertedAmount = findViewById(R.id.tvConvertedAmount)
        rgConversionDirection = findViewById(R.id.rgConversionDirection)
        btnConvert = findViewById(R.id.btnConvert)

        btnConvert.setOnClickListener {
            convertCurrency()
        }
        var back:ImageView=findViewById(R.id.backButton)
        back.setOnClickListener {
            finish()
        }
    }

    private fun convertCurrency() {
        val amount = etAmount.text.toString().toDoubleOrNull()
        val sellRate = intent.getStringExtra("sell")?.toDoubleOrNull()
        val buyRate = intent.getStringExtra("buy")?.toDoubleOrNull()

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
}