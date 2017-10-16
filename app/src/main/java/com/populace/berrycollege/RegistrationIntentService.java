package com.populace.berrycollege;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.push.PushNotification;
import com.shephertz.app42.paas.sdk.android.push.PushNotificationService;

import java.io.IOException;
import java.util.ArrayList;

import static com.facebook.login.widget.ProfilePictureView.TAG;
//import static com.populace.gacvbconference.GTCCameraActivity.TAG;

/**
 * Created by  on 01/02/17.
 */

public class RegistrationIntentService extends IntentService {
    public RegistrationIntentService() {
        super(TAG);
    }
    // ...

    @Override
    public void onHandleIntent(Intent intent) {
        // ...
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken("257333558128",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // ...
    }

    private void sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed

        String apikey = "AIzaSyAnsmN-yo3AdzJS3ZLCrlFKCRopsQzkC4I";
        App42API.initialize(this.getApplicationContext(), "c7e20064acb971e98ba24177b749aa2bce08422a2c5df7bad331e52b70fb9266", "99262c5730a79e75f12343cda21ec753bf40bf2787b48d1a9a5034bc8c54b004");
        final PushNotificationService pushNotificationService = App42API.buildPushNotificationService();
        //PushNotificationService pushNotificationService = App42API.buildPushNotificationService();
        pushNotificationService.uploadApiKey(apikey, new App42CallBack() {
            public void onSuccess(Object response) {
                PushNotification pushNotification = (PushNotification) response;

                //  App42API.initialize("ANDROID_APPLICATION_CONTEXT","API_KEY","SECRET_KEY");
                // PushNotificationService pushNotificationService = App42API.buildPushNotificationService();
                pushNotificationService.storeDeviceToken(token, token, new App42CallBack() {
                    public void onSuccess(Object response) {
                        PushNotification pushNotification = (PushNotification) response;
                        System.out.println("userName is " + pushNotification.getUserName());
                        System.out.println("DeviceToken is " + pushNotification.getDeviceToken());

                        pushNotificationService.subscribeToChannel("BerryCollege", token, new App42CallBack() {
                            @Override
                            public void onSuccess(Object o) {
                                PushNotification pushNotification = (PushNotification) o;
                                System.out.println("UserName is " + pushNotification.getUserName());
                                ArrayList<PushNotification.Channel> channelList = pushNotification.getChannelList();
                                System.out.print("Recieved push notification");
                                for (PushNotification.Channel channelObj : channelList) {
                                    System.out.println("channelName is " + channelObj.getName());
                                }
                            }

                            @Override
                            public void onException(Exception e) {
                                System.out.println("Exception Message" + e.getMessage());
                            }
                        });
                    }

                    public void onException(Exception ex) {
                        System.out.println("Exception Message" + ex.getMessage());
                    }
                });

                //InstanceID instanceID = InstanceID.getInstance(this);
                System.out.println(pushNotification);
            }

            public void onException(Exception ex) {
                System.out.println("Exception Message" + ex.getMessage());
            }
        });
    }


    // ...
}