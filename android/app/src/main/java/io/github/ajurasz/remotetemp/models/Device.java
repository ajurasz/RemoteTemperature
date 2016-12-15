package io.github.ajurasz.remotetemp.models;

public class Device {
    private String deviceName;
    private String deviceId;
    private String registrationId;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
