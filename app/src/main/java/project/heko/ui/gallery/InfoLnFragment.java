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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import project.heko.R;
import project.heko.databinding.FragmentInfoLnBinding;
import project.heko.models.Book;
import project.heko.models.BookShelf;

public class InfoLnFragment extends Fragment {

    private FragmentInfoLnBinding binding;
    private LnViewModel lnViewModel;
    private String id;
    private FirebaseFirestore db;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private int count;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lnViewModel = new ViewModelProvider(this).get(LnViewModel.class);
        binding = FragmentInfoLnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if(getArguments() != null) {
            id = String.valueOf(getArguments().getString("id"));
            Log.i("dona", id);
        }
        getBook();
        lnViewModel.getBook().observe(getViewLifecycleOwner(), book -> {
            binding.lnTitle.setText(book.getTitle());
            binding.txtLastUpdate.setText(formatDate(book.getLast_update().toDate()));
            binding.txtAuthor.setText(book.getAuthor());
            if (!book.getBook_cover().equals("")){
                Picasso.get().load(book.getBook_cover()).into(binding.lnCover);
            }
            binding.txtContent.setText(book.getDescription());
        });
        isFollowed();
        if(Boolean.TRUE.equals(lnViewModel.getId().getValue())){
            binding.follow.setImageResource(R.drawable.ic_heart);
        }
        else binding.follow.setImageResource(R.drawable.ic_heart_outline);
        lnViewModel.getId().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                Follow();
            }
            else {
                unFollow();
            }
        });
        showFollow();
        gotoListVolume();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getBook(){
        db.collection("books")
                .document(id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("B", "DocumentSnapshot data: " + document.getData());
                            DocumentSnapshot snap = task.getResult();
                            Book book = snap.toObject(Book.class);
                            lnViewModel.getBook().setValue(book);
                            assert book != null;
                            String[] genres = book.getGenre().split(";");
                            for (String i : genres) {
                                Chip chip = new Chip(this.getContext());
                                chip.setText(i);
                                binding.groupGenre.addView(chip);
                            }
                        } else {
                            Log.d("B", "No such document");
                        }
                    }
                });
    }
    @NonNull
    private String formatDate(Date date){
        Date currentDate = new Date();
        long difference = currentDate.getTime() - date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(difference);
        int differenceYears = c.get(Calendar.YEAR)-1970;
        int differenceHours = c.get(Calendar.HOUR);
        int differenceMonth = c.get(Calendar.MONTH);
        int differenceDays = c.get(Calendar.DAY_OF_MONTH)-1;
        int differenceWeek = (c.get(Calendar.DAY_OF_MONTH)-1)/7;
        int differenceMinutes = c.get(Calendar.MINUTE);// ** if you use this, change the mDay to (c.get(Calendar.DAY_OF_MONTH)-1)%7
        String dif = differenceYears + "năm";
        if(differenceYears == 0){
            dif = differenceMonth + " tháng";
            if(differenceMonth == 0){
                dif = differenceWeek + " tuần";
                if(differenceWeek == 0){
                    dif = differenceDays +" ngày";
                    if (differenceDays == 0){
                        dif = differenceHours + " giờ";
                        if(differenceHours == 0){
                            dif = differenceMinutes +" phút";
                        }
                    }
                }
            }
        }
        return dif+" trước";
    }
    private void isFollowed(){
        //AggregateQuery count = db.collection("bookShelf").whereEqualTo("book_id", Objects.requireNonNull(lnViewModel.getBook().getValue()).getId()).whereEqualTo("user_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).count();
        Query query = db.collection("bookShelf")
                .whereEqualTo("book_id", id)
                .whereEqualTo("user_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d("Bien", "Count: " + snapshot.getCount());
                count = (int) snapshot.getCount();
                if (count == 0) {
                    lnViewModel.getId().setValue(false);
                } else {
                    lnViewModel.getId().setValue(true);
                }
            }
        });
    }
    private void showFollow(){
        binding.follow.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(lnViewModel.getId().getValue())){
                lnViewModel.getId().setValue(false);
                Log.i("Clicked","Unfollow");
            }
            else{
                lnViewModel.getId().setValue(true);
                Log.i("Clicked", "Follow");
            }
        });
    }
    private void Follow(){
        BookShelf bookShelf = new BookShelf(id, new Timestamp(new Date()), 0, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        db.collection("bookShelf")
                .add(bookShelf)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Add", "DocumentSnapshot written with ID: " + documentReference.getId());
                    binding.follow.setImageResource(R.drawable.ic_heart);
                })
                .addOnFailureListener(e -> lnViewModel.getId().setValue(false));
    }
    private void unFollow(){
        db.collection("bookShelf")
                .whereEqualTo("book_id", id)
                .whereEqualTo("user_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("GetSus", document.getId() + " => " + document.getData());
                            db.collection("bookShelf").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        Log.d("DeleteS", "DocumentSnapshot successfully deleted!");
                                        binding.follow.setImageResource(R.drawable.ic_heart_outline);
                                    })
                                    .addOnFailureListener(e -> Log.w("DeleteE", "Error deleting document", e));
                        }
                    } else {
                        Log.d("GetEr", "Error getting documents: ", task.getException());
                    }
                });
    }
    private void gotoListVolume(){
        binding.icList.setOnClickListener(v -> {
            if (getParentFragment() != null) {
                LnFragment parentFragment = (LnFragment) getParentFragment();
                parentFragment.setFragmentVolume();

            } else {
                Log.i("Child", "Loi");
            }
        })  ;
    }
}