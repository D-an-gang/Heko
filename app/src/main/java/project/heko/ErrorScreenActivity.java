package project.heko;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import project.heko.databinding.ActivityErrorScreenBinding;
import project.heko.helpers.NetworkHelper;

public class ErrorScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project.heko.databinding.ActivityErrorScreenBinding binding = ActivityErrorScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        binding.errRetryButton.setOnClickListener(v -> {
            if (NetworkHelper.isNetworkConnected(this)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(v, "Kiểm tra kết nối mạng và thử lại sau...", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}