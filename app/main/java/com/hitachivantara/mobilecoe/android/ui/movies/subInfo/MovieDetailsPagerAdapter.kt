package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MovieDetailsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MoreAboutFragment.newInstance()
            1 -> ReviewsFragment.newInstance()
            2 -> CastsFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}