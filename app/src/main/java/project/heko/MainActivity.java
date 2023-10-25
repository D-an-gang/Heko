package project.heko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
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
import com.google.android.material.snackbar.Snackbar;

import project.heko.auth.LoginActivity;
import project.heko.databinding.ActivityMainBinding;
import project.heko.models.User;
import project.heko.ui.navHeader.navHeaderViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private navHeaderViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project.heko.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        liveDataInit(navigationView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //set up login activity result
        ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                int resCode = o.getResultCode();
                Intent data = o.getData();

                if (resCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null || extras.getString("id") != null) {
                        User user = new User(extras.getString("username"), extras.getString("email"), extras.getString("img"), extras.getString("id"));
                        model.getUser().setValue(user);
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        model.getUser().setValue(new User());
                    }
                }
            }
        });
        //initialize login on click listener
        authMethodInit(navigationView, loginLauncher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                //change img
            } else {
                username.setText(R.string.nav_header_title);
                email.setText(R.string.nav_header_subtitle);
                img.setImageResource(R.mipmap.ic_launcher);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getUser().observe(this, nameObserver);
    }

    private void authMethodInit(@NonNull NavigationView navView, ActivityResultLauncher<Intent> loginLauncher) {
        View nav = navView.getHeaderView(0);
        nav.setOnClickListener(e -> {
            if (model.getUser().getValue().getId() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                loginLauncher.launch(intent);
            } else {
                //TODO user profile page
            }
        });
    }
}