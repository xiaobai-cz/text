package cc.xiaobaicz.widgets.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.xiaobaicz.widgets.demo.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private val bind by lazy { ActivityAppBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
    }

}