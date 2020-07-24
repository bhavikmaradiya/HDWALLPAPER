package com.collabcreations.hdwallpaper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.collabcreations.hdwallpaper.Interface.OnUserFetchedListener;
import com.collabcreations.hdwallpaper.Modal.User;
import com.collabcreations.hdwallpaper.R;
import com.collabcreations.hdwallpaper.Tasks.GetUserByIdTask;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity implements OnUserFetchedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            GetUserByIdTask task = new GetUserByIdTask(getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(), this);
            task.execute();
        } else {
            start();
        }

    }

    private void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }, 5000);
    }


    @Override
    public void onUserFetchComplete(boolean isSuccessful, User user, int responseCode) {
        start();
    }
}
