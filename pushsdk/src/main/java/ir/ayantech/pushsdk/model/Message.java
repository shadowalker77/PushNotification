package ir.ayantech.pushsdk.model;

import java.io.Serializable;

import ir.ayantech.pushsdk.model.action.PushNotificationAction;

public class Message<T extends PushNotificationAction> implements Serializable {
    private T action;
    private String actionType;
    private NotificationToShow notificationToShow;
    private String messageId;

    public Message(T action, String actionType, NotificationToShow notificationToShow, String messageId) {
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public NotificationToShow getNotificationToShow() {
        return notificationToShow;
    }

    public void setNotificationToShow(NotificationToShow notificationToShow) {
        this.notificationToShow = notificationToShow;
    }
}
