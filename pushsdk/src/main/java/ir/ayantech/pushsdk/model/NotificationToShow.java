package ir.ayantech.pushsdk.model;

import java.io.Serializable;
import java.util.List;

import ir.ayantech.pushsdk.activity.CustomizableDialogActivity;


public class NotificationToShow implements Serializable {
    private String title;
    private String message;
    private String imageUrl;
    private String iconUrl;
    private boolean useCustomView;
    private List<CustomizableDialogActivity.Button> buttons;

    public NotificationToShow(String title, String message, String imageUrl, String iconUrl, boolean useCustomView, List<CustomizableDialogActivity.Button> buttons) {
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.iconUrl = iconUrl;
        this.useCustomView = useCustomView;
        this.buttons = buttons;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<CustomizableDialogActivity.Button> getButtons() {
        return buttons;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean isUseCustomView() {
        return useCustomView;
    }
}
