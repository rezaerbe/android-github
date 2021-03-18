package com.erbe.background

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.erbe.background.databinding.ActivityFilterBinding
import com.erbe.background.lib.Constants
import com.erbe.background.lib.ImageOperations

/** The [android.app.Activity] where the user picks filters to be applied on an image. */
class FilterActivity : AppCompatActivity() {

    private val viewModel: FilterViewModel by viewModels()
    private var outputImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityFilterBinding.inflate(layoutInflater).run {
            setContentView(root)
            bindViews(this)
            // Check to see if we have output.
            viewModel.outputStatus.observe(this@FilterActivity) { listOfInfos ->
                onStateChange(listOfInfos, this)
            }
        }
    }

    private fun bindViews(binding: ActivityFilterBinding) {
        with(binding) {
            val imageUri: Uri = Uri.parse(intent.getStringExtra(Constants.KEY_IMAGE_URI))
            Glide.with(this@FilterActivity).load(imageUri).into(imageView)

            // Only show output options if a Imgur client id is set.
            val multipleDestinationsPossible = Constants.IMGUR_CLIENT_ID.isNotEmpty()
            if (!multipleDestinationsPossible) {
                destinationsGroup.visibility = View.GONE
            }

            apply.setOnClickListener {
                val applyWaterColor = filterWatercolor.isChecked
                val applyGrayScale = filterGrayscale.isChecked
                val applyBlur = filterBlur.isChecked
                val save = save.isChecked

                val imageOperations = ImageOperations(
                        applicationContext, imageUri,
                        applyWaterColor, applyGrayScale, applyBlur,
                        save
                )

                viewModel.apply(imageOperations)
            }

            output.setOnClickListener {
                if (outputImageUri != null) {
                    val viewOutput = Intent(Intent.ACTION_VIEW, outputImageUri)
                    if (viewOutput.resolveActivity(packageManager) != null) {
                        startActivity(viewOutput)
                    }
                }
            }
            cancel.setOnClickListener { viewModel.cancel() }
        }
    }

    private fun onStateChange(listOfInfos: List<WorkInfo>, binding: ActivityFilterBinding) {
        if (listOfInfos == null || listOfInfos.isEmpty()) {
            return
        }
        
        val info = listOfInfos[0]
        val finished = info.state.isFinished

        with(binding) {
            if (!finished) {
                progressBar.visibility = View.VISIBLE
                cancel.visibility = View.VISIBLE
                apply.visibility = View.GONE
                output.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                cancel.visibility = View.GONE
                apply.visibility = View.VISIBLE
            }
        }
        val outputData = info.outputData
        outputData.getString(Constants.KEY_IMAGE_URI)?.let {
            outputImageUri = Uri.parse(it)
            binding.output.visibility = View.VISIBLE
        }
    }

    companion object {

        /**
         * Creates a new intent which can be used to start [FilterActivity].
         *
         * @param context the application [Context].
         * @param imageUri the input image [Uri].
         * @return the instance of [Intent].
         */
        internal fun newIntent(context: Context, imageUri: Uri) =
                Intent(context, FilterActivity::class.java).putExtra(
                        Constants.KEY_IMAGE_URI, imageUri.toString()
                )
    }
}
