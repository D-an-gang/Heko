package project.heko.ui.bookShelf;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import project.heko.databinding.FragmentBookShelfBinding;
import project.heko.dto.BookShelfDto;

public class BookShelfFragment extends Fragment {


    private FragmentBookShelfBinding binding;
    private BookShelfAdapter bookShelfAdapter;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookShelfBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("user", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        binding.txtUsername.setText("Tài khoản: " + mAuth.getCurrentUser().getEmail());
        getData();
        GetBanner();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("bookShelf")
                .whereEqualTo("user_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).orderBy("last_update", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BookShelfDto> options = new FirestoreRecyclerOptions.Builder<BookShelfDto>()
                .setQuery(query, snapshot -> {
                    BookShelfDto dto = new BookShelfDto();
                    dto.setUnread(snapshot.getLong("unread"));
                    dto.setDocId(snapshot.getId());
                    db.collection("books").document(snapshot.getString("book_id"))
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    dto.setCover(task.getResult().getString("book_cover"));
                                    dto.setTitle(task.getResult().getString("title"));
                                    dto.setId(task.getResult().getString("id"));
                                    bookShelfAdapter.notifyDataSetChanged();
                                }
                            });
                    return dto;
                }).build();
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookShelfAdapter = new BookShelfAdapter(options,Navigation.findNavController(requireView()));
        recyclerView.setAdapter(bookShelfAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        bookShelfAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        bookShelfAdapter.stopListening();
    }
    private void GetBanner(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("home").orderBy("create_at", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               Picasso.get().load(task.getResult().getDocuments().get(0).getString("imgUrl")).fit().centerCrop().into(binding.banner);
           }
        });
    }
}