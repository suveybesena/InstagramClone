package com.suveybesena.instagramclone.presentation.edit


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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentEditBinding
import kotlinx.android.synthetic.main.fragment_edit.*


class EditFragment : Fragment() {
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    private lateinit var contentResolver: ContentResolver
    var pickedImage: Uri? = null
    var bitmap: Bitmap? = null

    private lateinit var binding: FragmentEditBinding

    private val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentResolver = requireActivity().contentResolver
        initList()
        getInstance()
    }

    private fun getInstance() {
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    private fun initList() {

        binding.apply {
            btwCloseEdit.setOnClickListener {
                closeEdit()
            }
            btwSaveEdit.setOnClickListener {
                saveEdit()
            }
            iwProfile.setOnClickListener {
                getProfileImage()
            }
        }

    }

    private fun saveEdit() {

        val name = binding.edtName.text.toString()
        val surname = binding.edtSurname.text.toString()
        val bio = binding.edtBio.text.toString()
        val webSite = binding.edtWebsite.text.toString()

        pickedImage?.let { viewModel.saveEdit(name, surname, bio, webSite, it) }
        viewModel._loadingState.observe(viewLifecycleOwner) { loading ->
            edit_progress_bar.visibility = if (loading == true) View.VISIBLE else View.GONE
        }
        viewModel._errorState.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG)
                .show()
        }
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_editFragment_to_profileFragment)
        }

    }

    private fun closeEdit() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_editFragment_to_profileFragment)

        }
    }

    private fun getProfileImage() {
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
                    binding.iwProfile.setImageBitmap(bitmap)

                } else {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage)
                    binding.iwProfile.setImageBitmap(bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}