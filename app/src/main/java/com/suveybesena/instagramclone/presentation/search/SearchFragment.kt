package com.suveybesena.instagramclone.presentation.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.databinding.FragmentSearchBinding
import com.suveybesena.instagramclone.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_friends_item.*
import kotlinx.android.synthetic.main.add_friends_item.view.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var friendsAdapter: FriendsAdapter? = null
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        initListeners()
    }

    private fun feedAdapter(userList: ArrayList<User>) {
        friendsAdapter = FriendsAdapter(userList, object : FollowInterface {
            override fun itemOnClick(userId: String) {
                viewModel.follow(userId)
                bw_follow.visibility = View.GONE
                bw_unfollow.visibility = View.VISIBLE

            }
        }, object : UnfollowInterface {
            override fun onItemClick(userId: String) {
                viewModel.unFollow(userId)
                bw_follow.visibility = View.VISIBLE
                bw_unfollow.visibility = View.GONE

            }
        }, object : GetUserInterface {
            override fun getUserList(userId: String, follow: Button, unfollow: Button) {
                viewModel.checkFollowingStatus(userId, follow, unfollow)
                viewModel._followState.observe(viewLifecycleOwner) { followStatus ->
                    if (followStatus != true) {
                        follow.visibility = View.VISIBLE
                        unfollow.visibility = View.GONE
                    } else {
                        unfollow.visibility = View.VISIBLE
                        follow.visibility = View.GONE
                    }
                }


            }

        })

        binding.searchRecyclerView.adapter = friendsAdapter
    }

    private fun initListeners() {
        binding.apply {
            edtSearch.doAfterTextChanged { text ->
                viewModel.retrieveUsers(text.toString())
                binding.searchRecyclerView.visibility = View.VISIBLE
            }
            iwClose.setOnClickListener {
                clearRecycler()
            }
        }
    }

    private fun clearRecycler() {
        binding.apply {
            searchRecyclerView.visibility = View.INVISIBLE
            edtSearch.text.clear()
        }

    }

    private fun observeLiveData() {
        viewModel._userList.observe(viewLifecycleOwner) { arrayListUser ->
            arrayListUser?.let {
                feedAdapter(arrayListUser)
            }
        }
        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            binding.pbSearchScreen.visibility = if (loading == true) View.VISIBLE else View.GONE
        }
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            error?.let { errorMessage ->
                Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }

    }


}