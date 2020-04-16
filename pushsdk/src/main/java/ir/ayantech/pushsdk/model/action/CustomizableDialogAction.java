package ir.ayantech.pushsdk.model.action;

import java.io.Serializable;
import java.util.List;

import ir.ayantech.pushsdk.activity.CustomizableDialogActivity;

public class CustomizableDialogAction extends PushNotificationAction implements Serializable {

    private Model model;

    public CustomizableDialogAction(String title, String message, String imageUrl, List<CustomizableDialogActivity.Button> buttons) {
        this.model = new Model(title, message, imageUrl, buttons);
    }

    @Override
    public void doAction() {
        CustomizableDialogActivity.createAndStartActivity(getContext(), model);
    }

    public static class Model extends ActionModel {
        private String title;
        private String message;
        private String imageUrl;
        private List<CustomizableDialogActivity.Button> buttons;

        public Model(String title, String message, String imageUrl, List<CustomizableDialogActivity.Button> buttons) {
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.buttons = buttons;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public List<CustomizableDialogActivity.Button> getButtons() {
            return buttons;
        }

        public void setButtons(List<CustomizableDialogActivity.Button> buttons) {
            this.buttons = buttons;
        }
    }
}
