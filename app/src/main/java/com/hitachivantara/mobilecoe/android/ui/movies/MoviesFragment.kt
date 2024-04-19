package com.hitachivantara.mobilecoe.android.ui.movies

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.hitachivantara.mobilecoe.android.Injection
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.databinding.FragmentMoviesBinding
import com.hitachivantara.mobilecoe.android.ui.utils.ItemClickListener
import com.hitachivantara.mobilecoe.android.ui.utils.getActionBar
import com.hitachivantara.mobilecoe.android.ui.utils.hideLoading
import com.hitachivantara.mobilecoe.android.ui.utils.showLoading
import kotlinx.coroutines.launch

class MoviesFragment : Fragment(), ItemClickListener<Movie> {
    private var isFirstCreated = true

    private var _binding: FragmentMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var moviesViewModel: MoviesViewModel

    private val baseMoviesAdapter by lazy { MoviesAdapter() }
    private val topRateAdapter by lazy { PopularMoviesAdapter() }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        moviesViewModel =
            ViewModelProvider(this, Injection.provideMovieViewModelFactory(requireContext(), this))[MoviesViewModel::class.java]

        moviesViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        baseMoviesAdapter.itemClickListener = this
        topRateAdapter.setClickListener(this)

        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.rvMovies.layoutManager = GridLayoutManager(context, 3)
        binding.rvMovies.adapter = baseMoviesAdapter

        binding.topMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.topMovies.adapter = topRateAdapter

        binding.viewModel = moviesViewModel
        binding.executePendingBindings()

        val actionBar =  activity.getActionBar()
        val customView = actionBar?.customView
        val title = customView?.findViewById<AppCompatTextView>(R.id.title_actionBar)
        title?.gravity = Gravity.START
        title?.text = getString(R.string.top_bar_home_title)
        title?.textSize = 18F
        title?.typeface = Typeface.create(null, 700, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTabs()
        bindSearch()

        lifecycleScope.launch{
            moviesViewModel.moviesFlow.collect {
                baseMoviesAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        moviesViewModel.popularMovies.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                binding.topMovies.visibility = View.GONE
                binding.mainBody.visibility = View.GONE
                this@MoviesFragment.showLoading()
            }
            else {
                binding.mainBody.visibility = View.VISIBLE
                binding.topMovies.visibility = View.VISIBLE
                this@MoviesFragment.hideLoading()
            }
            topRateAdapter.updateData(it)
        }
    }

    private fun bindSearch(){
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if(query.isNullOrEmpty()) false
                else {
                    val action = MoviesFragmentDirections.actionNavigationMoviesToNavigationSearchResult(
                        searchQuery = query
                    )
                    findNavController().navigate(action)
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun bindTabs(){
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab
                when(tab?.position){
                    0 -> {
                        moviesViewModel.changeSource(MovieSource.NOW_PLAYING)
                        binding.executePendingBindings()
                    }
                    1 -> {
                        moviesViewModel.changeSource(MovieSource.UPCOMING)
                        binding.executePendingBindings()
                    }
                    2 -> {
                        moviesViewModel.changeSource(MovieSource.TOP_RATED)
                        binding.executePendingBindings()
                    }
                    else -> {
                        moviesViewModel.changeSource(MovieSource.POPULAR)
                        binding.executePendingBindings()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onClick(item: Movie) {
        val action = MoviesFragmentDirections.actionNavigationMoviesToNavigationMovieDetail(id = item.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstCreated) {
            moviesViewModel.fetchMovies()
            moviesViewModel.getTopPopularMovies()
            isFirstCreated = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}