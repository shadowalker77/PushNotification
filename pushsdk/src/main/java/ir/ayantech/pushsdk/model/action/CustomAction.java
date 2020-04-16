package ir.ayantech.pushsdk.model.action;

import android.content.Context;

import java.io.Serializable;

public class CustomAction extends PushNotificationAction implements Serializable {

    private Model model;
    private CustomActionReceived customActionReceived;

    public CustomAction(Context context, String customActionName, String customInput, CustomActionReceived customActionReceived) {
        this.setContext(context);
        this.model = new Model(customActionName, customInput);
        this.customActionReceived = customActionReceived;
    }

    @Override
    public void doAction() {
        customActionReceived.onCustomActionReceived(model.getCustomActionName(), model.getCustomInput());
    }

    public static class Model extends ActionModel {
        private String customActionName;
        private String customInput;

        public Model(String customActionName, String customInput) {
            this.customActionName = customActionName;
            this.customInput = customInput;
        }

        public String getCustomActionName() {
            return customActionName;
        }

        public void setCustomActionName(String customActionName) {
            this.customActionName = customActionName;
        }

        public String getCustomInput() {
            return customInput;
        }

        public void setCustomInput(String customInput) {
            this.customInput = customInput;
        }
    }
}
