package org.st991640705.week06assignment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import org.st991640705.week06assignment.databinding.FragmentUpdatePasswordBinding

/**
 * This [Fragment] provides functionality for users to update their passwords, ensuring a secure
 * and straightforward process for maintaining account security
 *
 * @Author Frank Ekwomadu. C
 */
class UpdatePasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using view binding
        _binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()  // Navigate back
        }

        binding.resetPassBtn.setOnClickListener {
            val password: String = binding.passwordEdtText.text.toString()
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please enter password", Toast.LENGTH_LONG).show()
            } else {
                auth.currentUser?.updatePassword(password)
                    ?.addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_updatePasswordFragment_to_loginFragment) // Navigate back to LoginFragment after successful update
                        } else {
                            Toast.makeText(requireContext(), "Password not changed", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}