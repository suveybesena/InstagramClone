package com.suveybesena.instagramclone.presentation.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.suveybesena.instagramclone.R
import kotlinx.android.synthetic.main.fragment_contacts.*


class ContactsFragment : Fragment() {

    lateinit var contactsAdapter: ContactsAdapter
    var list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getContactsPermission()
        setupRecyclerView()
    }


    private fun getContactsPermission() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                Array(1) { Manifest.permission.READ_CONTACTS },
                111
            )
        } else {
            getAllContact()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) getAllContact()
    }

    private fun getAllContact() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor = requireActivity().contentResolver?.query(uri, null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME).toInt()
                )
                list.add(name)
            }


        }
    }


    private fun setupRecyclerView() {
        contactsAdapter = ContactsAdapter(list)

        contactsRecyclerView.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        contactsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("name", it)
                println(it)
            }
            findNavController().navigate(R.id.action_contactsFragment_to_searchFragment, bundle)
        }

    }


}