package com.sleepy.erik.diplom.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.sleepy.erik.diplom.databinding.FragmentMainAlternativeBinding

class FragmentMainAlternative : Fragment() {
    private var _binding: FragmentMainAlternativeBinding? = null
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
        _binding = FragmentMainAlternativeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}