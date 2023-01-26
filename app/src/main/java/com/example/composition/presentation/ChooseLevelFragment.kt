package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentChooseLevelBinding
import com.example.composition.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    companion object {
        const val NAME = "choose_level_fragment"
        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }

            buttonEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }

            buttonNormal.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }

            buttonHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        val args = Bundle().apply {
            putParcelable(GameFragment.ARG_LEVEL, level)
        }
        findNavController().navigate(R.id.action_chooseLevelFragment2_to_gameFragment2, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}