package com.notificator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class NotificatorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(NotificatorActivity.this, LoginView.class));
        Utils.orders_loaded = false;
        Utils.init(this);
        finish();
    }
}