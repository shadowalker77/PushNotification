package ir.ayantech.pushnotification

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ir.ayantech.pushsdk.core.AyanNotification
import ir.ayantech.pushsdk.helper.ShareHelper
import ir.ayantech.pushsdk.model.api.PushNotification
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

        copyBtn.setOnClickListener {
            ShareHelper.copyToClipBoard(this, PushNotificationUser.getPushNotificationToken())
            Toast.makeText(this, "Copied", Toast.LENGTH_LONG).show()
        }

        Log.d("TOKEN", PushNotificationUser.getPushNotificationToken())
    }
}
