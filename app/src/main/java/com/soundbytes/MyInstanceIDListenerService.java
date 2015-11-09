package com.soundbytes;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Olumide on 11/1/2015.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        //TODO
    }
}
