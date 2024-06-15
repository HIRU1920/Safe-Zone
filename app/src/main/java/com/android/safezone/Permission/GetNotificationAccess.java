package com.android.safezone.Permission;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.safezone.Activity.MainActivity;
import com.android.safezone.R;
import com.android.safezone.View.ExpandableTextView;

public class GetNotificationAccess extends AppCompatActivity {
    Button finishButton;
    TextView status;
    ExpandableTextView expandableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_notification_access);

        expandableTextView = findViewById(R.id.expandableTextViewNoti);
        status = findViewById(R.id.noti_per);
        finishButton = findViewById(R.id.grant_noti_access);
        granted();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        granted();
    }

    @SuppressLint("SetTextI18n")
    public void granted() {
        if (Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {
            status.setText("Permission Granted");
            finishButton.setVisibility(View.GONE);
        }
    }

    public void getNotificationAccess(View view) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    public void toggle(View v) {
        expandableTextView.toggle();
    }

    public void done(View view) {
        SharedPreferences sharedPref = getSharedPreferences("achievix", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("firstTime", "no");
        editor.apply();

        Intent intent = new Intent(GetNotificationAccess.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
