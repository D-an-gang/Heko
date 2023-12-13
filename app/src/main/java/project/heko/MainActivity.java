package project.heko;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import project.heko.auth.LoginActivity;
import project.heko.databinding.ActivityMainBinding;
import project.heko.models.User;
import project.heko.ui.chapter.SettingViewModel;
import project.heko.ui.dialogs.FontSettingDialog;
import project.heko.ui.navHeader.navHeaderViewModel;

public class MainActivity extends AppCompatActivity implements FontSettingDialog.FontDialogListener {

    private AppBarConfiguration mAppBarConfiguration;

    private navHeaderViewModel model;
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;
    private SettingViewModel set_font;
    FontSettingDialog dialog;

    public navHeaderViewModel getModel() {
        return model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        initFloatingButton();
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        liveDataInit(navigationView);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home
                , R.id.nav_bookshelf
        )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //Neu muon set nut home luon ve nha !!
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home && navController.getCurrentBackStack().getValue().get(1).getDestination().getId() == R.id.nav_home) {
                navController.popBackStack(R.id.nav_home, false);
            }
            drawer.closeDrawers();
            return onOptionsItemSelected(item);
        });
        //set up login activity result
        ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            int resCode = o.getResultCode();
            Intent data = o.getData();

            if (resCode == RESULT_OK && data != null) {
                try {
                    Bundle extras = data.getExtras();
                    if (extras != null && extras.getString("id") != null) {
                        User user = new User(Objects.requireNonNull(extras.getString("username")), extras.getString("email"), extras.getString("img"), extras.getString("id"));
                        model.getUser().setValue(user);
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        model.getUser().setValue(new User());
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_norm), Toast.LENGTH_SHORT).show();
                    model.getUser().setValue(new User());
                }
            }
        });
        //initialize login on click listener
        authMethodInit(navigationView, loginLauncher, navController);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        initSetFont();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mapUser(currentUser);
        } else {
            model.getUser().setValue(new User());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void liveDataInit(@NonNull NavigationView navView) {
        model = new ViewModelProvider(this).get(navHeaderViewModel.class);
        View nav = navView.getHeaderView(0);
        TextView username = nav.findViewById(R.id.username);
        TextView email = nav.findViewById(R.id.email);
        ImageView img = nav.findViewById(R.id.imageView);
        // Create the observer which updates the UI.
        final Observer<User> nameObserver = newUser -> {
            // Update the UI, in this case, a TextView.
            if (newUser.getId() != null) {
                username.setText(newUser.getUsername());
                email.setText(newUser.getEmail());
                Log.i("changed", " HARRY!!!");
                Picasso.get().load(newUser.getImgUrl()).fit().into(img);
            } else {
                username.setText(R.string.nav_header_title);
                email.setText(R.string.nav_header_subtitle);
                img.setImageResource(R.mipmap.ic_launcher);
            }
        };
        model.getUser().observe(this, nameObserver);
    }

    private void authMethodInit(@NonNull NavigationView navView, ActivityResultLauncher<Intent> loginLauncher, NavController navController) {
        View nav = navView.getHeaderView(0);
        nav.setOnClickListener(e -> {
            if (Objects.requireNonNull(model.getUser().getValue()).getId() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                loginLauncher.launch(intent);
            } else {
                DrawerLayout mDrawerLayout = binding.drawerLayout;
                mDrawerLayout.closeDrawers();
                navController.navigate(R.id.profileFragment);
            }
        });
    }

    public void mapUser(@NonNull FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                final User res = new User();
                if (document.exists()) {
                    res.setUsername(Objects.requireNonNull(document.getString("username")));
                    res.setImgUrl(document.getString("imgUrl"));
                    res.setEmail(document.getString("email"));
                    res.setId(document.getId());
                    model.getUser().setValue(res);
                } else {
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, R.string.error_user_not_found, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, R.string.error_norm, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void initSetFont() {
        set_font = new ViewModelProvider(this).get(SettingViewModel.class);
    }

    private void initFloatingButton() {
        binding.appBarMain.fab.setOnClickListener(view -> {
            if (dialog != null
                    && dialog.getDialog() != null
                    && dialog.getDialog().isShowing()
                    && !dialog.isRemoving()) {
                //dialog is showing so do something
                dialog.dismiss();
            } else {
                //dialog is not showing
                showDialog();
            }
        });
    }

    @Override
    public void onDialogPositiveClick(FontSettingDialog dialog) {
        try {
            Log.i("XX", "font:" + dialog.font_id + " | size: " + dialog.size);
            SharedPreferences x = getPreferences(Context.MODE_PRIVATE);
            Log.i("XX", "SP font:" + x.getInt(getString(R.string.font_family_id), -1) + " | size: " + x.getFloat(getString(R.string.text_size), -1));
        } catch (Exception ignored) {
        }
    }

    private void showDialog() {
        dialog = new FontSettingDialog();
        dialog.show(getSupportFragmentManager(), "Font Setting");
    }
}