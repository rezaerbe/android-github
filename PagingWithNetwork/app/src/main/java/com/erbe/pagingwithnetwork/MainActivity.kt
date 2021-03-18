package com.erbe.pagingwithnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erbe.pagingwithnetwork.databinding.ActivityMainBinding
import com.erbe.pagingwithnetwork.lib.reddit.repository.RedditPostRepository
import com.erbe.pagingwithnetwork.reddit.ui.RedditActivity

/**
 * chooser activity for the demo.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.withDatabase.setOnClickListener {
            show(RedditPostRepository.Type.DB)
        }
        binding.networkOnly.setOnClickListener {
            show(RedditPostRepository.Type.IN_MEMORY_BY_ITEM)
        }
        binding.networkOnlyWithPageKeys.setOnClickListener {
            show(RedditPostRepository.Type.IN_MEMORY_BY_PAGE)
        }
    }

    private fun show(type: RedditPostRepository.Type) {
        val intent = RedditActivity.intentFor(this, type)
        startActivity(intent)
    }
}
