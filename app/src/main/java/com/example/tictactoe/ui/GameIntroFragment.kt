package com.example.tictactoe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentGameIntroBinding
import com.example.tictactoe.databinding.FragmentGamePlayBinding
import com.example.tictactoe.models.GameViewModel


class GameIntroFragment : Fragment() {
    private lateinit var binding: FragmentGameIntroBinding
    private val viewModel: GameViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_intro, container, false)
        binding = FragmentGameIntroBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.playerVsAiButton.setOnClickListener {
            viewModel.mode = "AI"
            startGame()
        }
        binding.playerVsPlayerButton.setOnClickListener {
            viewModel.mode = "Player"
            startGame()
        }
    }

    private fun startGame() {
        findNavController().navigate(R.id.action_gameIntroFragment_to_gamePlayFragment)
    }
}