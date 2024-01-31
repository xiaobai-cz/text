package io.github.xiaobaicz.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.xiaobaicz.demo.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private val bind by lazy { ActivityAppBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
    }

}