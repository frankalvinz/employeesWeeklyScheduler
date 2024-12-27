package org.st991640705.week06assignment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.st991640705.week06assignment.databinding.FragmentAddBinding
import org.st991640705.week06assignment.model.Contacts

/**
 * This [Fragment] fragment allows users to add new contacts, providing a simple form for entering
 * contact details and saving them to the database.
 *
 * @Author Frank Ekwomadu. C
 */
class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private  val binding get() = _binding!!

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater , container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        // process of "Send Data" button click
        binding.btnSend.setOnClickListener {
            // adds data to DB
            saveData()
            // and navigate back to HomeFragment
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }

        // pick selected image from registerForActivityResult
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.imgAdd.setImageURI(it)
            if (it != null){
                uri = it
            }
        }

        // process of "Pick image" button click
        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        return binding.root
    }


    // collect data from the console adn save to DB
    private fun saveData() {
        val name = binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val workSchedule = binding.edtWorkSchedule.text.toString()

        // name phone, and schedule can't be blanks
        if (name.isEmpty()) binding.edtName.error = "write a name"
        if (phone.isEmpty()) binding.edtPhone.error = "write a phone number"
        if (workSchedule.isEmpty()) binding.edtWorkSchedule.error = "write a schedule"

        // push() function generates a unique key for each new child in firebase
        val contactId = firebaseRef.push().key!!

        var contacts : Contacts

        // store an image via StorageReference
        uri?.let{
            storageRef.child(contactId).putFile(it)
                .addOnSuccessListener { task->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            Toast.makeText(context, " Image stored successfully", Toast.LENGTH_SHORT).show()
                            val imgUrl = url.toString()
                            // create new Contact object
                            contacts = Contacts(contactId, name , phone , workSchedule, imgUrl)

                            // add new data contacts for new document
                            firebaseRef.child(contactId).setValue(contacts)
                                .addOnCompleteListener{
                                    Toast.makeText(context, " data stored successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{error ->
                                    Toast.makeText(context, "error ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { url ->
                            Toast.makeText(context, " Image Failed", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
}