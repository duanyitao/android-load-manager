

package com.colin.load.manager.core

import android.view.View
import android.view.ViewGroup

/**
 * @author duanyitao
 */
internal data class TargetContext(
    val contentParent: ViewGroup,
    val target: View,
    val childIndex: Int
)
