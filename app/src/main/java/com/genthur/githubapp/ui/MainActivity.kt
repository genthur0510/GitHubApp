package com.genthur.githubapp.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.genthur.githubapp.R
import com.genthur.githubapp.helper.SettingPreferences
import com.genthur.githubapp.data.response.ItemsItem
import com.genthur.githubapp.helper.dataStore
import com.genthur.githubapp.databinding.ActivityMainBinding
import com.genthur.githubapp.ui.adapter.UserAdapter
import com.genthur.githubapp.ui.viewmodel.MainViewModel
import com.genthur.githubapp.ui.viewmodel.ThemeViewModel
import com.genthur.githubapp.helper.ThemeViewModelFactory
import com.google.android.material.search.SearchBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var searchBar: SearchBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoadingSplashScreen.value
            }
        }

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.listUser.observe(this) { users ->
            setUserData(users)
        }


        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { v, actionId, event ->
                val searchText = searchView.text.toString()
                searchBar.setText(searchText)
                mainViewModel.findUser(searchText)
                searchView.hide()
                searchView.clearFocus()
                false
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvListUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)

        themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(SettingPreferences.getInstance(application.dataStore))).get(ThemeViewModel::class.java)
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchBar = binding.searchBar
        searchBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.theme_change) {
                Intent(this@MainActivity, ThemeActivity::class.java).also {
                    startActivity(it)
                }
            } else if (menuItem.itemId == R.id.favorite_user) {
                Intent(this@MainActivity, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            true
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(user: ItemsItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_DETAIL, user.login)
                    startActivity(it)
                }
            }
        })
        binding.rvListUser.adapter = adapter
        adapter.submitList(users)
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (binding.searchView.editText!!.text.isNotEmpty()) {
            binding.searchView.editText!!.text.clear() // Clear text
            binding.searchView.clearFocus() // Clear focus
            mainViewModel.displayUserLists() // Refetch initial user list
        } else {
            super.onBackPressed()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}