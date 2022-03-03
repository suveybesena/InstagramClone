package com.suveybesena.instagramclone.presentation.share

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentShareBinding


class ShareFragment() : Fragment() {

    private lateinit var contentResolver: ContentResolver
    private val viewModel: ShareViewModel by viewModels()
    private lateinit var binding: FragmentShareBinding

    var pickedImage: Uri? = null
    var bitmap: Bitmap? = null
    private val args: ShareFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentResolver = requireActivity().contentResolver

        initList()

    }

    private fun savePost() {
        if (!args.location.isNullOrEmpty()) {
            val location = args.location
            val comment = binding.edtComment.toString()
            pickedImage?.let { pickedImage ->
                if (location != null) {
                    viewModel.imageDownloader(
                        pickedImage,
                        comment,
                        location
                    )
                }

            }
            Navigation.findNavController(requireView())
                .navigate(R.id.action_shareFragment_to_homeFragment)
        }

        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (error != null) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show()
        }

        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            binding.shareProgressBar.visibility = if (loading == true) View.VISIBLE else View.GONE
        }

    }

    private fun initList() {
        if (!args.location.isNullOrEmpty()) {
            binding.twGetLocation.text = args.location
        }

        binding.apply {
            imwShare.setOnClickListener {
                imwPickedImage()
            }
            btwSavePost.setOnClickListener {
                savePost()
            }
            btwClosePost.setOnClickListener {
                closePost()
            }
            twGetLocation.setOnClickListener {
                getLocation()
            }
        }

    }

    private fun getLocation() {
        view?.let { Navigation.findNavController(it).navigate(R.id.getUserLocationFragment) }
    }

    private fun closePost() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_shareFragment_to_homeFragment)
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
                    binding.imwShare.setImageBitmap(bitmap)

                } else {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage)
                    binding.imwShare.setImageBitmap(bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}