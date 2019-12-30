

package com.colin.load.manager.state

import com.colin.load.manager.R
import com.colin.load.manager.core.LoadCallback

/**
 * @author duanyitao
 */

class Loading(private val loadingResLayout: Int? = null) : LoadCallback() {

    override fun onReloadEvent(): Boolean {
        return true
    }
    override fun onCreateView(): Int {
        return loadingResLayout ?: R.layout.layout_loading_default
    }
}
