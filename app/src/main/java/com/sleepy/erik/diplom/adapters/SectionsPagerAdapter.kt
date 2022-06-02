package com.sleepy.erik.diplom.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sleepy.erik.diplom.R
import com.sleepy.erik.diplom.mainscreen.main.FragmentInfo
import com.sleepy.erik.diplom.mainscreen.main.FragmentMain
import com.sleepy.erik.diplom.mainscreen.main.FragmentMainAlternative
import com.sleepy.erik.diplom.mainscreen.main.FragmentProfile

private val TAB_TITLES = arrayOf(
    R.string.tab_text_main,
    R.string.tab_text_profile,
    R.string.tab_text_info
)


class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, private val key: Boolean) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        lateinit var fragment: Fragment
        if (!key){
            when (position) {
                0 -> fragment = FragmentMain()
                1 -> fragment = FragmentProfile()
                2 -> fragment = FragmentInfo()
            }
        } else {
            when (position) {
                0 -> fragment = FragmentMainAlternative()
                1 -> fragment = FragmentProfile()
                2 -> fragment = FragmentInfo()
            }
        }

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}