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

@FragmentDestination(pageUrl = "main/tabs/publish")
class PublishFragment : BaseFragment() {

    private lateinit var publishViewModel:  PublishModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        publishViewModel =
            ViewModelProviders.of(this).get(PublishModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        publishViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
