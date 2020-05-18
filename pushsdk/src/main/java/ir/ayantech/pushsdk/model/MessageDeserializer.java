package ir.ayantech.pushsdk.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ir.ayantech.pushsdk.model.action.CustomAction;
import ir.ayantech.pushsdk.model.action.CustomizableDialogAction;
import ir.ayantech.pushsdk.model.action.DownloadFileAction;
import ir.ayantech.pushsdk.model.action.NoAction;
import ir.ayantech.pushsdk.model.action.OpenApplicationAction;
import ir.ayantech.pushsdk.model.action.OpenUrlAction;
import ir.ayantech.pushsdk.model.action.PushNotificationAction;
import ir.ayantech.pushsdk.model.action.ShareAction;
import ir.ayantech.pushsdk.model.action.TargetedClassAction;

public class MessageDeserializer implements JsonDeserializer<Message> {

    private String messageId;

    public MessageDeserializer(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String actionType = json.getAsJsonObject().get("actionType").getAsString();
        Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageDeserializer(messageId)).create();
        NotificationToShow notificationToShow = gson.fromJson(json.getAsJsonObject().get("notificationToShow"), NotificationToShow.class);
        PushNotificationAction action;
        Class<? extends PushNotificationAction> classOfAction;
        switch (actionType) {
            case "CustomizableDialog":
                classOfAction = CustomizableDialogAction.class;
                break;
            case "DownloadFile":
                classOfAction = DownloadFileAction.class;
                break;
            case "OpenUrl":
                classOfAction = OpenUrlAction.class;
                break;
            case "Share":
                classOfAction = ShareAction.class;
                break;
            case "TargetedClass":
                classOfAction = TargetedClassAction.class;
                break;
            case "OpenApplication":
                classOfAction = OpenApplicationAction.class;
                break;
            case "Custom":
                classOfAction = CustomAction.class;
                break;
            default:
                classOfAction = NoAction.class;
                break;
        }
        action = gson.fromJson(json.getAsJsonObject().get("action"), classOfAction);
        return new Message<>(action, actionType, notificationToShow, messageId);
    }

    public static Message stringToMessage(String body, String messageId) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageDeserializer(messageId)).create();
            return gson.fromJson(body, Message.class);
        } catch (Exception e){
            return null;
        }
    }
}