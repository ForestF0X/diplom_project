package com.sleepy.erik.diplom.mainscreen

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sleepy.erik.diplom.R
import com.sleepy.erik.diplom.adapters.SectionsPagerAdapter
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.databinding.ActivityHomeBinding
import com.sleepy.erik.diplom.startactivities.RegActivity
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity() {


    var db: UserDao? = null
    var dataBase: UserRoomDatabase? = null
    private var sharedpreferences: SharedPreferences? = null
    private val MyPREFERENCES = "myprefs"
    private lateinit var user: String
    private lateinit var binding: ActivityHomeBinding
    private var key by Delegates.notNull<Int>()
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val intent = intent
        key = intent.getIntExtra("docFinish", 0)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.statusBarColor = this.resources.getColor(R.color.background_dark)
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        user = sharedpreferences?.getString("user", "")!!
        dataBase = Room.databaseBuilder(this, UserRoomDatabase::class.java, "user_database")
            .allowMainThreadQueries()
            .build()
        db = dataBase!!.userDao()
        val userData = db!!.getUserData(user)
        dialog = Dialog(this)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, /*key*/userData!!.documentsSend)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val text = tab!!.text
                if (text != "Главная") {
                    binding.title.text = text
                } else {
                    binding.title.text = "ЕЭТК"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        if (key == 1){
            dialog.setContentView(R.layout.dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val okBtn = dialog.findViewById<Button>(R.id.okBtn)
            val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
            okBtn.setOnClickListener {
                dialog.dismiss()
            }
            cancelBtn.setOnClickListener {
                dialog.dismiss()
                super.onBackPressed()
            }
            dialog.show()
        }
    }
}