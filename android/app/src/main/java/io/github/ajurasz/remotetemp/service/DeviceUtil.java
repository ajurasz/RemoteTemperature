package io.github.ajurasz.remotetemp.service;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.iid.FirebaseInstanceId;

public class DeviceUtil {

    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        return Settings.Secure.getString(AppHolder.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceName() {
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        return  deviceMan + " " +deviceName;
    }

    public static String getRegistrationId() {
        return FirebaseInstanceId.getInstance().getToken();
    }
}
