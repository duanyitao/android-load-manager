

package com.colin.load.manager.core

import android.content.Context
import android.view.View
import java.io.Serializable

/**
 * @author duanyitao
 */
abstract class LoadCallback : Serializable {
    private var rootView: View? = null
    private lateinit var context: Context
    private var onReloadListener: ((View) -> Unit)? = null

    constructor()

    internal constructor(view: View, context: Context, onReloadListener: ((View) -> Unit)?) {
        this.rootView = view
        this.context = context
        this.onReloadListener = onReloadListener
    }

    internal fun setCallback(
        context: Context,
        onReloadListener: ((View) -> Unit)?
    ): LoadCallback {
        this.context = context
        this.onReloadListener = onReloadListener
        return this
    }

    protected open fun onReloadEvent(): Boolean {
        return false
    }

    fun obtainRootView(): View {
        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null)
        }
        rootView!!.setOnClickListener { v ->
            if (onReloadEvent()) {
                return@setOnClickListener
            }
            onReloadListener?.invoke(v)
        }
        return rootView!!
    }

    protected abstract fun onCreateView(): Int
}
