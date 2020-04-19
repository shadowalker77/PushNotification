package ir.ayantech.pushnotification

import android.os.Handler
import android.view.View
import ir.ayantech.pushsdk.core.AyanNotification
import ir.ayantech.pushsdk.model.api.NotificationObject
import ir.ayantech.pushsdk.networking.SimpleCallBack
import kotlinx.android.synthetic.main.fragment_messages.*

class MessagesFragment : FragmentationFragment() {
    override fun getLayoutId() = R.layout.fragment_messages
    override fun getPageTitle() = "پیام\u200Cها"

    private var messagesList = arrayListOf<NotificationObject<*>>()

    override fun onCreate() {
        super.onCreate()
        setupMessagesAdapter()
        messagesRv.onScrollToBottom {
            if (it >= messagesList.size - 1) {
                getNextPage?.let {
                    it.invoke()
                    progressbar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        messagesList.clear()
        messagesRv.adapter?.notifyDataSetChanged()
        Handler().postDelayed({
            getMessagesList()
        }, 500)
    }

    private fun getMessagesList() {
        AyanNotification.getNotificationList(10) { success, totalCount, unSeenCount, notifications, getNextPageClosure ->
            if (success) {
                notifications?.let { messagesList.addAll(it) }
                if (messagesList.isEmpty()) {
                    noMessageTv.visibility = View.VISIBLE
                } else {
                    noMessageTv.visibility = View.GONE
                    progressbar.visibility = View.GONE
                    messagesRv.adapter?.let {
                        (it as MessagesAdapter).replaceWith(messagesList)
                    }
                }
                getNextPage = getNextPageClosure
            }
        }
    }

    var getNextPage: SimpleCallBack? = null

    private fun setupMessagesAdapter() {
        messagesRv.linearSetupWithDivider()
        messagesRv.adapter = MessagesAdapter(context!!, {
            it.remove { success ->
                if (success) {
                    messagesList.clear()
                    getMessagesList()
                }
            }

        }) { notificationObject ->
            start(MessageDetailsFragment().also {
                it.notificationObject = notificationObject
            })
        }
    }

}