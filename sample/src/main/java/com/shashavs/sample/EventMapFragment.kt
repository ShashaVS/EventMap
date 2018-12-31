package com.shashavs.sample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashavs.eventmap.drag_scale.base.Marker
import com.shashavs.eventmap.drag_scale.base.MarkerListener
import com.shashavs.eventmap.drag_scale.base.MarkerView
import com.shashavs.eventmap.drag_scale.default_view.DefaultInfoLayout
import com.shashavs.sample.dummy.DummyData
import kotlinx.android.synthetic.main.fragment_seating.*

private const val ARG_TYPE = "type"

class EventMapFragment : Fragment() {

    private var type: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { type = it.getInt(ARG_TYPE) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(type) {
            1 -> initMapOne()
            2 -> initMapTwo()
        }

        map_container.addMarkerListenet( object : MarkerListener {
            override fun onMarker(markerView: MarkerView) {
                Snackbar.make(map_container, "Marker: ${markerView.marker.id}", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun initMapOne() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)

        map_container.getMapImageView().setImageResource(R.drawable.seating_plan)

        DummyData().data1.forEachIndexed { index, dummy ->
            val marker = Marker(dummy.id,dummy.x * metrics.density,dummy.y * metrics.density)

            val markerView = MarkerView.Builder(requireContext(), marker)
                .background(R.drawable.default_title_marker)
                .text(dummy.title)
                .textColor(R.color.light)
                .textSize(14f)
                .build()

            map_container.addMarker(markerView)
        }
    }

    private fun initMapTwo() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)

        map_container.getMapImageView().setImageResource(R.drawable.exhibitor_plan)

        DummyData().data2.forEachIndexed { index, dummy ->
            val marker = Marker(dummy.id,dummy.x * metrics.density,dummy.y * metrics.density)
            val infoView = DefaultInfoLayout(requireContext(), marker, dummy.title, dummy.description)

            val markerView = MarkerView.Builder(requireContext(), marker)
                .background(R.drawable.default_marker)
                .info(infoView)
                .build()

            map_container.addMarker(markerView)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int) =
            EventMapFragment().apply {
                arguments = Bundle().apply { putInt(ARG_TYPE, type) }
            }
    }
}
