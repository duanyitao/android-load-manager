

package com.colin.load.manager.core

import android.view.View
import android.view.ViewGroup

/**
 * @author duanyitao
 */

internal var callbacks = HashSet<LoadCallback>()
internal lateinit var defaultLoadCallback: Class<out LoadCallback>
internal var animateTime: Long = 500L

fun View.observe(onReloadListener: ((View) -> Unit)?): LoadService {
    val targetContext = getTargetContext(this)
    return LoadService(targetContext, onReloadListener)
}

internal fun getTargetContext(target: View): TargetContext {
    val contentParent = target.parent as ViewGroup
    val childIndex = contentParent.indexOfChild(target)
    contentParent.removeView(target)
    return TargetContext(contentParent, target, childIndex)
}

object LoadManager {

    fun install(inputCallBacks: HashSet<LoadCallback>): LoadManager {
        if (callbacks.isEmpty()) {
            inputCallBacks.forEach { callbacks.add(it) }
            return this
        } else {
            throw IllegalStateException("you have repeat call 'install' method")
        }
    }

    fun setDefaultCallback(callback: Class<out LoadCallback>) {
        defaultLoadCallback = callback
    }

    fun setAnimateTime(time: Long) {
        animateTime = time
    }
}
