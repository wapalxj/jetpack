package com.vero.navigation.utils

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import com.vero.navigation.FixFragmentNavigator

class NavGraphBuilder {
    companion object {
        fun build(controller: NavController, activity: FragmentActivity, containerId: Int) {
            val provider = controller.navigatorProvider
//            val fragmentNavigator = provider.getNavigator(FragmentNavigator::class.java)
            val activityNavigator = provider.getNavigator(ActivityNavigator::class.java)


            //使用自定义fragment导航器
            val fragmentNavigator =
                FixFragmentNavigator(activity, activity.supportFragmentManager, containerId)
            provider.addNavigator(fragmentNavigator)


            val navGraph = NavGraph(NavGraphNavigator(provider))

            //解析destination.json
            val destConfig = AppConfig.getsDestConfig()

            for (value in destConfig.values) {
                if (value.isIsFragment) {
                    //fragment
                    val destination = fragmentNavigator.createDestination()
                    destination.className = value.clazName
                    destination.id = value.id
                    destination.addDeepLink(value.pageUrl)

                    navGraph.addDestination(destination)
                } else {
                    //activity
                    val destination = activityNavigator.createDestination()
                    destination.setComponentName(
                        ComponentName(
                            com.vero.libcommon.AppGlobals.sApplication.packageName,
                            value.clazName
                        )
                    )
                    destination.id = value.id
                    destination.addDeepLink(value.pageUrl)
                    navGraph.addDestination(destination)
                }
                if (value.isAsStarter) {
                    navGraph.startDestination = value.id
                }
            }
            //添加导航图
            controller.graph = navGraph
        }
    }

}