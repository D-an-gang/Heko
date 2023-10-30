package project.heko.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
        });
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
        //hide user panel first
        binding.userPanel.setVisibility(View.GONE);
        //get user data
        getUser();
        //init logout btn
        logoutButtonListener();

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
            DocumentReference docRef = db.collection("cities").document("SF");
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //parse data onto view
                        try {
                            User res = User.user_mapper(document);
                            mViewModel.getUser().setValue(res);
                            binding.userPanel.setVisibility(View.VISIBLE);
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
}