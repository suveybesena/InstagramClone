package com.suveybesena.instagramclone.presentation.map

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.databinding.FragmentGetUserLocationBinding
import kotlinx.android.synthetic.main.fragment_get_user_location.*
import java.util.*

class GetUserLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding : FragmentGetUserLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding =FragmentGetUserLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener { latLng ->
            mapClickListener(latLng)
        }
    }

    private fun mapClickListener(location: LatLng) {
        mMap.clear()
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var address = ""
        try {
            val adressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (adressList.get(0).subAdminArea != null) {
                address += adressList.get(0).subAdminArea
                if (adressList.get(0).adminArea != null) {
                    address += adressList.get(0).adminArea
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.twLocation.text = address
        binding.bwCheck.setOnClickListener {
            val action =
                GetUserLocationFragmentDirections.actionGetUserLocationFragmentToShareFragment()
            action.location = address
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

}