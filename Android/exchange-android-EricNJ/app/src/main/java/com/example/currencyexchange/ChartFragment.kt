package com.example.currencyexchange

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var show: Button
    private lateinit var lineChart: LineChart
    private lateinit var spinn:Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chart, container, false)
        startDatePicker = view.findViewById(R.id.startDate)
        endDatePicker = view.findViewById(R.id.endDate)
        dayDifference = view.findViewById(R.id.dayDifference)
        lineChart = view.findViewById(R.id.chart1)
        show=view.findViewById(R.id.showGraphButton)
        spinn=view.findViewById(R.id.granularitySpinner)
        setupDatePickers()
        setupChart()
        initialgraph()
        return view
    }

    private fun setupChart() {
        val description = Description().apply {
            text = "Rate"
            textSize = 18f
            setPosition(150f, 15f)
        }
        lineChart.apply {
            this.description = description
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 15f
            axisLeft.apply {
                axisMinimum =87000f
                axisMaximum = 91000f
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
            val call = ExchangeService.exchangeApi().getAvgRates("Bearer $token","2024-04-02", "2024-04-20","daily")
            call.enqueue(object : Callback<Map<String, Map<String, Double>>> {
                override fun onResponse(call: Call<Map<String, Map<String, Double>>>, response: Response<Map<String, Map<String, Double>>>) {
                    if (response.isSuccessful) {
                        val ratesMap = response.body()
                        displayChartData(ratesMap)
                    } else {
                        Toast.makeText(context, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                    Toast.makeText(context, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }}

    private fun fetchDataFromBackend(granularity:String) {
        val startDateStr = startDatePicker.text.toString()
        val endDateStr = endDatePicker.text.toString()


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
                    Toast.makeText(context, "There is no data to be shown at this time.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Map<String, Map<String, Double>>>, t: Throwable) {
                Toast.makeText(context, "Failed to show Graph.Make sure you are connected to the internet.", Toast.LENGTH_SHORT).show()
            }
        })
    }}

    private fun displayChartData(ratesMap: Map<String, Map<String, Double>>?) {
        ratesMap ?: return

        // Entries for LBP to USD
        val entriesLBPtoUSD = mutableListOf<Entry>()
        var indexLBPtoUSD = 0f
        ratesMap["lbp_to_usd"]?.forEach { (_, rate) ->
            entriesLBPtoUSD.add(Entry(indexLBPtoUSD++, rate.toFloat()))
        }

        // Entries for USD to LBP
        val entriesUSDtoLBP = mutableListOf<Entry>()
        var indexUSDtoLBP = 0f
        ratesMap["usd_to_lbp"]?.forEach { (_, rate) ->
            entriesUSDtoLBP.add(Entry(indexUSDtoLBP++, rate.toFloat()))
        }
            Log.e("hhh",entriesUSDtoLBP.size.toString())
        // Create dataset for LBP to USD
        val dataSetLBPtoUSD = LineDataSet(entriesLBPtoUSD, "LBP to USD").apply {
            color = android.graphics.Color.BLUE
            lineWidth = 2f
        }

        // Create dataset for USD to LBP
        val dataSetUSDtoLBP = LineDataSet(entriesUSDtoLBP, "USD to LBP").apply {
            color = android.graphics.Color.RED
            lineWidth = 2f
        }

        // Adding both datasets to the chart
        val lineData = LineData(dataSetLBPtoUSD, dataSetUSDtoLBP)
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

                    show.setOnClickListener {
                        fetchDataFromBackend(spinn.selectedItem.toString())
                    }
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
