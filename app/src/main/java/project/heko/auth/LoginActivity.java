package project.heko.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import project.heko.R;
import project.heko.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setContentView(binding.getRoot());
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        String mystring= getResources().getString(R.string.login_signup_link);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        binding.signupLink.setText(content);
    }
}