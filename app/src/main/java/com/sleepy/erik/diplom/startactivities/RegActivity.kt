package com.sleepy.erik.diplom.startactivities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sleepy.erik.diplom.mainscreen.HomeActivity
import com.sleepy.erik.diplom.databinding.ActivityRegBinding

class RegActivity : AppCompatActivity() {
    private var binding: ActivityRegBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        val view = binding?.root
        supportActionBar?.hide()
        binding!!.toLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            super.finish()
        }
        binding!!.regBtn.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]{2,3}+"
            when (binding!!.emailText.text.isNotEmpty() && binding!!.passText.text.isNotEmpty()
                    && binding!!.loginText.text.isNotEmpty() && binding!!.nameText.text.isNotEmpty()
                    && binding!!.surnameText.text.isNotEmpty()){
                true -> {
                    when (binding!!.emailText.text.matches(emailPattern.toRegex())){
                        true -> {
                            when (binding!!.passText.text.length >= 6){
                                true -> {
                                    if (isOnline(this)){
                                        val replyIntent = Intent()
                                        val email = binding!!.emailText.text.toString()
                                        val login = binding!!.loginText.text.toString()
                                        val name = binding!!.nameText.text.toString()
                                        val surname = binding!!.surnameText.text.toString()
                                        val password = binding!!.passText.text.toString()
                                        replyIntent.putExtra(EXTRA_EMAIL, email)
                                        replyIntent.putExtra(EXTRA_LOGIN, login)
                                        replyIntent.putExtra(EXTRA_NAME, name)
                                        replyIntent.putExtra(EXTRA_SURNAME, surname)
                                        replyIntent.putExtra(EXTRA_PASSWORD, password)
                                        setResult(Activity.RESULT_OK, replyIntent)
                                        super.finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Отстуствует интернет-соединение",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                false -> {
                                    if (!isOnline(this)){
                                        Toast.makeText(
                                            this,
                                            "Отсутствует интернет-соединение",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }else{
                                        Toast.makeText(
                                            this,
                                            "Пароль должен быть длиннее 6 символов",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                        false -> {
                            Toast.makeText(
                                this,
                                "Некорректный адрес электронной почты",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                false -> {
                    Toast.makeText(
                        this,
                        "Пожалуйста, заполните все поля",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setContentView(view)
    }

    fun isOnline(context: Context): Boolean {
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
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val EXTRA_EMAIL = "email"
        const val EXTRA_LOGIN = "login"
        const val EXTRA_NAME = "name"
        const val EXTRA_SURNAME = "surname"
        const val EXTRA_PASSWORD = "password"
    }
}