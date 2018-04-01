package com.yoshizuka.bicloo.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.adapters.StationAdapter

import com.yoshizuka.bicloo.models.entities.Station

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [StationFragment.OnStationFragmentListener] interface.
 */
class StationFragment : Fragment() {

    private var listener: OnStationFragmentListener? = null

    var items: List<Station> = ArrayList()
        set(value) {
            field = value
            mAdapter?.setStations(value)
        }

    private var mAdapter: StationAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_station_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = StationAdapter(items, listener)
            }
            mAdapter = view.adapter as StationAdapter?
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnStationFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnStationFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnStationFragmentListener {
        fun onStationClick(station: Station?)
    }

}
