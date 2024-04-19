package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.hitachivantara.mobilecoe.android.databinding.FragmentCastsBinding
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CastsFragment : Fragment() {

    private var _binding : FragmentCastsBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MoviesViewModel by viewModels ({
        requireActivity()
    })

    private val castAdapter by lazy { CastsAdapter() }

    private var firstCreated = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesViewModel.getMovieCast()


        _binding = FragmentCastsBinding.inflate(inflater, container, false)
        val rvCast = binding.rvCast

        binding.viewModel = moviesViewModel
        rvCast.adapter = castAdapter

        val layoutManager = GridLayoutManager(context, 2)
        rvCast.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            moviesViewModel.castsFlow.collectLatest {
                castAdapter.submitData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(firstCreated){
            moviesViewModel.getMovieCast()
            firstCreated = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = CastsFragment()
    }
}