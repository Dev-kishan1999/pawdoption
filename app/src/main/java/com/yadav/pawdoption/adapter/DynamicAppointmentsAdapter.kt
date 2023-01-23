package com.yadav.pawdoption.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.content.Context;
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yadav.pawdoption.view.DayAppointment


class DynamicAppointmentsAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentStatePagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        val b = Bundle()
        b.putInt("position", position)
        val fragment = DayAppointment()
        fragment.arguments = b
        return fragment
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}