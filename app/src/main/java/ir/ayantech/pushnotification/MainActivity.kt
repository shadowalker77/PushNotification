package ir.ayantech.pushnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ir.ayantech.pushsdk.core.AyanNotification
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getListBtn.setOnClickListener {
            AyanNotification.getNotificationList(10) { totalCount, unSeenCount, notifications, getNextPageClosure ->
                Log.d("notifs", notifications.toString())
                getNextPageClosure?.invoke()
            }
        }
    }
}
