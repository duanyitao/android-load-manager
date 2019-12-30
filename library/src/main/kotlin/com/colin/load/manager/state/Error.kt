

package com.colin.load.manager.state

import com.colin.load.manager.R
import com.colin.load.manager.core.LoadCallback

/**
 * @author duanyitao
 */

class Error(private val errorResLayout: Int? = null) : LoadCallback() {
    override fun onCreateView(): Int {
        return errorResLayout ?: R.layout.layout_error_default
    }
}
