/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rti.rcd.ict.lgug;

import org.rti.rcd.ict.lgug.c2dm.Config;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.c2dm.C2DMBaseReceiver;

/**
 * Broadcast receiver that handles Android Cloud to Data Messaging (AC2DM) messages, initiated
 * by the JumpNote App Engine server and routed/delivered by Google AC2DM servers. The
 * only currently defined message is 'sync'.
 * Code from the Google AC2DM Jumpnote example.
 */
public class C2DMReceiver extends C2DMBaseReceiver {
    static final String TAG = Config.makeLogTag(C2DMReceiver.class);
    
    private static final int HELLO_ID = 1;

    public C2DMReceiver() {
        super(Config.C2DM_SENDER);
    }
    
    @Override
    public void onRegistered(Context context, String registrationId) {
        //DeviceRegistrar.registerWithServer(context, registration);
    	Log.d(TAG, "registrationID: " + registrationId);
    	CoconutActivity c = CoconutActivity.getRef();
        Account acc = c.getSelectedAccount();
        Log.d( TAG, "onRegistered() sendRegistrationId" );
        NetworkCommunication.sendRegistrationId( acc, context, registrationId );
        Log.d( TAG, "onRegistered() p.onRegistered()" );
        c.onRegistered();
        Log.d( TAG, "onRegistered() done" );
    }

    @Override
    public void onUnregistered(Context context) {
//        SharedPreferences prefs = Prefs.get(context);
//        String deviceRegistrationID = prefs.getString("deviceRegistrationID", null);
//        DeviceRegistrar.unregisterWithServer(context, deviceRegistrationID);
    	Log.d(TAG, "Unregistered app.");
    }

    @Override
    public void onError(Context context, String errorId) {
//        Toast.makeText(context, "Messaging registration error: " + errorId,
//                Toast.LENGTH_LONG).show();
        Log.d(TAG, "Messaging registration error: " + errorId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String accountName = intent.getExtras().getString(Config.C2DM_ACCOUNT_EXTRA);
        String message = intent.getExtras().getString(Config.C2DM_MESSAGE_EXTRA);
        Log.d(TAG, "Messaging request received for account " + accountName);
        Log.d(TAG, "Message: " + message);
//        CoconutActivity c = CoconutActivity.getRef();
//        c.displayMessage( message );
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.icon;
        CharSequence tickerText = "KIMS";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        
        //Context context = getApplicationContext();
        CharSequence contentTitle = "New KIMS Message";
        //CharSequence contentText = "Hello World!";
        Intent notificationIntent = new Intent(this, CoconutActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, message, contentIntent);

        mNotificationManager.notify(HELLO_ID, notification);
    }

    /**
     * Register or unregister based on phone sync settings.
     * Called on each performSync by the SyncAdapter.
     */
    public static void refreshAppC2DMRegistrationState(Context context) {
        // Determine if there are any auto-syncable accounts. If there are, make sure we are
        // registered with the C2DM servers. If not, unregister the application.
//        boolean autoSyncDesired = false;
//        if (ContentResolver.getMasterSyncAutomatically()) {
//            AccountManager am = AccountManager.get(context);
//            Account[] accounts = am.getAccountsByType(SyncAdapter.GOOGLE_ACCOUNT_TYPE);
//            for (Account account : accounts) {
//                if (ContentResolver.getIsSyncable(account, JumpNoteContract.AUTHORITY) > 0 &&
//                        ContentResolver.getSyncAutomatically(account, JumpNoteContract.AUTHORITY)) {
//                    autoSyncDesired = true;
//                    break;
//                }
//            }
//        }
//
//        boolean autoSyncEnabled = !C2DMessaging.getRegistrationId(context).equals("");
//
//        if (autoSyncEnabled != autoSyncDesired) {
//            Log.i(TAG, "System-wide desirability for JumpNote auto sync has changed; " +
//                    (autoSyncDesired ? "registering" : "unregistering") +
//                    " application with C2DM servers.");
//
//            if (autoSyncDesired == true) {
//                C2DMessaging.register(context, Config.C2DM_SENDER);
//            } else {
//                C2DMessaging.unregister(context);
//            }
//        }
    }
}
