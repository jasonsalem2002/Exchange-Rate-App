package com.example.currencyexchange

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.data.FutureRate
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

class Prediction : AppCompatActivity() {
    private lateinit var startDatePicker: EditText
    private lateinit var dayDifference: TextView
    private lateinit var show: Button
    private lateinit var lineChart: LineChart
    private lateinit var spinn:Spinner
    private lateinit var ratesMap:Map<String, Map<String, Double>>
    private lateinit var predicted:List<FutureRate>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)
        startDatePicker = findViewById(R.id.startDate)
        dayDifference =findViewById(R.id.dayDifference)
        lineChart = findViewById(R.id.chart1)

        setupDatePickers()
        setupChart()
        initialgraph()
    }

    private fun setupChart() {
        val description = Description().apply {
            text = "Exchange Rate"
            textSize = 30f
            setPosition(150f, 15f)
        }
        lineChart.apply {
            this.description = description
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 15f
            axisLeft.apply {
                axisMinimum =87000f
                axisMaximum = 90000f
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
        val today = Calendar.getInstance()
        val thirtyDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -30)
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = dateFormat.format(today.time)
        val thirtyDaysAgoStr = dateFormat.format(thirtyDaysAgo.time)

        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getAvgRates("Bearer $token", thirtyDaysAgoStr, todayStr, "daily")
            call.enqueue(object : Callback<Map<String, Map<String, Double>>> {
                override fun onResponse(call: Call<Map<String, Map<String, Double>>>, response: Response<Map<String, Map<String, Double>>>) {
                    if (response.isSuccessful) {
                         ratesMap = response.body()!!
                    } else {
                        Toast.makeText(this@Prediction, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                    Toast.makeText(this@Prediction, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getNext30DaysRates("Bearer $token")
            call.enqueue(object : Callback<List<FutureRate>>{
                override fun onResponse(call: Call<List<FutureRate>>, response: Response<List<FutureRate>>) {
                    if (response.isSuccessful) {
                        predicted = response.body()!!
                        if (::ratesMap.isInitialized) {
                            displayChartData(ratesMap, predicted)
                        }

                    } else {
                        Toast.makeText(this@Prediction, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<FutureRate>>, t: Throwable) {
                    Toast.makeText(this@Prediction, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

   /* private fun fetchDataFromBackend(granularity:String) {
        val startDateStr = startDatePicker.text.toString()
        if (startDateStr.isEmpty() ) {
            Toast.makeText(this@Prediction, "Please select both start and end dates.", Toast.LENGTH_SHORT).show()
            return
        }
        Authentication.getToken()?.let { token ->
            val call = ExchangeService.exchangeApi().getAvgRates("Bearer $token", startDateStr, endDateStr, granularity)
            call.enqueue(object : Callback<Map<String, Map<String, Double>>> {
                override fun onResponse(call: Call<Map<String, Map<String, Double>>>, response: Response<Map<String, Map<String, Double>>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                        displayChartData(ratesMap)
                    } else {
                        Toast.makeText(this@Prediction, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                    Toast.makeText(this@Prediction, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }}
*/
    private fun displayChartData(ratesMap: Map<String, Map<String, Double>>?,predicted:List<FutureRate>) {
        ratesMap ?: return
       Log.e("size1",predicted.size.toString())


        val entriesUSDtoLBP = mutableListOf<Entry>()
       val entriesUSDtoLBPredicted = mutableListOf<Entry>()
        var indexUSDtoLBP = 0f
        ratesMap["usd_to_lbp"]?.forEach { (_, rate) ->
            entriesUSDtoLBP.add(Entry(indexUSDtoLBP++, rate.toFloat()))
        }
       for(ff in predicted){
           entriesUSDtoLBPredicted.add(Entry(indexUSDtoLBP++, ff.averageExchangeRate))

       }
       val dataSetpredicted = LineDataSet(entriesUSDtoLBPredicted, "USD to LBP Predicted").apply {
           color = android.graphics.Color.BLUE
           lineWidth = 2f
       }


        // Create dataset for USD to LBP
        val dataSetUSDtoLBP = LineDataSet(entriesUSDtoLBP, "USD to LBP").apply {
            color = android.graphics.Color.RED
            lineWidth = 2f
        }

        // Adding both datasets to the chart
        val lineData = LineData(dataSetUSDtoLBP,dataSetpredicted)
        lineChart.data = lineData
        lineChart.invalidate() // Refreshes the chart
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            updateDateText(startDatePicker, selectedDate)
            updateDateDifference()
        }

        startDatePicker.setOnClickListener {
            DatePickerDialog(this@Prediction, startDateSetListener,
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
        if (startDateStr.isEmpty())  {
            dayDifference.text = "Please select a date to predict the rate for a certain date."
            return
        }

        try {
            val startDate = format.parse(startDateStr)
            val today = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val todayStr = dateFormat.format(today.time)
            if (startDate != null ) {
                if (!startDate.before(today.time)) {
                    Authentication.getToken()?.let { token ->
                        val call = ExchangeService.exchangeApi().getPredRate("Bearer $token", startDateStr)
                        call.enqueue(object : Callback<FutureRate> {
                            override fun onResponse(call: Call<FutureRate>, response: Response<FutureRate>) {
                                if (response.isSuccessful) {
                                    dayDifference.text = "Predicted Rate: "+response.body()?.averageExchangeRate.toString()
                                } else {
                                    Toast.makeText(this@Prediction, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<FutureRate>, t: Throwable) {
                                Toast.makeText(this@Prediction, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }


                   /* show.setOnClickListener {
                        fetchDataFromBackend(spinn.selectedItem.toString())
                    }*/
                } else {
                    startDatePicker.text.clear()

                    dayDifference.text = "End date must be after start date"
                }
            }
        } catch (e: ParseException) {
            dayDifference.text = "Invalid dates"
        }
    }

}

