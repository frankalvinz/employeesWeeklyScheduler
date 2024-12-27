package org.st991640705.week06assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.st991640705.week06assignment.adapter.RvContactsAdapter
import org.st991640705.week06assignment.databinding.FragmentHomeBinding
import org.st991640705.week06assignment.model.Contacts

/**
 * This [Fragment] displays all contacts, integrating functionality to fetch, display, and manage
 * contact information effectively.
 *
 * @Author Frank Ekwomadu. C
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsList: ArrayList<Contacts>
    private lateinit var filteredContactsList: ArrayList<Contacts>
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var rvAdapter: RvContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize the FloatingActionButton
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        // Initialize FirebaseDatabase reference
        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        contactsList = arrayListOf()
        filteredContactsList = arrayListOf()

        // Set up RecyclerView
        binding.rvContacts.setHasFixedSize(true)
        binding.rvContacts.layoutManager = LinearLayoutManager(this.context)

        // Initialize the adapter with the full list of contacts
        rvAdapter = RvContactsAdapter(filteredContactsList)
        binding.rvContacts.adapter = rvAdapter

        // Fetch data from Firebase
        fetchData()

        // Set up SearchView query listener
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText.orEmpty())
                return true
            }
        })

        return binding.root
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactsList.clear()
                if (snapshot.exists()) {
                    for (contactSnap in snapshot.children) {
                        val contact = contactSnap.getValue(Contacts::class.java)
                        if (contact != null) {
                            contactsList.add(contact)
                        }
                    }
                }
                // Initially display all contacts
                filteredContactsList.clear()
                filteredContactsList.addAll(contactsList)
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterContacts(query: String) {
        filteredContactsList.clear()
        if (query.isEmpty()) {
            filteredContactsList.addAll(contactsList) // Show all contacts if query is empty
        } else {
            // Filter the contacts based on the query (name or work days)
            for (contact in contactsList) {
                if (contact.name?.contains(query, ignoreCase = true) == true || contact.workSchedule?.contains(query, ignoreCase = true) == true) {
                    filteredContactsList.add(contact)
                }
            }
        }
        rvAdapter.notifyDataSetChanged()
    }
}
