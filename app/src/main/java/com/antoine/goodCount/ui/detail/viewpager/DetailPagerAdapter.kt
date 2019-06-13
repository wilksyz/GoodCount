package com.antoine.goodCount.ui.detail.viewpager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.detail.viewpager.balance.BalanceFragment
import com.antoine.goodCount.ui.detail.viewpager.spent.SpentFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

private val TAB_TITLES = arrayOf(R.string.tab_title_1, R.string.tab_title_2)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, private val commonPotId: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return when (position) {
            0 -> {
                SpentFragment.newInstance(commonPotId)
            }else -> {
                return BalanceFragment.newInstance(commonPotId)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}