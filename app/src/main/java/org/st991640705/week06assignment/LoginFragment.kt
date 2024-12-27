package org.st991640705.week06assignment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import org.st991640705.week06assignment.databinding.FragmentLoginBinding

/**
 * This [Fragment] allows users to log in by entering their credentials and navigating to the
 * appropriate screens upon successful login.
 *
 * @Author Frank Ekwomadu. C
 */
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up click listeners
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdtText.text.toString()
            val password = binding.passEdtText.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    requireActivity(),
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Successfully Logged In", Toast.LENGTH_LONG).show()
                            // Navigate to the home fragment
                            findNavController().navigate(R.id.action_LoginFragment_to_homeFragment)
                        } else {
                            Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
        }

        binding.signupBtn.setOnClickListener {
            // Navigate to the signup fragment
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.resetPassTv.setOnClickListener {
            // Navigate to the forgot password fragment
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
