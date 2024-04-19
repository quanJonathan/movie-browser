package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayoutMediator
import com.hitachivantara.mobilecoe.android.Injection
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.databinding.ActivityMovieDetailBinding
import com.hitachivantara.mobilecoe.android.ui.MainActivity
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModel


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var moviesViewModel: MoviesViewModel

    private var movie: Movie? = null

    private var menu: Menu? = null

    private val navigationArgs: MovieDetailActivityArgs by navArgs()
    private lateinit var navController: NavController

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesViewModel =
            ViewModelProvider(
                this,
                Injection.provideMovieViewModelFactory(this, this)
            )[MoviesViewModel::class.java]

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        binding.mainBody.visibility = View.GONE

        binding.ratingOverall.setOnClickListener {
            showDialog()
        }

        val id = navigationArgs.id
        moviesViewModel.getMovie(id)
        moviesViewModel.currentMovie.observe(this) { currentMovie ->
            movie = currentMovie
            bind(currentMovie)
        }

        val viewPager = binding.viewPager
        val tabLayout = binding.tabs

        viewPager.adapter = MovieDetailsPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "About Movie"
                1 -> "Reviews"
                2 -> "Cast"
                else -> null
            }
        }.attach()

        viewPager.currentItem = 0


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Details"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_left)
    }

    fun showDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet)

        val bottomSheet = dialog.findViewById<LinearLayout>(R.id.bottom_sheet)

        val closeButton = dialog.findViewById<ImageView>(R.id.close_button)
        closeButton.setOnClickListener{
            dialog.dismiss()
        }

        val rateScoreTV = dialog.findViewById<TextView>(R.id.rate_score)


        val slider = dialog.findViewById<Slider>(R.id.rate_slider)
        slider.addOnChangeListener { slider, value, fromUser ->
            rateScoreTV.text = String.format("%.3f", slider.value)
        }
        rateScoreTV.text = String.format("%.3f", slider.value)

        val submitButton = dialog.findViewById<AppCompatButton>(R.id.submit_button)
        submitButton.setOnClickListener{
            moviesViewModel.rateMovie(movie, slider.value)
            dialog.dismiss()
        }

        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        this.menu = menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.save_to_watch_list -> {
                if(movie == null) return false
                else{
                    movie?.let {
                        val result = moviesViewModel.actionOnWatchList(it)
                        if(result == 0 ){
                            item.setIcon(R.drawable.saved)
                            Toast.makeText(this@MovieDetailActivity, "Saved to watch list", Toast.LENGTH_SHORT).show()
                        }else{
                            item.setIcon(R.drawable.save)
                            Toast.makeText(this@MovieDetailActivity, "Deleted from watch list", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            else -> super.onOptionsItemSelected(item)
        }
        return true
    }


    private fun bind(movie: Movie) {
        binding.progressBar.visibility = View.GONE
        binding.mainBody.visibility = View.VISIBLE
        binding.movie = movie

        if(moviesViewModel.hasMovieInWatchList(movie)){
            menu?.getItem(0)?.setIcon(R.drawable.saved)
        }
    }
}