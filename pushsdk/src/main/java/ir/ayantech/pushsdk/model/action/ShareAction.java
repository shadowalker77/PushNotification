package ir.ayantech.pushsdk.model.action;

import java.io.Serializable;

import ir.ayantech.pushsdk.helper.ShareHelper;

public class ShareAction extends PushNotificationAction implements Serializable {

    private Model model;

    public ShareAction(String content) {
        this.model = new Model(content);
    }

    @Override
    public void doAction() {
        ShareHelper.share(getContext(), model.getContent());
    }

    public static class Model extends ActionModel {
        private String content;

        public Model(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
