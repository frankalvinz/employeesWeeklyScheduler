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
import org.st991640705.week06assignment.databinding.FragmentSignupBinding

/**
 * This [Fragment] enables new users to register by providing their details, with validation to
 * ensure all fields are completed before submission
 *
 * @Author Frank Ekwomadu. C
 */
class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using view binding
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Set up click listeners
        binding.signupBtn.setOnClickListener {
            val email: String = binding.emailEdtText.text.toString()
            val password: String = binding.passEdtText.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Successfully Registered", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                        } else {
                            Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}