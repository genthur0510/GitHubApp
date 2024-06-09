package com.genthur.githubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.genthur.githubapp.database.FavoriteEntity
import com.genthur.githubapp.databinding.ActivityFavoriteBinding
import com.genthur.githubapp.ui.adapter.FavoriteAdapter
import com.genthur.githubapp.ui.viewmodel.FavoriteViewModel
import com.genthur.githubapp.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    private val favViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Favorite User"
        }

        favViewModel.getAllFavorites().observe(this) {
            if (it != null) {
                adapter.setListFavorite(it)
            }
        }

        adapter = FavoriteAdapter()
        binding?.rvListFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvListFavorite?.setHasFixedSize(true)
        binding?.rvListFavorite?.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(favorite: FavoriteEntity, position: Int) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_DETAIL, favorite.username)
                    startActivity(it)
                }
            }

        })
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

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

}