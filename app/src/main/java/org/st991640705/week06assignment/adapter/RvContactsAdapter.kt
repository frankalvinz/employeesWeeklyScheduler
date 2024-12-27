package org.st991640705.week06assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import org.st991640705.week06assignment.HomeFragmentDirections
import org.st991640705.week06assignment.databinding.RvContactsItemBinding
import org.st991640705.week06assignment.model.Contacts

/**
 * This adapter facilitates the binding of contact data to the RecyclerView, managing the
 * display and interaction of contact items in the user interface.
 */
class RvContactsAdapter(private var contactList: ArrayList<Contacts>) : RecyclerView.Adapter<RvContactsAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(val binding: RvContactsItemBinding) : RecyclerView.ViewHolder(binding.root)

    // onCreateViewHolder to inflate item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvContactsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // onBindViewHolder to bind data to each item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contactList[position]
        holder.apply {
            binding.apply {
                tvNameItem.text = currentItem.name
                tvPhoneItem.text = currentItem.phoneNumber
                tvWorkSchedule.text = currentItem.workSchedule
                Picasso.get().load(currentItem.imgUri).into(imgItem)

                // Handle item click to navigate to UpdateFragment
                rvContainer.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(
                        currentItem.id.toString(),
                        currentItem.name.toString(),
                        currentItem.phoneNumber.toString(),
                        currentItem.workSchedule.toString(),
                        currentItem.imgUri.toString()
                    )
                    findNavController(holder.itemView).navigate(action)
                }

                // Handle long click for delete action
                rvContainer.setOnLongClickListener {
                    showDeleteConfirmationDialog(holder.itemView.context, currentItem)
                    true
                }
            }
        }
    }

    // getItemCount to return the number of items in the list
    override fun getItemCount(): Int {
        return contactList.size
    }

    // Method to update the contacts list with filtered results
    fun updateContacts(newContactList: List<Contacts>) {
        contactList.clear()
        contactList.addAll(newContactList)
        notifyDataSetChanged()
    }

    // Delete contact logic
    private fun showDeleteConfirmationDialog(context: Context, contact: Contacts) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete ${contact.name}?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteContact(contact, context)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // Method to delete contact from Firebase
    private fun deleteContact(contact: Contacts, context: Context) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("contacts/${contact.id}")
        databaseReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Delete from Firebase Storage if needed
                contact.imgUri?.let { deleteFromFirebaseStorage(it) }
                Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to delete image from Firebase Storage
    private fun deleteFromFirebaseStorage(imageUri: String) {
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri)
        storageReference.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Successfully deleted the image
            } else {
                // Handle any errors
            }
        }
    }
}
