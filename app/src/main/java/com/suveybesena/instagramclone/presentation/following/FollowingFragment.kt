package com.suveybesena.instagramclone.presentation.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.databinding.FragmentFollowingBinding
import com.suveybesena.instagramclone.model.FollowModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private val viewModel: FollowingViewModel by viewModels()
    private lateinit var followingadapter: FollowingAdapter
    private lateinit var binding : FragmentFollowingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFollowersId()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel._userList.observe(viewLifecycleOwner) { list ->
            feedAdapter(list)
        }
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
        }
        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            binding.followingProgressBar.visibility = if (loading == true) View.VISIBLE else View.GONE
        }


    }
    private fun feedAdapter(list: List<FollowModel>) {
        followingadapter = FollowingAdapter(list)
        binding.followRecyclerView.adapter = followingadapter
    }


}