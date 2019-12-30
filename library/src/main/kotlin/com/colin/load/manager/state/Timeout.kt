

package com.colin.load.manager.state

import com.colin.load.manager.R
import com.colin.load.manager.core.LoadCallback

/**
 * @author duanyitao
 */

class Timeout(private val timeoutResLayout: Int? = null) : LoadCallback() {

    override fun onCreateView(): Int {
        return timeoutResLayout ?: R.layout.layout_timeout_default
    }
}
