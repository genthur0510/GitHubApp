package com.genthur.githubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.genthur.githubapp.data.response.ItemsItem
import com.genthur.githubapp.databinding.FragmentFollowBinding
import com.genthur.githubapp.ui.adapter.UserAdapter
import com.genthur.githubapp.ui.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private var adapter = UserAdapter()
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)

        val position = arguments?.getInt(ARG_POSITION)
        username = arguments?.getString(ARG_USERNAME) ?: ""


        if (position == 1){
            followViewModel.displayFollowing(username)
            followViewModel.listFollowing.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        } else {
            followViewModel.displayFollowers(username)
            followViewModel.listFollowers.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        binding.rvListFollow.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListFollow.adapter = adapter

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ItemsItem) {

            }
        })
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
