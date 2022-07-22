package com.example.tictactoe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentGameIntroBinding
import com.example.tictactoe.ui.GameViewModel


class GameIntroFragment : Fragment() {
    lateinit var binding: FragmentGameIntroBinding
    private val viewModel: GameViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameIntroBinding.inflate(inflater, container, false)
        return binding.root
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