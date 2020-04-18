package ir.ayantech.pushnotification

import ir.ayantech.pushsdk.model.api.NotificationObject
import kotlinx.android.synthetic.main.fragment_message_details.*

class MessageDetailsFragment : FragmentationFragment() {
    override fun getLayoutId() = R.layout.fragment_message_details
    override fun getPageTitle() = "جزئیات پیام"

    lateinit var notificationObject: NotificationObject<*>

    override fun onCreate() {
        super.onCreate()

        getNotificationDetail()

        notificationObject.getImage {
            notifIv.setImageBitmap(it)
        }
    }

    private fun getNotificationDetail() {
        notificationObject.getNotificationDetail { success, notification ->
            if (success) {
                titleTv.text = notification?.title
                dateTv.text = notification?.sendDateTime
                bodyTv.text = notification?.body
            }
        }
    }
}