package com.example.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.BlendMode.Companion.Color

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
class ChartFragment : Fragment() {

    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chart, container, false)
        lineChart = view.findViewById(R.id.chart1)
        setupChart()
        fetchDataFromBackend()
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
                axisMinimum = 88f
                axisMaximum = 90f
                axisLineWidth = 2f
                textSize = 15f
                labelCount = 10
            }
        }
    }

    private fun fetchDataFromBackend() {
        // Simulate fetching data from the backend
        val exampleData = listOf(88f, 89f, 90f, 88.6f)
        val reversedData = exampleData.reversed() // This correctly reverses and stores the result
        displayChartData(reversedData)
    }

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
}
