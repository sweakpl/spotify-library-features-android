package com.sweak.spotifylibraryfeatures.features.trackfeatures

import android.animation.ObjectAnimator
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sweak.spotifylibraryfeatures.R
import com.sweak.spotifylibraryfeatures.databinding.ActivityFeaturesBinding
import com.sweak.spotifylibraryfeatures.features.main.MainActivity.Companion.TRACK_ARTISTS_KEY
import com.sweak.spotifylibraryfeatures.features.main.MainActivity.Companion.TRACK_ID_KEY
import com.sweak.spotifylibraryfeatures.features.main.MainActivity.Companion.TRACK_NAME_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlin.math.roundToInt

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FeaturesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeaturesBinding
    private val viewModel: FeaturesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareActionBar()
        prepareViewModel()
    }

    private fun prepareActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title =
            getString(
                R.string.song_and_artists,
                intent.getStringExtra(TRACK_NAME_KEY),
                intent.getStringExtra(TRACK_ARTISTS_KEY)
            )
    }

    private fun prepareViewModel() {
        viewModel.initializeTrackFeatures(intent.getStringExtra(TRACK_ID_KEY))

        lifecycleScope.launchWhenStarted {
            viewModel.trackFeatures.collect { trackFeatures ->
                with(binding) {
                    textViewFeatureTempo.text =
                        getString(R.string.tempo_value, trackFeatures[0].tempo.roundToInt())
                    textViewFeatureLoudness.text =
                        getString(R.string.loudness_value, trackFeatures[0].loudness.roundToInt())

                    fillProgressBar(
                        progressBarLiveness,
                        (trackFeatures[0].liveness * 100).roundToInt()
                    )
                    textViewPercentageLiveness.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].liveness * 100).roundToInt()
                        )
                    fillProgressBar(
                        progressBarAcousticness,
                        (trackFeatures[0].acousticness * 100).roundToInt()
                    )
                    textViewPercentageAcousticness.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].acousticness * 100).roundToInt()
                        )
                    fillProgressBar(
                        progressBarDanceability,
                        (trackFeatures[0].danceability * 100).roundToInt()
                    )
                    textViewPercentageDanceability.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].danceability * 100).roundToInt()
                        )
                    fillProgressBar(
                        progressBarInstrumentalness,
                        (trackFeatures[0].instrumentalness * 100).roundToInt()
                    )
                    textViewPercentageInstrumentalness.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].instrumentalness * 100).roundToInt()
                        )
                    fillProgressBar(
                        progressBarSpeechiness,
                        (trackFeatures[0].speechiness * 100).roundToInt()
                    )
                    textViewPercentageSpeechiness.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].speechiness * 100).roundToInt()
                        )
                    fillProgressBar(progressBarEnergy, (trackFeatures[0].energy * 100).roundToInt())
                    textViewPercentageEnergy.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].energy * 100).roundToInt()
                        )
                    fillProgressBar(
                        progressBarValence,
                        (trackFeatures[0].valence * 100).roundToInt()
                    )
                    textViewPercentageValence.text =
                        getString(
                            R.string.percentage_value,
                            (trackFeatures[0].valence * 100).roundToInt()
                        )
                }
            }
        }
    }

    private fun fillProgressBar(progressBar: ProgressBar, progressMax: Int) {
        val objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", progressMax)
        objectAnimator.duration = 1000
        objectAnimator.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.features_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.features_info -> {
            showFeaturesInfoDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showFeaturesInfoDialog() {
        Dialog(this).apply {
            setContentView(R.layout.features_info_dialog)
            findViewById<TextView>(R.id.text_view_source).movementMethod =
                LinkMovementMethod.getInstance()
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            show()
        }
    }
}