package com.example.tictactoe.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentGamePlayBinding
import com.example.tictactoe.models.GameViewModel

class GamePlayFragment : Fragment(R.layout.fragment_game_play), BoardView.OnGameEndedListener {
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentGamePlayBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGamePlayBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.gamePlayFragment = this
        binding.boardView.onGameEndedListener = this
    }

    fun resetGame() {
        viewModel.reset()
        binding.boardView.invalidate()
    }

    override fun onGameEnded() {
        Toast.makeText(requireActivity(), "player${viewModel.winner} won", Toast.LENGTH_SHORT).show()
    }
}