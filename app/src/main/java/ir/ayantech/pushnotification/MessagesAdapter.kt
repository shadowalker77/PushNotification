package ir.ayantech.pushnotification

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ir.ayantech.pushsdk.model.api.NotificationObject
import kotlinx.android.synthetic.main.row_message.view.*

class MessagesAdapter(
    private val context: Context,
    private val onMessageRemoved: (NotificationObject<*>) -> Unit,
    private val onMessageSelected: (NotificationObject<*>) -> Unit
) : RecyclerView.Adapter<CommonViewHolder>() {

    private var messagesList = arrayListOf<NotificationObject<*>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_message, parent, false)
        return CommonViewHolder(inflatedView)
    }

    override fun getItemCount() = messagesList.size

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val pm = PopupMenu(context, holder.itemView.leftMenuIv)
        holder.itemView.titleTv.text = messagesList[position].title
        holder.itemView.dateTv.text = messagesList[position].sendDateTime
        holder.itemView.bodyTv.text = messagesList[position].body

        if (messagesList[position].buttons != null) {
            holder.itemView.leftMenuIv.visibility = View.VISIBLE
            messagesList[position].buttons?.forEachIndexed { index, notificationObject ->
                notificationObject.title?.let {title->
                    pm.menu.add(Menu.NONE, index, 1, title)
                }
            }
        } else {
            holder.itemView.leftMenuIv.visibility = View.GONE
        }

        if (messagesList[position].seen) {
            holder.itemView.statusIv.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.grey_circle
                )
            )
        } else {
            holder.itemView.statusIv.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.red_circle
                )
            )
        }

        holder.itemView.removeTv.setOnClickListener {
                onMessageRemoved(messagesList[position])
        }

        holder.itemView.leftMenuIv.setOnClickListener {
            pm.show()
        }

        pm.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                0 -> messagesList[position].buttons?.get(0)?.performAction(context)
                1 -> messagesList[position].buttons?.get(1)?.performAction(context)
                2 -> messagesList[position].buttons?.get(2)?.performAction(context)
            }
            true
        }

        holder.itemView.setOnClickListener {
            onMessageSelected(messagesList[position])
        }
    }

    fun replaceWith(items: List<NotificationObject<*>>) {
        messagesList = items as ArrayList<NotificationObject<*>>
        notifyDataSetChanged()
    }
}