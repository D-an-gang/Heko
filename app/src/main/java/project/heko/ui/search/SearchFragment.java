package project.heko.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import project.heko.R;
import project.heko.databinding.FragmentSearchBinding;
import project.heko.models.SearchResult;

class SearchFragment extends Fragment {
    public FragmentSearchBinding binding;
    private SearchView searchView;
    private RecyclerView searchResult;
    private Query query;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<SearchResult, SearchHolder> adapter;
    private FirestoreRecyclerOptions<SearchResult> options;
    private SearchQueryModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = binding.searchBar;
        searchResult = binding.searchRes;
        searchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();

        viewModel = new ViewModelProvider(this).get(SearchQueryModel.class);
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


    private class SearchHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView author;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_title);
            author = itemView.findViewById(R.id.search_author);
        }
    }

    public class SearchQueryModel extends ViewModel {
        private final MutableLiveData<Query> query;

        public SearchQueryModel() {
            query = new MutableLiveData<>();
            query.setValue(db.collection("_search").whereGreaterThanOrEqualTo("key", "").orderBy("key", Query.Direction.DESCENDING));
        }

        private MutableLiveData<Query> getQuery() {
            return query;
        }
    }

    private void initAdapter() {
        options = new FirestoreRecyclerOptions.Builder<SearchResult>().setQuery(query, SearchResult.class).build();
        adapter = new FirestoreRecyclerAdapter<SearchResult, SearchHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull SearchResult model) {
                // Bind the Chat object to the ChatHolder
                String title = model.getDisplay().split("-")[0].trim();
                String author = model.getDisplay().split("-")[1].trim();
                holder.title.setText(title);
                holder.author.setText(author);
            }

            @NonNull
            @Override
            public SearchHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.search_list_item, group, false);
                return new SearchHolder(view);
            }


        };
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