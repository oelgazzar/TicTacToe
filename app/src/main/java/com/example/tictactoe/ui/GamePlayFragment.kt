package com.example.tictactoe.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentGamePlayBinding

class GamePlayFragment : Fragment(R.layout.fragment_game_play), BoardView.OnMoveListener {
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentGamePlayBinding
    private var _effectsPlayer: MediaPlayer? = null
    private val effectsPlayer: MediaPlayer get() = _effectsPlayer!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGamePlayBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.gamePlayFragment = this

        binding.boardView.onMoveListener = this
        viewModel.isGameRunning.observe(viewLifecycleOwner) { isGameRunning ->
            if (!isGameRunning) onGameEnded()
        }

        _effectsPlayer = MediaPlayer.create(requireContext(), R.raw.move)
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        effectsPlayer.release()
        _effectsPlayer = null
    }

    fun resetGame() {
        viewModel.reset()
        binding.boardView.invalidate()
    }

    fun quit() {
        resetGame()
        findNavController().navigate(R.id.action_gamePlayFragment_to_gameIntroFragment)
    }

    private fun onGameEnded() {
        val message = when (viewModel.winner) {
            0 -> "Tie"
            1 -> "Player1 won"
            else -> "${viewModel.mode} won"
        }
        val bundle = Bundle()
        bundle.putString("message", message)
        findNavController().navigate(R.id.action_gamePlayFragment_to_gameEndFragment, bundle)
    }

    override fun onMove(row: Int, col: Int) {
        viewModel.updateMatrix(row, col)
        if (effectsPlayer.isPlaying) {
            effectsPlayer.seekTo(0)
        } else {
            effectsPlayer.start()
        }
    }
}