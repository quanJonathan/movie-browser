package com.hitachivantara.mobilecoe.android.ui.utils

import android.app.Activity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hitachivantara.mobilecoe.android.ui.MainActivity

fun Fragment?.showLoading() {
    (this?.activity as? MainActivity)?.showLoading()
}

fun Fragment?.hideLoading() {
    (this?.activity as? MainActivity)?.hideLoading()
}

fun Activity?.getActionBar(): ActionBar?{
    return (this as AppCompatActivity).supportActionBar
}