package com.example.currencyexchange

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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

class ChartFragment : Fragment() {
    private lateinit var startDatePicker: EditText
    private lateinit var endDatePicker: EditText
    private lateinit var dayDifference: TextView

    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chart, container, false)
        startDatePicker = view.findViewById(R.id.startDate)
        endDatePicker = view.findViewById(R.id.endDate)
        dayDifference = view.findViewById(R.id.dayDifference)
        lineChart = view.findViewById(R.id.chart1)
        setupDatePickers()
        setupChart()

        return view
    }

    private fun setupChart() {
        val description = Description().apply {
            text = "Exchange Rate Trend"
            setPosition(150f, 15f)
        }
        lineChart.apply {
            this.description = description
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 15f
            axisLeft.apply {
                axisMinimum =0f
                axisMaximum = 90000f
                axisLineWidth = 2f
                textSize = 15f
                labelCount = 10
            }
            setTouchEnabled(true)

            // Enable scaling and dragging
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)

        // Set the maximum and minimum scaling factors
        // scaleXMax = 5.0f  // Max zoom limit for X-axis
        //scaleYMax = 1.0f

        }
    }

    /*private fun fetchDataFromBackend() {

        val exampleData = listOf(88f, 89f, 90f, 88.6f)
        val reversedData = exampleData.reversed()
        displayChartData(reversedData)
    }*/
    private fun fetchDataFromBackend() {
        val startDateStr = startDatePicker.text.toString()
        val endDateStr = endDatePicker.text.toString()
        val granularity = "daily"

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(context, "Please select both start and end dates.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }}

    private fun displayChartData(ratesMap: Map<String, Map<String, Double>>?) {
        ratesMap ?: return

        val entries = mutableListOf<Entry>()
        var index = 0f
        ratesMap["lbp_to_usd"]?.forEach { (date, rate) ->
            entries.add(Entry(index++, rate.toFloat()))

        }

        for(dt in entries){
            Log.e("logpaper",dt.toString())
        }
        val dataSet = LineDataSet(entries, "LBP to USD Exchange Rate")
        dataSet.lineWidth = 2f
        lineChart.data = LineData(dataSet)
        lineChart.invalidate() // Refreshes the chart
    }

/*
    private fun displayChartData(data: List<Float>) {
        val entries = data.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }

        val dataSet = LineDataSet(entries, "Exchange Rate").apply {
            lineWidth = 2f
        }

        lineChart.data = LineData(dataSet)
        lineChart.invalidate() // Refreshes the chart
    }
*/
    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()

        // Start Date Picker
        val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            updateDateText(startDatePicker, selectedDate)
            updateDateDifference()
        }

        startDatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), startDateSetListener,
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
            DatePickerDialog(requireContext(), endDateSetListener,
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

        // Check if either date string is empty
        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            dayDifference.text = "Please select both dates."
            return
        }

        try {
            val startDate = format.parse(startDateStr)
            val endDate = format.parse(endDateStr)

            if (startDate != null && endDate != null) {
                if (!endDate.before(startDate)) {
                    val diff = endDate.time - startDate.time
                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    dayDifference.text = "Difference: $days days"


                    fetchDataFromBackend()



                } else {
                    startDatePicker.text.clear()
                    endDatePicker.text.clear()
                    dayDifference.text = "End date must be after start date"
                }
            }
        } catch (e: ParseException) {
            dayDifference.text = "Invalid dates"
        }
    }

}
