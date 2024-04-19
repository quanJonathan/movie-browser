package com.hitachivantara.mobilecoe.android.ui.utils

import androidx.fragment.app.Fragment
import com.hitachivantara.mobilecoe.android.ui.MainActivity

fun Fragment?.showLoading() {
    (this?.activity as? MainActivity)?.showLoading()
}

fun Fragment?.hideLoading() {
    (this?.activity as? MainActivity)?.hideLoading()
}