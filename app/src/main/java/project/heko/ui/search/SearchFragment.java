package project.heko.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import project.heko.databinding.FragmentSearchBinding;
import project.heko.models.SearchResult;

public class SearchFragment extends Fragment {
    public FragmentSearchBinding binding;
    private RecyclerView searchResult;
    private Query query;
    private FirebaseFirestore db;
    private SearchAdapter adapter;
    private FirestoreRecyclerOptions<SearchResult> options;
    private SearchQueryModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        query = db.collection("_search").whereGreaterThanOrEqualTo("key", "").orderBy("key", Query.Direction.DESCENDING);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView searchView = binding.searchBar;
        searchResult = binding.searchRes;
        searchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SearchQueryModel.class);
        viewModel.getQuery().observe(getViewLifecycleOwner(), query -> {
            adapter.stopListening();
            this.query = query;
            options = new FirestoreRecyclerOptions.Builder<SearchResult>().setQuery(query, SearchResult.class).build();
            adapter.updateOptions(options);
            adapter.startListening();
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query target = db.collection("_search").whereGreaterThanOrEqualTo("key", query).orderBy("key");
                viewModel.getQuery().setValue(target);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void initAdapter() {
        options = new FirestoreRecyclerOptions.Builder<SearchResult>().setQuery(query, SearchResult.class).build();
        adapter = new SearchAdapter(options);
        searchResult.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}