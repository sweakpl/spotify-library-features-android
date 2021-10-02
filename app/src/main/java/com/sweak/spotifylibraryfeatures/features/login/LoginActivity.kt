package com.sweak.spotifylibraryfeatures.features.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.sweak.spotifylibraryfeatures.R
import com.sweak.spotifylibraryfeatures.databinding.ActivityLoginBinding
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val showLoginActivityCode = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val authorizationResponse = AuthorizationClient.getResponse(result.resultCode, result.data)

        when (authorizationResponse.type) {
            AuthorizationResponse.Type.CODE ->
                showLoginActivityToken.launch(
                    authorizationFlow.getLoginActivityTokenIntent(
                        authorizationResponse.code
                    )
                )
            AuthorizationResponse.Type.ERROR -> {
                showErrorDialog(authorizationResponse.error)
            }
            else ->
                showErrorDialog("Unknown error")
        }
    }

    private val showLoginActivityToken = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val authorizationResponse = AuthorizationClient.getResponse(result.resultCode, result.data)

        when (authorizationResponse.type) {
            AuthorizationResponse.Type.TOKEN -> {
                val data = Intent()
                val token = authorizationResponse.accessToken
                val expiryDate = parseIntoExpiryDate(authorizationResponse.expiresIn)

                data.putExtra(ACCESS_TOKEN_KEY, token)
                data.putExtra(EXPIRY_DATE_KEY, expiryDate)
                setResult(RESULT_OK, data)
                finish()
            }
            AuthorizationResponse.Type.ERROR -> {
                showErrorDialog(authorizationResponse.error)
            }
            else ->
                showErrorDialog(getString(R.string.unknown_error))
        }
    }

    private lateinit var binding: ActivityLoginBinding
    private val authorizationFlow = AuthorizationFlow(this)

    private fun parseIntoExpiryDate(expiresIn: Int): Long {
        val expiryDate = Calendar.getInstance()

        expiryDate.add(Calendar.SECOND, expiresIn)

        return expiryDate.time.time
    }

    private fun showErrorDialog(errorMessage: String) {
        Dialog(this).apply {
            setContentView(R.layout.error_dialog)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            findViewById<TextView>(R.id.text_view_error).text = errorMessage
            findViewById<Button>(R.id.button_ok).setOnClickListener {
                finishAffinity()
            }
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            buttonLogin.setOnClickListener {
                buttonLogin.isVisible = false
                progressBarLogin.isVisible = true
                showLoginActivityCode.launch(authorizationFlow.getLoginActivityCodeIntent())
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (isFinishing)
            finishAffinity()
    }

    companion object {
        const val ACCESS_TOKEN_KEY = "accessToken"
        const val EXPIRY_DATE_KEY = "expiryDate"
    }
}