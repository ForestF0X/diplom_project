package com.sleepy.erik.diplom.documentsscreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.sleepy.erik.diplom.R
import com.sleepy.erik.diplom.databinding.ActivityDocInfoBinding

class DocInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.background_dark)
        supportActionBar?.hide()
        binding.toDocBtn.setOnClickListener {
            val intent = Intent(this, DocumentsActivity::class.java)
            startActivity(intent)
            super.finish()
        }
    }
}