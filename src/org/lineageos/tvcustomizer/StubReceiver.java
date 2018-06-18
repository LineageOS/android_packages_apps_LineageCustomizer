package org.lineageos.tvcustomize;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StubReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d("StubReceiver", "LineageCustomize.StubReceiver called");
    }
}
