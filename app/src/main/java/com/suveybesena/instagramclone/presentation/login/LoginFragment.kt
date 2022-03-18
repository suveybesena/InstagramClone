package com.suveybesena.instagramclone.presentation.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment(), TextWatcher {

    private var mAuth = FirebaseAuth.getInstance()
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeners()

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            view?.let {
                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    private fun listeners() {

        binding.btwLogin.isEnabled = false
        binding.edtLoginMail.addTextChangedListener(this)
        binding.edtLoginPassword.addTextChangedListener(this)

        btw_login.setOnClickListener {
            login()
        }
        tw_sign_up.setOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    fun login() {
        val email = binding.edtLoginMail.text.toString()
        val password = binding.edtLoginPassword.text.toString()
        viewModel.login(email, password)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_loginFragment_to_homeFragment)
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
        }

        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            binding.loginProgressBar.visibility = if (loading == true) View.VISIBLE else View.GONE
        }

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        binding.btwLogin.isEnabled =
            binding.edtLoginMail.text.toString()
                .isNotEmpty() && binding.edtLoginPassword.text.toString()
                .isNotEmpty()
    }


}