package project.heko;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import project.heko.helpers.NetworkHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent intent;
            if (NetworkHelper.isNetworkConnected(this)) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, ErrorScreenActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }

}