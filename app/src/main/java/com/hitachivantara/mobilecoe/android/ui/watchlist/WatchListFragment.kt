package com.hitachivantara.mobilecoe.android.ui.watchlist

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hitachivantara.mobilecoe.android.Injection
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.databinding.FragmentWatchListBinding
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModel
import com.hitachivantara.mobilecoe.android.ui.utils.ItemClickListener
import com.hitachivantara.mobilecoe.android.ui.utils.getActionBar

class WatchListFragment : Fragment(), ItemClickListener<Movie> {

    private var _binding : FragmentWatchListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieViewModel: MoviesViewModel
    private val watchListAdapter by lazy { WatchListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentWatchListBinding.inflate(inflater, container, false)

        movieViewModel =
            ViewModelProvider(this, Injection.provideMovieViewModelFactory(requireContext(), this))[MoviesViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext())

        movieViewModel.allWatchList.observe(this.viewLifecycleOwner) {
            watchListAdapter.updateData(it)
            if (it.isNotEmpty()) binding.noResultsGroup.visibility = View.GONE
        }

        binding.rvWatchlist.adapter = watchListAdapter
        binding.rvWatchlist.layoutManager = layoutManager
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        watchListAdapter.setClickListener(this)

        val actionBar =  activity.getActionBar()
        val customView = actionBar?.customView
        val title = customView?.findViewById<AppCompatTextView>(R.id.title_actionBar)
        title?.text = getString(R.string.title_watchlist)
        title?.typeface = Typeface.create(null, 400, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: Movie) {
        val action = WatchListFragmentDirections.actionNavigationWatchListToNavigationMovieDetail(item.id)
        findNavController().navigate(action)
    }
}