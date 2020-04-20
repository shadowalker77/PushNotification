package ir.ayantech.pushnotification

import android.os.Bundle
import android.util.Log
import ir.ayantech.pushsdk.storage.PushNotificationUser
import kotlinx.android.synthetic.main.activity_main.*
import me.yokeyword.fragmentation.SupportActivity

class MainActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getListBtn.setOnClickListener {
            loadRootFragment(R.id.fragmentContainer, MessagesFragment())
        }

        Log.d("TOKEN", PushNotificationUser.getPushNotificationToken())
    }
}
