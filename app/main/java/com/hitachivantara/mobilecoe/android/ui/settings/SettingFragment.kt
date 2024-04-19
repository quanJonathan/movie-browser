package com.hitachivantara.mobilecoe.android.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hitachivantara.mobilecoe.android.MainApplication
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.Result
import com.hitachivantara.mobilecoe.android.data.md5
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import com.hitachivantara.mobilecoe.android.databinding.FragmentSettingBinding
import com.hitachivantara.mobilecoe.android.ui.login.LoginActivity

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var settingViewModel: SettingViewModel
    private val preferencesStore by lazy { PreferencesStore.getInstance(requireContext()) }
    private var user: LoggedInUser? = null
    private val userId by lazy { preferencesStore.getLoginUserId() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingViewModel =
            ViewModelProvider(
                this, SettingViewModelFactory(
                    (context?.applicationContext as MainApplication).appDatabase.userDao(),
                    preferencesStore, userId ?: ""
                )
            ).get(SettingViewModel::class.java)
        settingViewModel.updateUser.observe(viewLifecycleOwner) { result ->
            if (result is Result.Success) {
                user = result.data
                Toast.makeText(context, "Update user successfully!!", Toast.LENGTH_SHORT).show()
                binding.edtUsername.setText(null)
                binding.edtPassword.setText(null)
            } else if (result is Result.Error) {
                Toast.makeText(
                    context,
                    "Update user failed, error: " + result.exception.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        settingViewModel.getUser(userId ?: "").observe(viewLifecycleOwner) {
            user = it
        }
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.homeViewModel = settingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            settingViewModel.logout()
            startActivity(
                Intent(
                    activity,
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        val actionBar = (activity as AppCompatActivity).supportActionBar

        actionBar?.setHomeAsUpIndicator(R.drawable.arrow_left)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnUpdate.setOnClickListener {
            val username = binding.edtUsername.text?.toString() ?: ""
            val password = binding.edtPassword.text?.toString() ?: ""
            settingViewModel.updateUser(
                user?.copy(displayName = username, password = password.md5()) ?: LoggedInUser(
                    preferencesStore.getLoginUserId() ?: "",
                    username,
                    password
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingFragment()
    }
}