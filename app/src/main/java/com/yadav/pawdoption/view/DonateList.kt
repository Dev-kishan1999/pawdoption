package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.DonateAdapter
import com.yadav.pawdoption.adapter.PetListAdapter
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.persistence.SheltersDAO


class DonateList : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var donateAdapter: DonateAdapter
    private val sheltersDAO = SheltersDAO()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_donate_list, container, false)

        activity?.title = "Shelters"

        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        donateAdapter = DonateAdapter(requireContext(), arrayListOf())
        linearLayoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.donateRecycler)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = donateAdapter
        sheltersDAO.getShelters().observe(viewLifecycleOwner
        ) {
            var keys = it.keys
            var shelters: ArrayList<Shelter> = arrayListOf()

            for(key in keys){
                shelters.add(it.get(key)!!)
            }

            donateAdapter = DonateAdapter(requireContext(), shelters)
            recyclerView.adapter = donateAdapter
            donateAdapter.notifyDataSetChanged()
        }
    }
}