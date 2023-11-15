package project.heko.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import project.heko.databinding.LightnovelDetailBinding;
import project.heko.models.Book;

public class LnFragment extends Fragment {

    private LightnovelDetailBinding binding;
    private LnViewModel lnViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lnViewModel = new ViewModelProvider(this).get(LnViewModel.class);
        binding = LightnovelDetailBinding.inflate(inflater, container, false);

/*        final TextView textView = binding.lnTitle;
        lnViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBook();
        lnViewModel.getBook().observe(getViewLifecycleOwner(), book -> {
            binding.lnTitle.setText(book.getTitle());
            String[] genres = book.getGenre().split(";");
            for (String i : genres) {
                Chip chip = new Chip(this.getContext());
                chip.setText(i);
                binding.groupGenre.addView(chip);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getBook(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("books").document("O5GqxRrBXPmNw15lyfQG");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("B", "DocumentSnapshot data: " + document.getData());
                    DocumentSnapshot snap = task.getResult();
                    Book book = snap.toObject(Book.class);
                    lnViewModel.getBook().setValue(book);
                    assert book != null;
                    String[] gens = book.getGenre().split(";");
                    Log.i("Test",gens[0]);
                } else {
                    Log.d("B", "No such document");
                }
            } else {
                Log.d("B", "get failed with ", task.getException());
            }
        });
    }

}