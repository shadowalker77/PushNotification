package ir.ayantech.pushsdk.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

import ir.ayantech.pushsdk.model.action.PushNotificationAction;

public class Message<T extends PushNotificationAction> implements Serializable {
    private T action;
    private String actionType;
    private NotificationToShow notificationToShow;
    @Nullable
    private String messageId;

    public Message(T action, String actionType, NotificationToShow notificationToShow, @Nullable String messageId) {
        this.action = action;
        this.actionType = actionType;
        this.notificationToShow = notificationToShow;
        this.messageId = messageId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public T getAction() {
        return action;
    }

    public void setAction(T action) {
        this.action = action;
    }

    @Nullable
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(@Nullable String messageId) {
        this.messageId = messageId;
    }

    public NotificationToShow getNotificationToShow() {
        return notificationToShow;
    }

    public void setNotificationToShow(NotificationToShow notificationToShow) {
        this.notificationToShow = notificationToShow;
    }
}
