

package com.colin.load.manager.state

import android.view.View
import com.colin.load.manager.core.LoadCallback

class Success internal constructor(
    view: View
) : LoadCallback(view, view.context, null) {
    override fun onReloadEvent(): Boolean {
        return true
    }

    override fun onCreateView(): Int {
        return 0
    }
}