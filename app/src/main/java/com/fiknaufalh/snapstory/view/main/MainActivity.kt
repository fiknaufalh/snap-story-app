package com.fiknaufalh.snapstory.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import com.fiknaufalh.snapstory.R
import com.fiknaufalh.snapstory.adapters.LoadingStateAdapter
import com.fiknaufalh.snapstory.adapters.StoryAdapter
import com.fiknaufalh.snapstory.data.remote.responses.StoryResponse
import com.fiknaufalh.snapstory.databinding.ActivityMainBinding
import com.fiknaufalh.snapstory.utils.ViewModelFactory
import com.fiknaufalh.snapstory.view.detail.DetailActivity
import com.fiknaufalh.snapstory.view.maps.MapsActivity
import com.fiknaufalh.snapstory.view.upload.UploadActivity
import com.fiknaufalh.snapstory.view.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this, "story")
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setErrorView(false)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        getData()

        binding.fabUpload.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        binding.ivMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        observeErrorView()

        viewModel.isLoading.observe(this) {
            isLoading -> showLoading(isLoading)
        }

        viewModel.errorToast.observe(this) {
            errorToast -> errorToast?.let {
                if (errorToast) {
                    Toast.makeText(this, "Success to retrieve the data", Toast.LENGTH_SHORT).show()
                    viewModel.resetToast()
                } else {
                    Toast.makeText(this, "Failed to retrieve the data", Toast.LENGTH_SHORT).show()
                    viewModel.resetToast()
                    setErrorView(true)
                }
            }
        }

        binding.btnRetry.setOnClickListener {
            setErrorView(false)
            viewModel.stories.observe(this) {
                getData()
            }
        }

        setupView()
        setupAction()
    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.ivLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setErrorView(isError: Boolean) {
        val isShow = if (isError) View.VISIBLE else View.GONE
        binding.ivError.visibility = isShow
        binding.tvError.visibility = isShow
        binding.btnRetry.visibility = isShow
        binding.rvStory.visibility = if (isError) View.GONE else View.VISIBLE
    }

    private fun observeErrorView() {
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main
        )

        viewModel.stories.observe(this) { stories ->
            differ.submitData(lifecycle, stories)
        }

        differ.addLoadStateListener { loadState ->
            if (differ.snapshot().isEmpty()) {
                setErrorView(true)
            } else {
                setErrorView(false)
            }
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}