package com.sleepy.erik.diplom.mainscreen.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.room.Room
import com.sleepy.erik.diplom.applicationclass.UsersApplication
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.data.User
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.databinding.FragmentProfileBinding
import com.sleepy.erik.diplom.documentsscreens.DocInfoActivity
import com.sleepy.erik.diplom.startactivities.LoginActivity
import com.sleepy.erik.diplom.viewmodel.UserViewModel
import com.sleepy.erik.diplom.viewmodel.UserViewModelFactory


class FragmentProfile : Fragment() {

    var db: UserDao? = null
    var dataBase: UserRoomDatabase? = null
    private var sharedpreferences: SharedPreferences? = null
    private val MyPREFERENCES = "myprefs"
    private var _binding: FragmentProfileBinding? = null
    private lateinit var user: String
    private lateinit var userData: User
    private val binding get() = _binding!!
    private lateinit var alertDialog: AlertDialog.Builder

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as UsersApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(requireContext())
        sharedpreferences = this.activity?.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        user = sharedpreferences?.getString("user", "")!!
        dataBase = Room.databaseBuilder(requireContext(), UserRoomDatabase::class.java, "user_database")
            .allowMainThreadQueries()
            .build()
        db = dataBase!!.userDao()
        userData = db!!.getUserData(user)!!
        val userName = userData.firstName
        val userSurname = userData.lastName
        binding.nameTextView.text = userName.toString()
        binding.surnameTextView.text = userSurname.toString()
        binding.emailTextView.text = user
        binding.resetMailBtn.setOnClickListener {
            alertDialog.apply {
                setTitle("Уведомление")
                setMessage("На вашу почту отправлено письмо для изменения почты")
                setPositiveButton("Спасибо") { _, _ ->
                }
            }.create().show()
        }
        binding.resetPassBtn.setOnClickListener {
            alertDialog.apply {
                setTitle("Уведомление")
                setMessage("На вашу почту отправлено письмо для изменения пароля")
                setPositiveButton("Спасибо") { _, _ ->
                }
            }.create().show()
        }
        binding.exitBtn.setOnClickListener {
            val userDataUpdate = User(
                userData.email, userData.firstName, userData.lastName, userData.login, userData.password,
                userData.documentsSend,
                signedIn = false
            )
            userViewModel.update(userDataUpdate)
            val intent = Intent(activity, LoginActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}