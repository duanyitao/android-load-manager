

package com.colin.load.manager.core

import android.view.View
import com.colin.load.manager.inter.INetError
import com.colin.load.manager.inter.INetTimeout
import com.colin.load.manager.state.Error
import com.colin.load.manager.state.Success
import com.colin.load.manager.state.Timeout

/**
 * @author duanyitao
 */
class LoadService internal constructor(
    targetContext: TargetContext,
    onReloadListener: ((View) -> Unit)?
) {

    private val loadLayout: LoadLayout =
        LoadLayout(targetContext.contentParent.context, onReloadListener)

    init {
        targetContext.contentParent.addView(
            loadLayout,
            targetContext.childIndex,
            targetContext.target.layoutParams
        )
        loadLayout.installSuccessLayout(Success(targetContext.target))
        callbacks.forEach { loadLayout.installCallbackLayout(it) }
        showCallback(defaultLoadCallback)
    }

    fun showSuccess() {
        showCallback(Success::class.java)
    }

    private fun showCallback(loadCallback: Class<out LoadCallback>) {
        loadLayout.showCallback(loadCallback)
    }

    fun notify(event: Any) {
        when (event) {
            is INetTimeout -> notify(event)
            is Throwable -> notify(event)
            is INetError -> notify(event)
            is LoadCallback -> notify(event)
            else -> throw IllegalArgumentException("非法参数：$event")
        }
    }

    fun notify(event: LoadCallback) {
        showCallback(event::class.java)
    }

    fun notify(error: INetTimeout) {
        showCallback(Timeout::class.java)
    }

    fun notify(error: Throwable) {
        showCallback(Error::class.java)
    }

    fun notify(error: INetError) {
        showCallback(Error::class.java)
    }
}
