package com.fiknaufalh.snapstory.view.detail

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.fiknaufalh.snapstory.R
import com.fiknaufalh.snapstory.databinding.ActivityDetailBinding
import org.w3c.dom.Text

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val name = intent.getStringExtra(getString(R.string.passing_name))
        val description = intent.getStringExtra(getString(R.string.passing_desc))
        val imageUrl = intent.getStringExtra(getString(R.string.passing_image))

        with(binding) {
            storyUser.text = name
            storyDesc.text = description
            Glide.with(this@DetailActivity)
                .load(imageUrl)
                .into(storyImage)
            Log.d("DetailActivity", "storyUser: ${storyUser.text}, " +
                    "storyDesc: ${storyDesc.text}, storyImage: ${storyImage.toString()}")
        }
    }
}