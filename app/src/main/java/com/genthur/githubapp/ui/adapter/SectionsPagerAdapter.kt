package com.genthur.githubapp.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.genthur.githubapp.ui.DetailActivity
import com.genthur.githubapp.ui.FollowFragment

class SectionsPagerAdapter(activity: DetailActivity): FragmentStateAdapter(activity) {
    var username: String = ""
    var followingCount: Int = 0
    var followersCount: Int = 0
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putString(FollowFragment.ARG_USERNAME, username)
            putInt(FollowFragment.ARG_POSITION, position + 1)
        }
        return fragment
    }
}