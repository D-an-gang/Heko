package project.heko.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.heko.R;
import project.heko.databinding.ActivitySignupBinding;
import project.heko.models.User;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private boolean hasUploadedPFP;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        //initialize firestore
        db = FirebaseFirestore.getInstance();
        //image uploader initialization
        hasUploadedPFP = false;
        imageUploaderInit();
        //submit btn init
        submitButtonInit();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //kHI NGUOI DUNG QUAY TRO LAI LOGIN
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                cancelActivity();
            }
        });
        backbtnInit();
    }


    private void signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("signup success", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (hasUploadedPFP) {
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();
                                // Create a reference to "mountains.jpg"
                                StorageReference usersPicRef = storageRef.child(user.getUid() + ".jpg");
                                // Create a reference to 'images/mountains.jpg'
                                //StorageReference usersPicImagesRef = storageRef.child("usersPhotos/" + user.getUid() + ".jpg");
                                // Get the data from an ImageView as bytes
                                imageView.setDrawingCacheEnabled(true);
                                imageView.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = usersPicRef.putBytes(data);
                                uploadTask.addOnFailureListener(exception -> {
                                    // Handle unsuccessful uploads
                                }).addOnSuccessListener(taskSnapshot -> {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                            .addOnSuccessListener(uri -> {
                                                User user1 = makeUser(FirebaseAuth.getInstance().getCurrentUser(), binding.txtUsername.getText().toString().trim(), uri.toString());
                                                recordUser(user1);
                                            })
                                            .addOnFailureListener(e -> {
                                                FirebaseUser user12 = FirebaseAuth.getInstance().getCurrentUser();
                                                assert user12 != null;
                                                recordUser(new User(binding.txtUsername.getText().toString().trim(), user12.getEmail(), user12.getUid()));
                                            });
                                });
                            }
                            else {
                                User newUser = makeUser(user, binding.txtUsername.getText().toString().trim(), null);
                                recordUser(newUser);
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("signup failed", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, R.string.signup_failed, Toast.LENGTH_SHORT).show();
                        endActivity(new User());
                    }
                });
    }

    private void submitButtonInit() {
        binding.btnSubmit.setOnClickListener(event -> {
            TextView email = binding.txtEmail;
            TextView password = binding.txtPw;
            TextView password_confirm = binding.txtPwCon;
            if (validate(email, password, password_confirm)) {
                signup(email.getText().toString().trim(), password.getText().toString());
            }
        });
    }

    public boolean validate(@NonNull TextView email, TextView password, TextView pw_con) {
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
        if (!password.getText().toString().equals(pw_con.getText().toString())) {
            password.setError(getResources().getString(R.string.pw_not_match));
            pw_con.setError(getResources().getString(R.string.pw_not_match));
            Toast.makeText(this, R.string.pw_not_match, Toast.LENGTH_SHORT).show();
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

    //image picking
    private void imageUploaderInit() {
        imageView = binding.loginLogo;
        imageView.setOnClickListener(e -> imageCropper());
    }


    private void imageCropper() {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = true;
        cropImageOptions.imageSourceIncludeCamera = true;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(null, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getApplicationContext(), true));
            imageView.setPadding(0,0,0,0);
            imageView.setImageBitmap(cropped);
            hasUploadedPFP = true;
        } else {
            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    });

    //create user model to push to firebase
    private User makeUser(FirebaseUser tar, String username, String imageUrl) {

        return (imageUrl == null) ?
                    new User(username, tar.getEmail(), tar.getUid())
                    : new User(username, tar.getEmail(), imageUrl, tar.getUid());
    }

    //insert user info into Firestore
    //this function will end Activity on Complete or Failure
    private void recordUser(@NonNull User user) {
        assert user.getId() != null;
        db.collection("users").document(user.getId()).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, R.string.signup_success, Toast.LENGTH_SHORT).show();
                    endActivity(user);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, R.string.signup_failed, Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    endActivity(new User());
                });
    }

    private void backbtnInit() {
        binding.backBtn.setOnClickListener(e -> cancelActivity());

    }

    private void cancelActivity() {
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }
}