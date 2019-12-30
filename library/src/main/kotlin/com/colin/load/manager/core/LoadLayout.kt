

package com.colin.load.manager.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.HashMap


/**
 * @author duanyitao
 */

@SuppressLint("ViewConstructor")
internal class LoadLayout : FrameLayout {

    private var onReloadListener: ((View) -> Unit)? = null

    constructor(context: Context, onReloadListener: ((View) -> Unit)? = null) : this(
        context,
        null,
        onReloadListener
    )

    internal constructor(
        context: Context,
        attributeSet: AttributeSet?,
        onReloadListener: ((View) -> Unit)? = null
    ) : this(context, attributeSet, 0, onReloadListener)

    internal constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defStyle: Int,
        onReloadListener: ((View) -> Unit)?
    ) : super(
        context,
        attributeSet,
        defStyle
    ) {
        this.onReloadListener = onReloadListener
    }

    private val callbacks = HashMap<Class<out LoadCallback>, LoadCallback>()

    private var mCurrentLoadCallback: Class<out LoadCallback>? = null

    private val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    fun installSuccessLayout(loadCallback: LoadCallback) {
        addCallback(loadCallback)
    }

    fun installCallbackLayout(callback: LoadCallback) {
        Log.d("load-manager", "install callback: $callback")
        val cloneCallback = callback.copy()
        cloneCallback.setCallback(context, onReloadListener)
        addCallback(cloneCallback)
    }

    @Synchronized
    fun LoadCallback.copy(): LoadCallback {
        val bao = ByteArrayOutputStream()
        val oos: ObjectOutputStream
        oos = ObjectOutputStream(bao)
        oos.writeObject(this)
        oos.close()
        val bis = ByteArrayInputStream(bao.toByteArray())
        val ois = ObjectInputStream(bis)
        val obj = ois.readObject()
        ois.close()
        return obj as LoadCallback
    }

    private fun addCallback(loadCallback: LoadCallback) {
        if (!callbacks.containsKey(loadCallback.javaClass)) {
            callbacks[loadCallback.javaClass] = loadCallback
            val obtainRootView = loadCallback.obtainRootView()
            addView(obtainRootView)
            obtainRootView.visibility = View.GONE
            Log.d("load-manager", "add view: $this")
        }
    }

    fun showCallback(loadCallback: Class<out LoadCallback>) {
        checkCallbackExist(loadCallback)
        if (isMainThread) {
            showCallbackView(loadCallback)
        } else {
            post { showCallbackView(loadCallback) }
        }
    }

    private fun showCallbackView(callback: Class<out LoadCallback>) {

        Log.d("load-manager", "show callback: $callback")
        mCurrentLoadCallback?.let {
            if (it == callback) {
                return
            }
        }

        callbacks.keys.forEach {
            if (it == callback) {
                callbacks[it]!!.obtainRootView().visibility = View.VISIBLE
                mCurrentLoadCallback = it
            } else {
                callbacks[it]!!.obtainRootView().visibility = View.GONE
            }
        }
    }

    private fun checkCallbackExist(loadCallback: Class<out LoadCallback>) {
        require(callbacks.containsKey(loadCallback)) {
            String.format("The LoadCallback (%s) is nonexistent.", loadCallback.simpleName)
        }
    }
}
