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

    private lateinit var linechart1: LineChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chart, container, false)
        linechart1 = view.findViewById(R.id.chart1)
        val description = Description().apply {
            text = "Exchange Rate"
            setPosition(150f, 15f)
        }
        linechart1.apply {
            setDescription(description)
            axisRight.isEnabled = false // Disables right Y-Axis
        }

        val xValues = listOf("One", "Two", "Three", "Four")
        linechart1.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(xValues)
            labelCount = 4
            textSize=15f
            granularity = 1f
        }

        linechart1.axisLeft.apply {
            axisMinimum = 88f
            axisMaximum = 90f
            axisLineWidth = 2f
            textSize=15f
            // axisLineColor = Color.BLACK
            labelCount = 10
        }

        val revenueComp1 = arrayListOf(88f, 89f, 90f, 88.6f)
        val entries1 = revenueComp1.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }

        val dataSet = LineDataSet(entries1, "Revenue").apply {
            //color = Color.BLUE
            //valueTextColor = Color.BLACK
            lineWidth = 2f
        }

        linechart1.data = LineData(dataSet)
        linechart1.invalidate() // Refreshes the chart

        return view
    }


}