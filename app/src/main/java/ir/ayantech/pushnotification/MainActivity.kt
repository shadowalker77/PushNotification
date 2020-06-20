package ir.ayantech.pushnotification

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ir.ayantech.pushsdk.core.AyanNotification
import ir.ayantech.pushsdk.helper.ShareHelper
import ir.ayantech.pushsdk.model.MessageDeserializer
import ir.ayantech.pushsdk.model.api.PushNotification
import ir.ayantech.pushsdk.storage.PushNotificationUser
import kotlinx.android.synthetic.main.activity_main.*
import me.yokeyword.fragmentation.SupportActivity
import java.lang.Exception

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

        testMessageBtn.setOnClickListener {
            try {
                val message = MessageDeserializer.stringToMessage("{\"actionType\":\"Share\",\"action\":{\"model\":{\"content\":\"share text\"}},\"notificationToShow\":{\"buttons\":[{\"message\":{\"action\":{\"model\":{}},\"actionType\":\"NoAction\"},\"text\":\"close\"}],\"imageUrl\":null,\"message\":\"\",\"title\":\"salam test\"}}", "")
                AyanNotification.performMessageLogic(this, message)
                Log.d("Msg", message.toString())
            } catch (e: Exception) {
                Log.e("Msg", e.message)
            }
        }

        Log.d("TOKEN", PushNotificationUser.getPushNotificationToken())
    }
}
