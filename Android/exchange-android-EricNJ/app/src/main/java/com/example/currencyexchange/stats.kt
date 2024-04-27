package com.example.currencyexchange

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Transaction
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class stats : AppCompatActivity() {
    private lateinit var startDatePicker: EditText
    private lateinit var endDatePicker: EditText
  //  private lateinit var dayDifference: TextView
    private lateinit var show: Button
    private lateinit var lineChart: LineChart
    private lateinit var spinn: Spinner
    private lateinit var highestamount:TextView
    private lateinit var largestamount:TextView
    private lateinit var volumeamount:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        spinn=findViewById(R.id.granularitySpinner)
        startDatePicker = findViewById(R.id.startDate)
        endDatePicker = findViewById(R.id.endDate)
        lineChart = findViewById(R.id.chart1)
        show=findViewById(R.id.showGraphButton)
        highestamount=findViewById(R.id.highestamount)
        largestamount=findViewById(R.id.largestamount)
        volumeamount=findViewById(R.id.volumeamount)
        setupDatePickers()
        setupChart()
        initialgraph()
    /*    var generate:Button=findViewById(R.id.showGraphButton)
        generate.setOnClickListener {
            fetchDataFromBackend(spinn.selectedItem.toString())
        }*/
    }
    private fun setupChart() {

        lineChart.apply {

            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 15f
            axisLeft.apply {
                axisMinimum =0f
                axisMaximum = 30f
                axisLineWidth = 2f
                textSize = 15f
                labelCount = 10
            }
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)

        }
    }
    private fun initialgraph() {

        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getNumberOfTransactions("Bearer $token","2024-04-02", "2024-04-20","daily")
            call.enqueue(object : Callback<Map<String, Map<String, Double>>> {
                override fun onResponse(call: Call<Map<String, Map<String, Double>>>, response: Response<Map<String, Map<String, Double>>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                        displayChartData(ratesMap)
                    } else {
                        Toast.makeText(this@stats, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                    Toast.makeText(this@stats, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
















    }

    private fun fetchDataFromBackend(granularity:String) {
        val startDateStr = startDatePicker.text.toString()
        val endDateStr = endDatePicker.text.toString()


        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this@stats, "Please select both start and end dates.", Toast.LENGTH_SHORT).show()
            return
        }
        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getNumberOfTransactions("Bearer $token", startDateStr, endDateStr, granularity)
            call.enqueue(object : Callback<Map<String, Map<String, Double>>> {
                override fun onResponse(call: Call<Map<String, Map<String, Double>>>, response: Response<Map<String, Map<String, Double>>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                        displayChartData(ratesMap)
                    } else {
                        Toast.makeText(this@stats, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                    Toast.makeText(this@stats, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getHighestTransactionToday("Bearer $token")
            call.enqueue(object :Callback<Map<String?, Transaction>> {
                override fun onResponse(call: Call<Map<String?, Transaction>>, response: Response<Map<String?, Transaction>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                        if(ratesMap?.get("highest_transaction")?.usdToLbp==true){
                            highestamount.text= ratesMap?.get("highest_transaction")?.usdAmount.toString()+" $ TO\n"+ratesMap?.get("highest_transaction")?.lbpAmount.toString()+" LBP" ?: "Not available yet"
                        }
                        else{
                        highestamount.text= ratesMap?.get("highest_transaction")?.lbpAmount.toString()+" LBP TO\n"+ratesMap?.get("highest_transaction")?.usdAmount.toString()+" $" ?: "Not available yet" }
                    } else {
                        Toast.makeText(this@stats, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String?, Transaction>>, t: Throwable) {
                    Toast.makeText(this@stats, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getVolumeOfTransactions("Bearer $token",startDateStr, endDateStr)
            call.enqueue(object :Callback<Map<String?, Float>> {
                override fun onResponse(call: Call<Map<String?, Float>>, response: Response<Map<String?, Float>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                       volumeamount.text= "USD: "+ratesMap?.get("usd_volume")?.toString()+"\nLBP: "+ratesMap?.get("lbp_volume")?.toString() ?: "Not available yet"
                    } else {
                        Toast.makeText(this@stats, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String?, Float>>, t: Throwable) {
                    Toast.makeText(this@stats, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getLargestTransactionAmount("Bearer $token",startDateStr, endDateStr)
            call.enqueue(object :Callback<Map<String?, Transaction>> {
                override fun onResponse(call: Call<Map<String?, Transaction>>, response: Response<Map<String?, Transaction>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                        if(ratesMap?.get("largest_transaction")?.usdToLbp==true){
                            largestamount.text= ratesMap?.get("largest_transaction")?.usdAmount.toString()+" $ TO\n"+ratesMap?.get("largest_transaction")?.lbpAmount.toString()+" LBP" ?: "Not available yet"
                        }
                        else{
                            largestamount.text= ratesMap?.get("largest_transaction")?.lbpAmount.toString()+" lBP TO\n"+ratesMap?.get("largest_transaction")?.usdAmount.toString()+" $" ?: "Not available yet" }

                    } else {
                        Toast.makeText(this@stats, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String?, Transaction>>, t: Throwable) {
                    Toast.makeText(this@stats, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }






















    }

    private fun displayChartData(ratesMap: Map<String, Map<String, Double>>?) {
        ratesMap ?: return
        Log.e("shefelnayeha",ratesMap.size.toString().toString())
        // Entries for LBP to USD
        val entriesLBPtoUSD = mutableListOf<Entry>()
        var indexLBPtoUSD = 0f
        ratesMap["transactions_per_period"]?.forEach { (_, rate) ->
            entriesLBPtoUSD.add(Entry(indexLBPtoUSD++, rate.toFloat()))

        }


        // Create dataset for LBP to USD
        val dataSetLBPtoUSD = LineDataSet(entriesLBPtoUSD, "Number of Transactions").apply {
            color = android.graphics.Color.BLUE
            lineWidth = 2f
        }


        val lineData = LineData(dataSetLBPtoUSD)
        lineChart.data = lineData
        lineChart.invalidate() // Refreshes the chart
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()

        val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            updateDateText(startDatePicker, selectedDate)
            updateDateDifference()
        }

        startDatePicker.setOnClickListener {
            DatePickerDialog(this@stats, startDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // End Date Picker
        val endDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            updateDateText(endDatePicker, selectedDate)
            updateDateDifference()
        }

        endDatePicker.setOnClickListener {
            DatePickerDialog(this@stats, endDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateDateText(editText: EditText, calendar: Calendar) {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.US)
        editText.setText(sdf.format(calendar.time))
    }

    private fun updateDateDifference() {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val startDateStr = startDatePicker.text.toString()
        val endDateStr = endDatePicker.text.toString()


        try {
            val startDate = format.parse(startDateStr)
            val endDate = format.parse(endDateStr)

            if (startDate != null && endDate != null) {
                if (!endDate.before(startDate)) {
                    show.setOnClickListener {
                        fetchDataFromBackend(spinn.selectedItem.toString())
                    }
                } else {
                    startDatePicker.text.clear()
                    endDatePicker.text.clear()
                    Toast.makeText(this@stats, "Start Date should be before End Date.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: ParseException) {
          //  dayDifference.text = "Invalid dates"
        }
    }

}
