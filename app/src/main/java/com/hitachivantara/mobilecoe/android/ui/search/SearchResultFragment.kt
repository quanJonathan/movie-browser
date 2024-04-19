package com.hitachivantara.mobilecoe.android.ui.search

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import com.hitachivantara.mobilecoe.android.databinding.FragmentSearchBinding
import com.hitachivantara.mobilecoe.android.ui.utils.ItemClickListener
import com.hitachivantara.mobilecoe.android.ui.utils.getActionBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultFragment : Fragment(), ItemClickListener<Movie> {

    private val preferencesStore by lazy { PreferencesStore.getInstance(requireContext()) }
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val navigationArgs: SearchResultFragmentArgs by navArgs()

    private val searchViewModel: SearchResultViewModel by viewModels {
        SearchResultViewModelFactory(preferencesStore)
    }

    private lateinit var searchQuery: String

    private val searchResultAdapter by lazy { SearchResultAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchQuery = navigationArgs.searchQuery

        return root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvSearchResult.layoutManager = layoutManager
        binding.rvSearchResult.adapter = searchResultAdapter

        if(searchQuery.isNotEmpty()){
            binding.searchBar.setQuery(searchQuery, true)
            searchViewModel.searchMovies(searchQuery)
        }

        lifecycleScope.launch {
            searchViewModel.searchResultFlow.collectLatest {
                searchResultAdapter.submitData(it)
            }
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("queryString", query.toString())
                return if(query.isNullOrEmpty()) false
                else {
                    searchViewModel.searchMovies(query)
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if(newText.isNullOrEmpty()) false
                else {
                    searchViewModel.searchMovies(newText)
                    true
                }
            }
        })

        searchResultAdapter.itemClickListener = this

        searchResultAdapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && searchResultAdapter.itemCount == 0
            binding.rvSearchResult.visibility = if (isListEmpty) View.GONE else View.VISIBLE
            binding.noResultsGroup.visibility = if (isListEmpty) View.VISIBLE else View.GONE
        }

        val actionBar =  activity.getActionBar()
        val customView = actionBar?.customView
        val title = customView?.findViewById<AppCompatTextView>(R.id.title_actionBar)
        title?.text = getString(R.string.title_search)
        title?.typeface = Typeface.create(null, 400, false)
    }

    override fun onClick(item: Movie) {
        val action = SearchResultFragmentDirections.actionNavigationSearchResultToNavigationMovieDetail(item.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}