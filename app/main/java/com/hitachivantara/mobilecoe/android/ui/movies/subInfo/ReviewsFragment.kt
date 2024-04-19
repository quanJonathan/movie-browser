package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.hitachivantara.mobilecoe.android.databinding.FragmentReviewsBinding
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModel
import kotlinx.coroutines.launch


class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val moviesViewModel: MoviesViewModel by viewModels( {
        requireActivity()
    })

    private var firstCreated = true

    private val reviewsAdapter by lazy { ReviewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        binding.viewModel = moviesViewModel

        binding.reviewRv.adapter = reviewsAdapter
        binding.reviewRv.layoutManager = LinearLayoutManager(context)
        moviesViewModel.getMovieReview()
        binding.executePendingBindings()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(firstCreated){
            moviesViewModel.getMovieReview()
            firstCreated = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            moviesViewModel.reviewsFlow.collect {
                Log.d("reviews", it.toString())
                reviewsAdapter.submitData(it)
            }
        }

        reviewsAdapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && reviewsAdapter.itemCount == 0
            binding.reviewRv.visibility = if (isListEmpty) View.GONE else View.VISIBLE
            binding.tvNoReviews.visibility = if (isListEmpty) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ReviewsFragment()
    }
}