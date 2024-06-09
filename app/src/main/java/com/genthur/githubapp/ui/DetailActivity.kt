package com.genthur.githubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.genthur.githubapp.R
import com.genthur.githubapp.databinding.ActivityDetailBinding
import com.genthur.githubapp.ui.adapter.SectionsPagerAdapter
import com.genthur.githubapp.ui.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail User"
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val dataDetail = intent.getStringExtra(EXTRA_DETAIL) ?: ""
        sectionsPagerAdapter.username = dataDetail

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (dataDetail.isNotEmpty()) {
            detailViewModel.displayDetail(dataDetail)
        }

        detailViewModel.detailUser.observe(this) { userDetail ->
            userDetail?.let { user ->
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(user.avatarUrl)
                        .into(detailUserImage)
                    tvDetailName.text = user.name
                    tvUsername.text = user.login
                    detailFollowing.text = "${user.following} Following"
                    detailFollowers.text = "${user.followers} Followers"
                }
            }
        }

        detailViewModel.isUserFavorite(dataDetail).observe(this@DetailActivity) { isFavorite ->
            if (isFavorite) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        binding.fabFavorite.setOnClickListener {
            val user = detailViewModel.detailUser.value
            if (user != null) {
                if (detailViewModel.userFavorite) {
                    detailViewModel.deleteFavorite(user.login, user.avatarUrl)
                } else {
                    detailViewModel.addFavorite(user.login, user.avatarUrl)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val dataDetail = intent.getStringExtra(EXTRA_DETAIL) ?: ""
        if (dataDetail.isNotEmpty()) {
            detailViewModel.isUserFavorite(dataDetail).observe(this) { isFavorite->
                detailViewModel.userFavorite = isFavorite
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tabFollowing,
            R.string.tabFollowers
        )
    }
}