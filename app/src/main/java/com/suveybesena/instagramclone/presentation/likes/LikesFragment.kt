package com.suveybesena.instagramclone.presentation.likes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.databinding.FragmentLikesBinding
import com.suveybesena.instagramclone.model.LikesModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_likes.*

@AndroidEntryPoint
class LikesFragment : Fragment() {

    private val viewModel: LikesViewModel by viewModels()
    private lateinit var likesAdapter: LikesAdapter
    private lateinit var binding: FragmentLikesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadImage()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel._likesList.observe(viewLifecycleOwner) { list ->
            if (list != null) adapterInit(list)
        }
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
            }
        }
        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            binding.likesProgressBar.visibility = if (loading == true) View.VISIBLE else View.GONE

        }
    }

    private fun adapterInit(list: List<LikesModel>) {
        likesAdapter = LikesAdapter(list)
        binding.likesRecyclerView.adapter = likesAdapter

        val swipe = object : SwipeToDeleteCallBack() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                binding.likesRecyclerView.adapter?.notifyItemRemoved(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(likes_recyclerView)


    }


}