package com.yoshizuka.bicloo.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.databinding.FragmentStationBottomSheetBinding
import com.yoshizuka.bicloo.models.entities.Station

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    StationBottomSheetFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 */
class StationBottomSheetFragment : BottomSheetDialogFragment() {

    /**
     * Station à afficher
     */
    var mStation: Station? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bottomSheetFragment: FragmentStationBottomSheetBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.fragment_station_bottom_sheet, container, false)
        bottomSheetFragment.station = mStation
        return bottomSheetFragment.root
    }
}
