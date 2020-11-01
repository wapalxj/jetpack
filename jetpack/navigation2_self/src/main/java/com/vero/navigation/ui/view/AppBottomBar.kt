package com.vero.navigation.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.vero.navigation.R
import com.vero.navigation.utils.AppConfig

class AppBottomBar : BottomNavigationView {
    constructor(context: Context?) : super(context, null) {}
    @JvmOverloads
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    //tab icon
    var sIcons = arrayOf(
        R.mipmap.icon_tab_home,
        R.mipmap.icon_tab_sofa,
        R.mipmap.icon_tab_publish,
        R.mipmap.icon_tab_find,
        R.mipmap.icon_tab_mine
    )

    @SuppressLint("RestrictedApi")
    private fun init() {
        val bottomBar = AppConfig.getBottomBar()
        val tabs = bottomBar.tabs
        //二维数组
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()
        val colors = intArrayOf(
            Color.parseColor(bottomBar.activeColor),
            Color.parseColor(bottomBar.inActiveColor)
        )
        val colorStateList = ColorStateList(states, colors)
        itemIconTintList = colorStateList
        itemTextColor = colorStateList
        //任何情况都显示文本
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        selectedItemId = bottomBar.selectTab

        for (i in tabs.indices) {
            val tab = tabs[i]
            if (!tab.isEnable) {
                return
            }
            val id = getId(tab.pageUrl)
            if (id < 0) {
                return
            }
            val item = menu.add(0, id, tab.index, tab.title)

            //添加所有的按钮
            item.setIcon(sIcons[tab.index])
        }

        //等所有按钮已添加后，调整大小
        //不能直接设置，需要获取第一个子view BottomNavigationMenuView

        for (i in tabs.indices) {
            val tab = tabs[i]
            val iconSize = dp2px(tab.size)

            val menuView = getChildAt(0) as BottomNavigationMenuView
            val menuItemView=menuView.getChildAt(tab.index) as BottomNavigationItemView
            menuItemView.setIconSize(iconSize.toInt())

            if (TextUtils.isEmpty(tab.title)) {
                menuItemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)))
               //上下浮动
                menuItemView.setShifting(false)
            }
        }

    }

    private fun dp2px(size: Int): Float {
        return context.resources.displayMetrics.density * size + 0.5f
    }

    private fun getId(pageUrl: String): Int {
        val destination = AppConfig.getsDestConfig()[pageUrl]
        return destination?.id ?: -1
    }
}