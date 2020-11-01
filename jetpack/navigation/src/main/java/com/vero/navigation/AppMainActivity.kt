package com.vero.navigation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vero.navigation.MainActivity
import com.vero.navigation.R

class AppMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.data= Uri.parse("vero://www.baidu.com")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_main)

//        val navController = findNavController(R.id.root)
//        val intent=Intent()
//        intent.data= Uri.parse("vero://www.baidu.com")
//        navController.handleDeepLink(intent)


//        val intent=Intent(this@AppMainActivity,MainActivity::class.java)
//        intent.data= Uri.parse("vero://www.baidu.com")
//        startActivity(intent)


        ////////////////////////////////

        Log.e("AppMainActivity","onCreate")
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


//        val deeplink = navController.createDeepLink()
//            .setDestination(R.id.navigation_notifications)
//            .createPendingIntent()
//
//        val notificationManager =
//            this?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(
//                NotificationChannel(
//                    "deeplink", "Deep Links", NotificationManager.IMPORTANCE_HIGH)
//            )
//        }
//
//        val builder = NotificationCompat.Builder(
//            this!!, "deeplink")
//            .setContentTitle("Navigation")
//            .setContentText("Deep link to Android")
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentIntent(deeplink)
//            .setAutoCancel(true)
//        notificationManager.notify(0, builder.build())

        navController.handleDeepLink(intent)

    }
}
