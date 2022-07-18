package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.fragment

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R

open class BaseFragment : Fragment() {
    private var progressBar : ProgressBar? = null

    fun setProgressBar(bar : ProgressBar){
        progressBar = bar
    }

    fun showProgressBar(){
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        progressBar?.visibility = View.INVISIBLE
    }

    fun hideKeyBoard(view : View){
        val imn = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(view.windowToken,0)
    }

    override fun onStop() {
        super.onStop()
    }
}