package com.suveybesena.instagramclone.presentation.followers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.databinding.FragmentFollowersBinding
import com.suveybesena.instagramclone.model.FollowersModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : Fragment() {
    private val viewModel: FollowerViewModel by viewModels()
    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var binding: FragmentFollowersBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFollowersId()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
        }
        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            //
        }

        viewModel._followerState.observe(viewLifecycleOwner) { list ->
            adapter(list)
        }

    }

    private fun adapter(list: List<FollowersModel>) {
        followersAdapter = FollowersAdapter(list)
        binding.followersRecyclerView.adapter = followersAdapter
    }


}