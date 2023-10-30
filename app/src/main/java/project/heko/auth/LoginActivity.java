package project.heko.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.heko.R;
import project.heko.databinding.ActivityLoginBinding;
import project.heko.models.User;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        //In gach chan nut sign up
        String mystring = getResources().getString(R.string.login_signup_link);
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
        //set up firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //submit--khi nguoi dung bam dang nhap
        binding.btnSubmit.setOnClickListener(e -> {
            TextView email = binding.txtEmail;
            TextView password = binding.txtPw;
            if (validate(email, password)) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("auth success", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user == null) {
                                    cancelActivity();
                                } else {
                                    mapUser(user);

                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("auth failed", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                cancelActivity();
                            }
                        });
            }

        });
        //init nut dang ky tai khoan
        signupBtnInit();
    }

    public boolean validate(TextView email, TextView password) {
        if (email.getText().toString().trim().equals("")) {
            email.setError(getResources().getString(R.string.login_email_empty_prompt));
            Toast.makeText(this, R.string.login_email_empty_prompt, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().trim().equals("")) {
            password.setError(getResources().getString(R.string.login_password_empty_prompt));
            Toast.makeText(this, R.string.login_password_empty_prompt, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validateEmail(email.getText().toString())) {
            email.setError(getResources().getString(R.string.invalid_email));
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validatePassword(password.getText().toString())) {
            password.setError(getResources().getString(R.string.invalid_password));
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        // Email regex pattern for basic validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        return password.length() >= 6;
    }

    private void cancelActivity() {
        Intent intent = new Intent();
        Bundle bundle = loadBundle(new User());
        intent.putExtras(bundle);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    public void endActivity(User user) {
        Intent intent = new Intent();
        Bundle bundle = loadBundle(user);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public Bundle loadBundle(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("username", user.getUsername());
        bundle.putString("img", user.getImgUrl());
        bundle.putString("email", user.getEmail());
        bundle.putString("id", user.getId());
        return bundle;
    }

    public void mapUser(FirebaseUser user) {
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    final User res = new User();
                    if (document.exists()) {
                        res.setUsername(document.getString("username"));
                        res.setImgUrl(document.getString("imgUrl"));
                        res.setEmail(document.getString("email"));
                        res.setId(document.getId());
                        endActivity(res);
                    } else {
                        mAuth.signOut();
                        Toast.makeText(LoginActivity.this, R.string.error_user_not_found, Toast.LENGTH_SHORT).show();
                        cancelActivity();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, R.string.error_norm, Toast.LENGTH_SHORT).show();
                    cancelActivity();
                }
            }
        });
    }

    public void signupBtnInit(){
        binding.signupLink.setOnClickListener(e->{
            binding.txtEmail.setText("");
            binding.txtPw.setText("");
            Intent intent = new Intent(this, SignupActivity.class);
            signupLauncher.launch(intent);
        });
    }
    ActivityResultLauncher<Intent> signupLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            int resCode = o.getResultCode();
            Intent data = o.getData();
            if(resCode==RESULT_OK && data != null){
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    });
}