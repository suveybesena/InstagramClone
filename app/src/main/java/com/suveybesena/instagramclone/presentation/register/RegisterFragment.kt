package com.suveybesena.instagramclone.presentation.register

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var contentResolver: ContentResolver
    var pickedImage: Uri? = null
    var bitmap: Bitmap? = null

    private lateinit var binding: FragmentRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentResolver = requireActivity().contentResolver
        initListeners()


    }

    private fun initListeners() {
        binding.btwRegister.setOnClickListener {
            register()
        }
        binding.imwRegister.setOnClickListener {
            imwPickedImage()
        }
        binding.twSignIn.setOnClickListener {
            view?.let { view ->
                Navigation.findNavController(view)
                    .navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }


    private fun register() {
        val register_mail = binding.edtRegisterMail.text.toString()
        val register_password = binding.edtRegisterPassword.toString()
        val username = binding.edtRegisterUsername.text.toString()
        val name = binding.edtRegisterName.text.toString()
        pickedImage?.let { image ->
            view?.let { view ->
                viewModel.register(
                    register_mail, register_password,
                    image, view, username, name
                )
            }
        }
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
        }

        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            register_progress_bar.visibility = if (loading == true) View.VISIBLE else View.GONE
        }

        viewModel._authState.observe(viewLifecycleOwner) { auth ->
            if (auth != false) {
                view?.let { view ->
                    Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_homeFragment)
                }
            }
        }


    }

    private fun imwPickedImage() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            pickedImage = data.data

            if (pickedImage != null) {

                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, pickedImage!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    imw_register.setImageBitmap(bitmap)

                } else {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage)
                    imw_register.setImageBitmap(bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}