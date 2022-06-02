package com.sleepy.erik.diplom.documentsscreens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.viewpager.widget.ViewPager
import com.sleepy.erik.diplom.R
import com.sleepy.erik.diplom.adapters.DocumentsPagerAdapter
import com.sleepy.erik.diplom.applicationclass.UsersApplication
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.data.User
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.databinding.ActivityDocumentsBinding
import com.sleepy.erik.diplom.mainscreen.HomeActivity
import com.sleepy.erik.diplom.viewmodel.UserViewModel
import com.sleepy.erik.diplom.viewmodel.UserViewModelFactory

class DocumentsActivity : AppCompatActivity() {

    var db: UserDao? = null
    var dataBase: UserRoomDatabase? = null
    private var sharedpreferences: SharedPreferences? = null
    private val MyPREFERENCES = "myprefs"
    private lateinit var user: String
    private lateinit var binding: ActivityDocumentsBinding
    private lateinit var viewPager: ViewPager

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UsersApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        user = sharedpreferences?.getString("user", "")!!
        dataBase = Room.databaseBuilder(this, UserRoomDatabase::class.java, "user_database")
            .allowMainThreadQueries()
            .build()
        db = dataBase!!.userDao()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.background_dark)
        supportActionBar?.hide()
        val indicator = binding.dotsIndicator
        viewPager = binding.documentsViewPager
        val adapter = DocumentsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
        binding.toNext.setOnClickListener {
            when (viewPager.currentItem) {
                4 -> {
                    val userData = db!!.getUserData(user)
                    val intent = Intent(this, HomeActivity::class.java)
                    val user = User(userData!!.email, userData.firstName, userData.lastName, userData.login, userData.password,
                        documentsSend = true,
                        signedIn = true
                    )
                    userViewModel.update(user)
                    startActivity(intent)
                }
                3 -> {
                    binding.toNext.setTextColor(resources.getColor(R.color.white))
                    binding.toNext.setBackgroundResource(R.drawable.button_red)
                    binding.toNext.text = "Отправить"
                    viewPager.currentItem = viewPager.currentItem + 1
                }
                else -> {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
        }
        binding.toPrevious.setOnClickListener {
            when (viewPager.currentItem) {
                4 -> {
                    binding.toNext.setTextColor(resources.getColor(R.color.red_light))
                    binding.toNext.setBackgroundResource(R.drawable.border_button)
                    binding.toNext.text = "Далее"
                    viewPager.currentItem = viewPager.currentItem - 1
                }
                0 -> {
                    super.onBackPressed()
                }
                else -> {
                    viewPager.currentItem = viewPager.currentItem - 1
                }
            }
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position != 0){
                    binding.toPrevious.text = "Назад"
                } else {
                    binding.toPrevious.text = "Отмена"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    override fun onPause() {
        super.onPause()
        onSaveInstanceState(Bundle())
    }

    override fun onStart() {
        super.onStart()
        onRestoreInstanceState(Bundle())
    }
    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}