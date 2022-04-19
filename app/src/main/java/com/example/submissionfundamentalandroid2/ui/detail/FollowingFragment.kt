package com.example.submissionfundamentalandroid2.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionfundamentalandroid2.ui.component.FollowersAdapter
import com.example.submissionfundamentalandroid2.databinding.FragmentFollowingBinding
import com.example.submissionfundamentalandroid2.data.remote.response.UserData

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        val dataUser = activity?.intent?.getParcelableExtra<UserData>(DATA_USER) as UserData

        viewModel.getFollowing(dataUser.login)
        viewModel.following.observe(viewLifecycleOwner, Observer { followers ->
            setUserData(followers)
        })

        viewModel.followingLoading.observe(viewLifecycleOwner, Observer {
            showLoading(it)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUserData(users: List<UserData>) {
        if(users.isNotEmpty()){
            binding.rvFollowing.visibility = View.VISIBLE
            binding.emptyDataMessage.visibility = View.INVISIBLE

            binding.rvFollowing.layoutManager = LinearLayoutManager(activity)

            val followersAdapter = FollowersAdapter(users)
            binding.rvFollowing.adapter = followersAdapter

            followersAdapter.setOnItemClickCallback(object : FollowersAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserData) {
                    showSelectedUser(data)
                }
            })
        }else{
            binding.rvFollowing.visibility = View.INVISIBLE
            binding.emptyDataMessage.visibility = View.VISIBLE
        }
    }

    private fun showSelectedUser(data: UserData) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.DATA_USER, data)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val DATA_USER = "data_user"
    }
}