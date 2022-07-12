package com.example.tictactoe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentGameEndBinding
import com.example.tictactoe.models.GameViewModel


class GameEndFragment : Fragment() {
    private lateinit var binding: FragmentGameEndBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_end, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGameEndBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.winnerText.text = arguments?.getString("message")
//        viewModel.reset()
    }
}