package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModel

class MoreAboutFragment : Fragment() {

    private val moviesViewModel: MoviesViewModel by viewModels({
        requireActivity()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_about_movie, container, false)
        val textView: TextView = view.findViewById(R.id.movie_overview)

        moviesViewModel.currentMovie.observe(viewLifecycleOwner){movie ->
            Log.d("movie", movie.overview)
            textView.post {
                textView.text = movie.overview
            }
        }

        return view
    }

    companion object {
        fun newInstance() = MoreAboutFragment()
    }
}