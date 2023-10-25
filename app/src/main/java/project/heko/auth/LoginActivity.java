package project.heko.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.heko.R;
import project.heko.databinding.ActivityLoginBinding;
import project.heko.models.User;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setContentView(binding.getRoot());
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        //In gach chan nut sign up
        String mystring= getResources().getString(R.string.login_signup_link);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        binding.signupLink.setText(content);
        //back pressed -- Khi nguoi dung cancel
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                cancelActivity();
            }
        });
        //submit--khi nguoi dung bam dang nhap
        binding.btnSubmit.setOnClickListener(e->{
            //TODO dang nhap
        });
    }

    public static boolean validateEmailAndPassword(String email, String password) {
        boolean isEmailValid = validateEmail(email);
        boolean isPasswordValid = validatePassword(password);

        return isEmailValid && isPasswordValid;
    }

    private static boolean validateEmail(String email) {
        // Email regex pattern for basic validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private static boolean validatePassword(String password) {
        return password.length() > 6;
    }

    private void cancelActivity(){
        Intent intent = new Intent();
        User user= new User();
        Bundle bundle = new Bundle();
        bundle.putString("username", user.getUsername());
        bundle.putString("img", user.getImgUrl());
        bundle.putString("email", user.getEmail());
        bundle.putString("id", user.getId());
        intent.putExtras(bundle);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}