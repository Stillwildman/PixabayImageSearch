package com.vincent.imagesearch.utilities

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.vincent.imagesearch.R

/**
 * Created by Vincent on 2020/11/17.
 */
object MenuHelper {

    fun setMenuOptions(menu: Menu, actions: IntArray?) {
        menu.clear()

        if (actions == null) {
            return
        }

        var titleRes = 0
        var iconRes = 0

        for (action in actions) {
            when (action) {
                MenuActions.ACTION_ROW_VIEW_TYPE -> {
                    titleRes = R.string.action_list_view
                    iconRes = R.drawable.selector_list
                }
                MenuActions.ACTION_GRID_VIEW_TYPE -> {
                    titleRes = R.string.action_grid_view
                    iconRes = R.drawable.selector_grid
                }
            }

            if (menu.findItem(action) == null) {
                menu.add(Menu.NONE, action, Menu.NONE, titleRes)
                    .setIcon(iconRes)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

                Log.i("MenuHelper", "onMenuSet: $action")
            }
        }
    }

    fun removeMenuOptions(menu: Menu, vararg actions: Int) {
        for (action in actions) {
            menu.removeItem(action)
        }
    }

}