

package com.colin.load.manager.state

import com.colin.load.manager.R
import com.colin.load.manager.core.LoadCallback

/**
 * @author duanyitao
 */

class Empty(private val emptyResLayout: Int? = null) : LoadCallback() {

    override fun onCreateView(): Int {
        return emptyResLayout ?: R.layout.layout_empty_default
    }
}
