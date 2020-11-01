package com.vero.navigation.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vero.libnavannotation.FragmentDestination
import com.vero.navigation.R
import com.vero.navigation.ui.BaseFragment

@FragmentDestination(pageUrl = "main/tabs/find")
class FindFragment : BaseFragment() {

    private lateinit var findViewModel:  FindViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        findViewModel =
            ViewModelProviders.of(this).get(FindViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        findViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
