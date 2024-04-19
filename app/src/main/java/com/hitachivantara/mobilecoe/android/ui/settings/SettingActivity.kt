package com.hitachivantara.mobilecoe.android.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.hitachivantara.mobilecoe.android.MainApplication
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.Result
import com.hitachivantara.mobilecoe.android.data.md5
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import com.hitachivantara.mobilecoe.android.databinding.ActivitySettingBinding
import com.hitachivantara.mobilecoe.android.ui.login.LoginActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingViewModel: SettingViewModel
    private val preferencesStore by lazy { PreferencesStore.getInstance(this) }
    private var user: LoggedInUser? = null
    private val userId by lazy { preferencesStore.getLoginUserId() }
    private lateinit var navController: NavController
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        settingViewModel =
            ViewModelProvider(
                this, SettingViewModelFactory(
                    MainApplication().appDatabase.userDao(),
                    preferencesStore, userId ?: ""
                )
            )[SettingViewModel::class.java]
        settingViewModel.updateUser.observe(this) { result ->
            if (result is Result.Success) {
                user = result.data
                Toast.makeText(this, "Update user successfully!!", Toast.LENGTH_SHORT).show()
                binding.edtUsername.text = null
                binding.edtPassword.text = null
            } else if (result is Result.Error) {
                Toast.makeText(
                    this,
                    "Update user failed, error: " + result.exception.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        settingViewModel.getUser(userId ?: "").observe(this) {
            user = it
            binding.textHome.text = String.format(getString(R.string.welcome), it.displayName)
        }
        binding = ActivitySettingBinding.inflate(layoutInflater)

        binding.homeViewModel = settingViewModel

        binding.btnLogout.setOnClickListener {
            settingViewModel.logout()
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Settings"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_left)

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

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
            else -> throw NotImplementedError("not found id")
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}