package com.sleepy.erik.diplom.startactivities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sleepy.erik.diplom.applicationclass.UsersApplication
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.data.User
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.databinding.ActivityLoginBinding
import com.sleepy.erik.diplom.mainscreen.HomeActivity
import com.sleepy.erik.diplom.viewmodel.UserViewModel
import com.sleepy.erik.diplom.viewmodel.UserViewModelFactory

class LoginActivity : AppCompatActivity() {

    var db: UserDao? = null
    var dataBase: UserRoomDatabase? = null
    var sharedpreferences: SharedPreferences? = null
    val MyPREFERENCES = "myprefs"


    private val newUserActivityRequestCode = 1
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UsersApplication).repository)
    }

    private var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        dataBase = Room.databaseBuilder(this, UserRoomDatabase::class.java, "user_database")
            .allowMainThreadQueries()
            .build()
        db = dataBase!!.userDao()
        userViewModel.delete("admin@admin.com")
        val view = binding?.root
        supportActionBar?.hide()
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]{2,3}+"
        binding!!.loginBtn.setOnClickListener {
            when (binding!!.emailText.text.isNotEmpty()) {
                true -> {
                    when (binding!!.emailText.text.matches(emailPattern.toRegex())) {
                        true -> {
                            when (binding!!.passText.text.isNotEmpty()) {
                                true -> {
                                    val user = db!!.getUser(
                                        binding!!.emailText.text.toString(),
                                        binding!!.passText.text.toString()
                                    )
                                    if (user != null) {
                                        if (isOnline(this)){
                                            val editor: SharedPreferences.Editor? = sharedpreferences?.edit()
                                            editor!!.putString("user", binding!!.emailText.text.toString())
                                            editor.apply()
                                            editor.commit()
                                            val userDataUpdate = User(
                                                user.email, user.firstName, user.lastName, user.login, user.password,
                                                user.documentsSend,
                                                signedIn = true
                                            )
                                            userViewModel.update(userDataUpdate)
                                            val intent = Intent(this, HomeActivity::class.java)
                                            startActivity(intent)
                                            super.finish()
                                        } else {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Отсутствует интернет-соединение",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        if (!isOnline(this)){
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Отсутствует интернет-соединение",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }else{
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Не зарегистрированный пользователь или некорректные данные",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                false -> {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Введите пароль",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        false -> {
                            when (binding!!.passText.text.isNotEmpty()) {
                                true -> {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Некорректный адрес электронной почты",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                false -> {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Введите пароль",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
                false -> {
                    when (binding!!.passText.text.isNotEmpty()) {
                        true -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Введите электронную почту",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        false -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Введите пароль и электронную почту",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        binding!!.toRegBtn.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivityForResult(intent, newUserActivityRequestCode)
        }
        setContentView(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newUserActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val email = intentData?.getStringExtra(RegActivity.EXTRA_EMAIL)
            val login = intentData?.getStringExtra(RegActivity.EXTRA_LOGIN)
            val name = intentData?.getStringExtra(RegActivity.EXTRA_NAME)
            val surname = intentData?.getStringExtra(RegActivity.EXTRA_SURNAME)
            val password = intentData?.getStringExtra(RegActivity.EXTRA_PASSWORD)
            val user = User(email!!, name!!, surname!!, login!!, password!!,
                documentsSend = false,
                signedIn = false
            )
            userViewModel.insert(user)
        } else {
            Log.d("error", "string empty, not saved")
        }
    }
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
            }
        }
        return false
    }
}