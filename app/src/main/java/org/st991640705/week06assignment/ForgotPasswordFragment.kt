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
import org.st991640705.week06assignment.databinding.FragmentForgotPasswordBinding

/**
 * This [Fragment] helps with password recovery, guiding users through the process of resetting
 * their passwords via email.
 *
 * @Author Frank Ekwomadu. C
 */
class ForgotPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using view binding
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.resetPassBtn.setOnClickListener {
            val email = binding.emailEdtText.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_LONG).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Reset email sent", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_forgotPasswordFragment_to_updatePasswordFragment)
                        } else {
                            Toast.makeText(requireContext(), "Error sending reset email", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp() // Navigate back when back button is pressed
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}