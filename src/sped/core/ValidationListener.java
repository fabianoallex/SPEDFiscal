package sped.core;

public interface ValidationListener {
    void onSuccess(ValidationEvent event);
    void onWarning(ValidationEvent event);
    void onError(ValidationEvent event);
}
