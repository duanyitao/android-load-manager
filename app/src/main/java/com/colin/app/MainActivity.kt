package com.colin.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.colin.load.manager.core.LoadCallback
import com.colin.load.manager.core.LoadManager
import com.colin.load.manager.state.Empty
import com.colin.load.manager.state.Error
import com.colin.load.manager.state.Loading
import com.colin.load.manager.state.Success
import com.colin.load.manager.state.Timeout
import kotlinx.android.synthetic.main.activity_main.content

class Custom : LoadCallback() {
    override fun onCreateView(): Int {
        return R.layout.activity_custom
    }
}

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        System.gc()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val inputCallBacks = hashSetOf(
            Loading(),
            Timeout(),
            Error(),
            Empty(),
            Custom()
        )

        LoadManager.install(inputCallBacks)
            .apply {
                setDefaultCallback(Success::class.java)
                setAnimateTime(100)
            }

        content.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }
    }
}
