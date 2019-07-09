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
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, commonPotId: String) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var mSpentFragment: SpentFragment = SpentFragment.newInstance(commonPotId)
    private var mBalanceFragment: BalanceFragment = BalanceFragment.newInstance(commonPotId)

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return when (position) {
            0 -> {
                mSpentFragment
            }else -> {
                return mBalanceFragment
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