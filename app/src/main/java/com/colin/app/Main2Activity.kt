

package com.colin.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.colin.load.manager.core.LoadService
import com.colin.load.manager.core.observe
import com.colin.load.manager.state.Loading
import com.colin.load.manager.state.Timeout
import kotlinx.android.synthetic.main.activity_main.content
import kotlin.concurrent.thread

class Main2Activity : AppCompatActivity() {

    private var finish = false
    private lateinit var loadService: LoadService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadService = content.observe {
            Toast.makeText(this, "reload", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Main3Activity::class.java))
        }
        val time = 1000L

        thread {
            repeat(3) {
                if (!finish) {
                    (content.parent as ViewGroup).postDelayed({ loadService.notify(Loading()) }, time)
                    Thread.sleep(time)
                    (content.parent as ViewGroup).postDelayed({ loadService.notify(Throwable()) }, time)
                    Thread.sleep(time)
                    (content.parent as ViewGroup).postDelayed({ loadService.notify(Custom()) }, time)
                    Thread.sleep(time)
                    (content.parent as ViewGroup).postDelayed({ loadService.notify(Timeout()) }, time)
                    Thread.sleep(time)
                    (content.parent as ViewGroup).postDelayed({ loadService.showSuccess() }, time)
                    Thread.sleep(time)

                    Log.d("load-manager", "doing...")
                } else {
                    return@repeat
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish = true
    }
}
