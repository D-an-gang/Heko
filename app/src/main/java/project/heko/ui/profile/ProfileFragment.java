package project.heko.ui.profile;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import project.heko.MainActivity;
import project.heko.R;
import project.heko.databinding.FragmentProfileBinding;
import project.heko.helpers.UItools;
import project.heko.models.User;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FragmentProfileBinding binding;
    private String old_username;
    private String old_email;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            //exit the fragment if theres no user logged in
            endFragment();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.txtProfileUsername.setText(user.getUsername());
            binding.txtProfileEmail.setText(user.getEmail());
        });
        //hide user panel first
        //get user data
        getUser();
        //init logout btn
        logoutButtonListener();
        updateProfileListener();
        updatePasswordListener();
    }

    private void logoutButtonListener() {
        binding.logoutBtn.setOnClickListener(e -> {
            toggleLoading(true);
            mAuth.signOut();
            MainActivity mainAct = (MainActivity) getActivity();
            assert mainAct != null;
            mainAct.getModel().getUser().setValue(new User());
            endFragment();
        });
    }

    public void endFragment() {
        toggleLoading(false);
        NavHostFragment.findNavController(this).popBackStack();
    }

    private void cancelFragment(int i, boolean shouldLogout) {
        if (shouldLogout) mAuth.signOut();
        UItools.toast(requireActivity(), getResources().getString(i));
        endFragment();
    }

    private void toggleLoading(boolean mode) {
        if (mode)
            binding.loader.progressOverlay.setVisibility(View.VISIBLE);
        else
            binding.loader.progressOverlay.setVisibility(View.GONE);
    }

    private void getUser() {
        toggleLoading(true);
        if (mAuth.getCurrentUser() != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //parse data onto view
                        try {
                            User res = User.user_mapper(document);
                            mViewModel.getUser().setValue(res);
                            old_username = res.getUsername();
                            old_email = res.getEmail();
                        } catch (NullPointerException e) {
                            Log.i("fetch failed", Objects.requireNonNull(e.getMessage()));
                            cancelFragment(R.string.error_user_not_found, true);
                        }
                        //display user panel
                    } else {
                        cancelFragment(R.string.error_user_not_found, true);
                    }
                } else {
                    //get failed--->end frag ||-->> redirect to error page
                    cancelFragment(R.string.error_user_not_found, false);
                }
                toggleLoading(false);
            });
        } else {
            endFragment();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void updatePasswordListener() {
        binding.textChangePassword.setOnClickListener(v -> {
                    toggleLoading(true);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(old_email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.profile_email_sent, Toast.LENGTH_SHORT).show();
                                }
                            });
                    binding.textChangePassword.setTextColor(R.color.teal_700);
                    binding.icPassword.setColorFilter(R.color.teal_700);
                    toggleLoading(false);
                }
        );
    }

    @SuppressLint("InflateParams")
    private void updateProfileListener() {
        binding.textChangeInfo.setOnClickListener(v -> {
                    toggleLoading(true);
                    final Dialog dialog = new Dialog(requireActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_dialog_username);
                    Window window = dialog.getWindow();
                    if (window == null) {
                        return;
                    }
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams wA = window.getAttributes();
                    wA.gravity = Gravity.CENTER;
                    window.setAttributes(wA);
                    EditText new_username = dialog.findViewById(R.id.dialog_username);
                    Button btnOK = dialog.findViewById(R.id.btn_dialog_ok);
                    Button btnCancel = dialog.findViewById(R.id.btn_dialog_cancel);
                    btnOK.setOnClickListener(v1 -> {
                        if (!old_username.equals(new_username.getText().toString()) && !new_username.getText().toString().equals("")) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                            userRef
                                    .update("username", new_username.getText().toString())
                                    .addOnSuccessListener(aVoid -> {
                                        UItools.toast(requireActivity(), getString(R.string.profile_success_change_username));
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e -> {
                                        UItools.toast(requireActivity(), getString(R.string.profile_update_fail));
                                        dialog.dismiss();
                                    });
                        } else {
                            UItools.toast(requireActivity(), getString(R.string.profile_username_null));
                        }
                        Toast.makeText(getActivity(), new_username.getText().toString(), Toast.LENGTH_SHORT).show();
                    });
                    btnCancel.setOnClickListener(v1 ->
                            dialog.dismiss()
                    );
                    dialog.show();
                    toggleLoading(false);
                }
        );
    }
}