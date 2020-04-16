package ir.ayantech.pushsdk.model.action;

import android.content.Intent;

import com.google.gson.Gson;

import java.io.Serializable;

public class TargetedClassAction extends PushNotificationAction implements Serializable {

    public static final String TARGETED_CLASS_PROPERTIES_TAG = "TARGETED_CLASS_PROPERTIES_TAG";

    private Model model;

    public TargetedClassAction(String activityClass, String className, String injectValuesJson) {
        this.model = new Model(activityClass, className, injectValuesJson);
    }

    @Override
    public void doAction() {
        try {
            Intent intent = new Intent(getContext(), Class.forName(model.getActivityClass()));
            if (model.getClassName() != null)
                intent.putExtra(TARGETED_CLASS_PROPERTIES_TAG, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Object getCustomClass(Model model) {
        try {
            return new Gson().fromJson(model.injectValuesJson, Class.forName(model.getClassName()).getClass());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object handleFromActivity(Intent intent) {
        if (intent.getSerializableExtra(TARGETED_CLASS_PROPERTIES_TAG) == null)
            return null;
        return getCustomClass((Model) intent.getSerializableExtra(TARGETED_CLASS_PROPERTIES_TAG));
    }

    public static class Model extends ActionModel {
        private String activityClass;
        private String className;
        private String injectValuesJson;

        public Model(String activityClass, String className, String injectValuesJson) {
            this.activityClass = activityClass;
            this.className = className;
            this.injectValuesJson = injectValuesJson;
        }

        public String getActivityClass() {
            return activityClass;
        }

        public void setActivityClass(String activityClass) {
            this.activityClass = activityClass;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getInjectValuesJson() {
            return injectValuesJson;
        }

        public void setInjectValuesJson(String injectValuesJson) {
            this.injectValuesJson = injectValuesJson;
        }
    }
}
