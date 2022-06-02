package com.sleepy.erik.diplom.startactivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.room.Room
import com.sleepy.erik.diplom.applicationclass.UsersApplication
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.data.User
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.databinding.ActivityStartBinding
import com.sleepy.erik.diplom.mainscreen.HomeActivity
import com.sleepy.erik.diplom.viewmodel.UserViewModel
import com.sleepy.erik.diplom.viewmodel.UserViewModelFactory

class StartActivity : AppCompatActivity() {
    var db: UserDao? = null
    var dataBase: UserRoomDatabase? = null
    private var sharedpreferences: SharedPreferences? = null
    private val MyPREFERENCES = "myprefs"
    private lateinit var user: String
    private var binding: ActivityStartBinding? = null

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UsersApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        user = sharedpreferences?.getString("user", "")!!
        dataBase = Room.databaseBuilder(this, UserRoomDatabase::class.java, "user_database")
            .allowMainThreadQueries()
            .build()
        db = dataBase!!.userDao()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        val userBased = User("admin@admin.com", "admin", "admin", "admin", "admin",
            documentsSend = false,
            signedIn = false
        )
        userViewModel.insert(userBased)
        when (user == ""){
            true -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    launch()
                    super.finish()
                }, 2500)
            }
            else -> {
                val userData = db!!.getUserData(user)
                if (userData!!.signedIn){
                    Handler(Looper.getMainLooper()).postDelayed({
                        launchSignedIn()
                        super.finish()
                    }, 2500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        launch()
                        super.finish()
                    }, 2500)
                }
            }
        }

    }
    private fun launch(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        super.finish()
    }
    private fun launchSignedIn(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        super.finish()
    }
}