package com.sweak.spotifylibraryfeatures.features.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sweak.spotifylibraryfeatures.R
import com.sweak.spotifylibraryfeatures.databinding.ActivityMainBinding
import com.sweak.spotifylibraryfeatures.features.login.LoginActivity
import com.sweak.spotifylibraryfeatures.features.trackfeatures.FeaturesActivity
import com.sweak.spotifylibraryfeatures.util.Preferences
import com.sweak.spotifylibraryfeatures.util.Preferences.Companion.PREFERENCES_ACCESS_TOKEN_KEY
import com.sweak.spotifylibraryfeatures.util.Preferences.Companion.PREFERENCES_EXPIRY_DATE_KEY
import com.sweak.spotifylibraryfeatures.util.Preferences.Companion.PREFERENCES_USER_REGISTERED_KEY
import com.sweak.spotifylibraryfeatures.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val showLoginActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            preferences.putString(
                PREFERENCES_ACCESS_TOKEN_KEY, result.data!!.getStringExtra(
                    PREFERENCES_ACCESS_TOKEN_KEY
                )!!
            )
            preferences.putLong(
                PREFERENCES_EXPIRY_DATE_KEY, result.data!!.getLongExtra(
                    PREFERENCES_EXPIRY_DATE_KEY, 0
                )
            )
            preferences.putBoolean(PREFERENCES_USER_REGISTERED_KEY, true)
        } else {
            showErrorDialog()
        }
    }

    private val tracksAdapter: TracksAdapter = TracksAdapter(
        object : TracksAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                showTrackFeatures(position)
            }
        }
    )

    @Inject
    lateinit var preferences: Preferences
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private fun showErrorDialog() {
        Dialog(this).apply {
            setContentView(R.layout.error_dialog)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            findViewById<TextView>(R.id.text_view_error).text = getString(R.string.unknown_error)
            findViewById<Button>(R.id.button_ok).setOnClickListener {
                finish()
            }
            show()
        }
    }

    private fun showTrackFeatures(position: Int) {
        Intent(this, FeaturesActivity::class.java).apply {
            putExtra(TRACK_ID_KEY, tracksAdapter.getTrackAt(position).id)
            putExtra(TRACK_NAME_KEY, tracksAdapter.getTrackAt(position).name)
            putExtra(TRACK_ARTISTS_KEY, tracksAdapter.getTrackAt(position).artists)
            startActivity(this)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareActionBar()
        prepareRecyclerView()
        showLoginActivityIfNeeded()
        startCollectingFromViewModel()
    }

    private fun prepareActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = getString(R.string.your_library)
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewTracks.apply {
            adapter = tracksAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    @ExperimentalCoroutinesApi
    private fun startCollectingFromViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.savedTracks.collect {
                val tracks = it ?: return@collect

                binding.apply {
                    progressBarLoadingTracks.isVisible =
                        tracks is Resource.Loading && tracks.data.isNullOrEmpty()
                    textViewError.isVisible =
                        tracks is Resource.Error && tracks.data.isNullOrEmpty()
                    textViewError.text = tracks.error?.localizedMessage
                }

                val saveTrackFeatures = launch {
                    if (tracks.data != null)
                        viewModel.saveTrackFeatures(tracks.data)
                }

                saveTrackFeatures.join()
                tracksAdapter.submitList(tracks.data)
            }
        }
    }

    private fun showLoginActivityIfNeeded() {
        if (!preferences.getBoolean(PREFERENCES_USER_REGISTERED_KEY)) {
            val intent = Intent(this, LoginActivity::class.java)
            showLoginActivity.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reload_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refresh -> {
            if (shouldReauthorize())
                showReauthorizeDialog()
            else
                viewModel.onRefresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun shouldReauthorize(): Boolean {
        val currentDate = Calendar.getInstance().time.time
        val expiryDate = preferences.getLong(PREFERENCES_EXPIRY_DATE_KEY)
        return currentDate >= expiryDate
    }

    private fun showReauthorizeDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.reauthorize_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.findViewById<Button>(R.id.button_authorize).setOnClickListener {
            preferences.putBoolean(PREFERENCES_USER_REGISTERED_KEY, false)
            showLoginActivityIfNeeded()
            dialog.dismiss()
            viewModel.onRefresh()
        }

        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    companion object {
        const val TRACK_ID_KEY = "com.sweak.quickspotify.features.main.TRACK_ID_KEY"
        const val TRACK_NAME_KEY = "com.sweak.quickspotify.features.main.TRACK_NAME_KEY"
        const val TRACK_ARTISTS_KEY = "com.sweak.quickspotify.features.main.TRACK_ARTISTS_KEY"
    }
}