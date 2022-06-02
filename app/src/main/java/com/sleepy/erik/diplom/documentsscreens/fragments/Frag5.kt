package com.sleepy.erik.diplom.documentsscreens.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SnapHelper
import androidx.room.Room
import com.sleepy.erik.diplom.adapters.RecyclerAdapter
import com.sleepy.erik.diplom.adapters.ZoomCenterCardLayoutManager
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.databinding.FragmentFrag5Binding


class Frag5 : Fragment() {

    private val mItems = ArrayList<Uri>()
    private lateinit var mAdapter: RecyclerAdapter
    private val pickImage = 100
    private val RECORD_REQUEST_CODE = 101
    private var imageUri: Uri? = null
    private var _binding: FragmentFrag5Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFrag5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permission = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        binding.button.setOnClickListener {
            openGallery()
        }
        mAdapter = RecyclerAdapter(requireContext(), mItems)
        val recycler = binding.imageRecycler
        val manager : RecyclerView.LayoutManager = ZoomCenterCardLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.layoutManager = manager
        recycler.itemAnimator
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(recycler)
        recycler.adapter = mAdapter
        recycler.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildViewHolder(view).adapterPosition
                if (position == 0 || position == state.itemCount - 1) {
                    val elementWidth = 160
                    val elementMargin = 180
                    val padding: Int = Resources.getSystem()
                        .displayMetrics.widthPixels / 2 - elementWidth - elementMargin / 2
                    if (position == 0) {
                        outRect.left = padding
                    } else {
                        outRect.right = padding
                    }
                }
            }
        })
    }
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            addItem(imageUri!!)
        }
    }

    private fun addItem(item: Uri) {
        mItems.add(item)
        mAdapter.notifyDataSetChanged()
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("error", "Permission has been denied by user")
                } else {
                    Log.d("error", "Permission has been granted by user")
                }
            }
        }
    }
}