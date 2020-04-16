package ir.ayantech.pushsdk.model.action;

public interface CustomActionReceived {
    void onCustomActionReceived(String customActionName, String customInput);
}
