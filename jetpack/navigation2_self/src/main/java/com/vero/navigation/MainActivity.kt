package com.vero.navigation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.vero.libnetwork.ApiResponse
import com.vero.libnetwork.GetRequest
import com.vero.libnetwork.JsonCallback
import com.vero.navigation.utils.NavGraphBuilder
import org.json.JSONObject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = NavHostFragment.findNavController(fragment!!)


        navView.setupWithNavController(navController!!)

        //通过JSON构建导航图
        NavGraphBuilder.Companion.build(navController!!, this, fragment.id)

        //设置bottom
        navView.setOnNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        navController?.navigate(menuItem.itemId)
        //return true:被选中，被着色，shifting
        //return false:没有被选中，不被着色，没有shifting
        return TextUtils.isEmpty(menuItem.title)
    }
}





















