package project.heko.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;

import project.heko.databinding.FragmentHomeBinding;
import project.heko.dto.HomePreviewDto;

public class HomeFragment extends Fragment {
    private static final int PAGE_SIZE = 2;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    HomePageRecycleAdapter recyclerViewAdapter;
    ArrayList<HomePreviewDto> rowsArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        rowsArrayList = new ArrayList<>();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerView;
        populateData();
        initAdapter();
        initScrollListener();
    }

    private void populateData() {
        //TODO fetch data
        Query ref = db.collection("books").orderBy("last_update", Query.Direction.DESCENDING).limit(PAGE_SIZE);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot x : task.getResult()) {
                    HomePreviewDto item = x.toObject(HomePreviewDto.class);
//                    HomePreviewDto item = new HomePreviewDto(x.getString("author"), x.getString("book_cover"), x.get);
                    rowsArrayList.add(item);
                }
            }
        });

    }

    private void initAdapter() {
        recyclerViewAdapter = new HomePageRecycleAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.remove(rowsArrayList.size() - 1);
        int scrollPosition = rowsArrayList.size();
        recyclerViewAdapter.notifyItemRemoved(scrollPosition);
        int currentSize = scrollPosition;
        int nextLimit = currentSize + PAGE_SIZE;

        //TODO fetch more data
        while (currentSize - 1 < nextLimit) {
//        rowsArrayList.add("Item " + currentSize);
            currentSize++;
        }
//      recyclerViewAdapter.notifyDataSetChanged();
        recyclerViewAdapter.notifyItemRangeInserted(scrollPosition, PAGE_SIZE - 1);
        isLoading = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}