package com.suveybesena.instagramclone.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentProfileBinding
import com.suveybesena.instagramclone.model.FollowModel
import com.suveybesena.instagramclone.model.MyPostsModel


class ProfileFragment : Fragment() {

    lateinit var list: List<FollowModel>
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var myPostsAdapter: MyPostsAdapter
    private lateinit var binding: FragmentProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDataFromFirebase()
        initList()
        observeLiveData()
    }

    private fun adapter(list: List<MyPostsModel>) {
        myPostsAdapter = MyPostsAdapter(list)
        binding.myPostsRecycler.apply {
            adapter = myPostsAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

    }

    private fun observeLiveData() {
        val userName = binding.twUserName
        val imageView = binding.imwProfile

        viewModel._nameState.observe(viewLifecycleOwner) { name ->
            userName.setText(name)
        }
        viewModel._imageState.observe(viewLifecycleOwner) { image ->
            Glide.with(this).load(image)
                .into(imageView)
        }
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
        }
        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            //profile_progress_bar.visibility = if (loading!= null) View.VISIBLE else View.GONE
        }

        viewModel._myPostList.observe(viewLifecycleOwner) { list ->
            binding.postCount.text = list.size.toString()
            adapter(list)
        }


    }

    private fun initList() {
        binding.btEditProfile.setOnClickListener {
            onclickEditProfile()
        }
        binding.layoutFollowing.setOnClickListener {
            following()
        }
        binding.layoutFollowers.setOnClickListener {
            followers()
        }
    }

    private fun followers() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_followersFragment)
        }
    }

    private fun following() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_followingFragment)
        }
    }

    private fun onclickEditProfile() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_editFragment)
        }
    }

}

