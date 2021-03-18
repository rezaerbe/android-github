package com.erbe.navigationsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

/**
 * Shows a question and four answers.
 */
class InGame : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_in_game, container, false)

        val gameOverListener: (View) -> Unit = {
            Navigation.findNavController(view).navigate(R.id.action_in_game_to_gameOver)
        }

        view.findViewById<View>(R.id.checkBox).setOnClickListener(gameOverListener)
        view.findViewById<View>(R.id.checkBox2).setOnClickListener(gameOverListener)
        view.findViewById<View>(R.id.checkBox4).setOnClickListener(gameOverListener)

        view.findViewById<View>(R.id.checkBox3).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_in_game_to_resultsWinner)
        }

        return view
    }
}