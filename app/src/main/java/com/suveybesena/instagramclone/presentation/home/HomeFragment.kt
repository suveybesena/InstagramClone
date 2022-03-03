package com.suveybesena.instagramclone.presentation.home

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.suveybesena.instagramclone.presentation.likes.LikesInterface
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentHomeBinding
import com.suveybesena.instagramclone.model.Firebase
import com.suveybesena.instagramclone.presentation.likes.LikesViewModel
import com.suveybesena.instagramclone.presentation.likes.UnlikeInterface
import kotlinx.android.synthetic.main.dialog_view.*
import kotlinx.android.synthetic.main.feed_recycler_row.*
import kotlinx.android.synthetic.main.top_bar.*


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var feed: FeedAdapter
    private val auth= FirebaseAuth.getInstance()
    private lateinit var binding : FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        initList()
    }

    private fun feedAdapter(userListFirebase: List<Firebase>) {
        feed = FeedAdapter(userListFirebase,
            object : LikesInterface {
            override fun likeItemClick(image: String) {
                LikesViewModel().getLikesImage( image)
            }
        },object : FeedLikesInterface{
            override fun getLikes(image: String, like :ImageView, unlike :ImageView) {
               viewModel.checkLikeStatus(image, like, unlike)
                viewModel._likeState.observe(viewLifecycleOwner){ likeStatus->
                    if (likeStatus == true){
                        imw_unLike.visibility = View.VISIBLE
                        imw_like.visibility = View.INVISIBLE
                    }else{
                        imw_like.visibility = View.VISIBLE
                        imw_unLike.visibility = View.INVISIBLE
                    }

                }
            }
        } ,
        object :UnlikeInterface{
            override fun unlikeItemClick(image: String) {
                LikesViewModel().removeLike(image)
            }

        })
        binding.feedRecycler.adapter = feed

    }

    private fun observeLiveData() {

        viewModel.readUserData()
        viewModel._firebaseList.observe(viewLifecycleOwner){arrayListFirebase ->
            feedAdapter(arrayListFirebase)
        }
        viewModel._errorState.observe(viewLifecycleOwner){ error->
            if (!error.isNullOrEmpty()) {
                Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
            }
        }
        viewModel._loadingState.observe(viewLifecycleOwner){loading ->
            binding.homeProgressBar.visibility = if (loading == true) View.VISIBLE else View.GONE
        }
    }

    private fun initList() {
        imw_options.setOnClickListener {
            options()
        }
    }

    private fun options() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_view)
        dialog.setCancelable(false)
        dialog.dialog_yes.setOnClickListener {
            auth.signOut()
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment)
            dialog.dismiss()
        }
        dialog.dialog_no.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


}