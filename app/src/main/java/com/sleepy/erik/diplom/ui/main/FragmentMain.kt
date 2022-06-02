package com.sleepy.erik.diplom.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sleepy.erik.diplom.databinding.FragmentMainBinding
import com.sleepy.erik.diplom.documentsscreens.DocInfoActivity


class FragmentMain : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var alertDialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        alertDialog = AlertDialog.Builder(requireContext())
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.regButton.setOnClickListener {
            val intent = Intent(activity, DocInfoActivity::class.java)
            requireActivity().startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}