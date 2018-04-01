package com.yoshizuka.bicloo.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yoshizuka.bicloo.BR
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.databinding.FragmentStationBinding
import com.yoshizuka.bicloo.databinding.FragmentStationBottomSheetBinding


import com.yoshizuka.bicloo.fragments.StationFragment.OnStationFragmentListener
import com.yoshizuka.bicloo.models.entities.Station

import kotlinx.android.synthetic.main.fragment_station.view.*

/**
 * [RecyclerView.Adapter] that can display a [Station] and makes a call to the
 * specified [OnStationFragmentListener].
 */
class StationAdapter(
        private var mStations: List<Station>,
        private val mListener: OnStationFragmentListener?)
    : RecyclerView.Adapter<StationAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    private lateinit var mBinding: FragmentStationBinding

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Station
            mListener?.onStationClick(item)
        }
    }

    /**
     * Met à jour la liste des stations
     * @param stations La liste des stations
     */
    fun setStations(stations: List<Station>) {
        mStations = stations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_station, parent, false)

        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mStations[position]
        holder.bind(item)

        with(holder.mBinding.root) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mStations.size

    inner class ViewHolder(val mBinding: FragmentStationBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(station: Station) {
            mBinding.station = station
            mBinding.executePendingBindings()
        }
    }
}
