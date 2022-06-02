package com.sleepy.erik.diplom.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sleepy.erik.diplom.documentsscreens.fragments.*

class DocumentsPagerAdapter (private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        lateinit var fragment: Fragment
        when (position) {
            0 -> fragment = Frag1()
            1 -> fragment = Frag2()
            2 -> fragment = Frag3()
            3 -> fragment = Frag4()
            4 -> fragment = Frag5()
        }
        return fragment
    }

    override fun getCount(): Int {
        return 5
    }
}