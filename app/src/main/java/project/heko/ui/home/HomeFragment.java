package project.heko.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.heko.R;
import project.heko.databinding.FragmentHomeBinding;
import project.heko.dto.HomePreviewDto;
import project.heko.helpers.UItools;

public class HomeFragment extends Fragment {
    private static final int PAGE_SIZE = 2;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    HomePageRecycleAdapter recyclerViewAdapter;
    ArrayList<HomePreviewDto> rowsArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    HomeViewModel homeViewModel;
    Query fetch;
    DocumentSnapshot cursor;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cursor = null;
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rowsArrayList = new ArrayList<>();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerView;
        fetch = db.collection("books").orderBy("last_update", Query.Direction.DESCENDING);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getBanner().observe(getViewLifecycleOwner(), banner -> {
            binding.textHome.setText(banner.announcement);
            Picasso.get().load(banner.imgUrl).fit().centerCrop().into(binding.banner);
        });
        binding.rvProgressBar.setIndeterminate(true);
        homeViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.rvProgressBar.setVisibility(View.VISIBLE);
            } else {
                binding.rvProgressBar.setVisibility(View.GONE);
            }
        });
        homeViewModel.getLoading().setValue(false);
        populateData();
        initHome(homeViewModel);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void populateData() {
        //TODO fetch data
        fetch.limit(PAGE_SIZE).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cursor = task.getResult().getDocuments().get(task.getResult().size() - 1);
                for (DocumentSnapshot x : task.getResult()) {
                    HomePreviewDto item = x.toObject(HomePreviewDto.class);
                    x.getReference().collection("volume").orderBy("create_at", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task1 -> {
                        if (!task1.isSuccessful()) {
                            rowsArrayList.add(item);
                            recyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            DocumentSnapshot i = task1.getResult().getDocuments().get(0);
                            assert item != null;
                            item.setLatest_vol(i.getString("title"));
                            i.getReference().collection("chapters").orderBy("create_at", Query.Direction.ASCENDING).limit(1).get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    DocumentSnapshot z = task2.getResult().getDocuments().get(0);
                                    if (z.exists()) {
                                        item.setLatest_chap(z.getString("title"));
                                    }
                                }
                                rowsArrayList.add(item);
                                recyclerViewAdapter.notifyDataSetChanged();
                            });

                        }
                    });
                }
            }
        });
        initAdapter();
        initScrollListener();
    }

    private void initAdapter() {
        recyclerViewAdapter = new HomePageRecycleAdapter(rowsArrayList);
        GridLayoutManager grid = new GridLayoutManager(requireActivity(), 2);

        recyclerView.setLayoutManager(grid);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initScrollListener() {

        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (binding.scrollView.getHeight() + binding.scrollView.getScrollY()));
            if (diff == 0 && Boolean.FALSE.equals(homeViewModel.getLoading().getValue())) {
                // your pagination code
                loadMore();
            }
        });

    }

    private void loadMore() {
        if (cursor != null) {
//            //TODO figure it out
            HomePreviewDto temp = new HomePreviewDto();
            temp.setId(HomePreviewDto.NULL_KEY);
            rowsArrayList.add(temp);
            recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);
//            //-----------
            int scrollPosition = rowsArrayList.size();
            fetch.startAfter(cursor).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        //TODO figure it out
//                        if (rowsArrayList.get(rowsArrayList.size() - 1).getId().equals(HomePreviewDto.NULL_KEY)) {
                        rowsArrayList.remove(rowsArrayList.size() - 1);
//                            recyclerViewAdapter.notifyItemRemoved(rowsArrayList.size() - 1);
//                        }
                        //--------------
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                            homeViewModel.getLoading().setValue(true);
                            cursor = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                            for (int ir = 0; ir < queryDocumentSnapshots.size(); ir++) {
                                Log.i("XX", "Size: " + queryDocumentSnapshots.size());
                                DocumentSnapshot x = queryDocumentSnapshots.getDocuments().get(ir);
                                HomePreviewDto item = x.toObject(HomePreviewDto.class);
                                x.getReference().collection("volume").orderBy("create_at", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task1 -> {
                                    if (!task1.isSuccessful()) {
                                        rowsArrayList.add(item);
                                    } else {
                                        DocumentSnapshot i = task1.getResult().getDocuments().get(0);
                                        assert item != null;
                                        item.setLatest_vol(i.getString("title"));
                                        i.getReference().collection("chapters").orderBy("create_at", Query.Direction.ASCENDING).limit(1).get().addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                DocumentSnapshot z = task2.getResult().getDocuments().get(0);
                                                if (z.exists()) {
                                                    item.setLatest_chap(z.getString("title"));
                                                }
                                            }
                                            recyclerViewAdapter.notifyItemRangeInserted(scrollPosition + 1, rowsArrayList.size());
                                            rowsArrayList.add(item);
                                            homeViewModel.getLoading().setValue(false);
                                        });
                                    }
                                });
                            }
                        } else
                            cursor = null;
                    }).addOnFailureListener(e -> UItools.toast(requireActivity(), getResources().getString(R.string.error_norm)))
                    .addOnCompleteListener(task -> lightScroll())
            ;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initHome(HomeViewModel vm) {
        db.collection("home").orderBy("create_at", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task -> {
            HomeViewModel.Banner banner = new HomeViewModel.Banner(getResources().getString(R.string.home_notice), "https://docln.net/images/banners/12890_m_l.jpg");
            if (task.isSuccessful()) {
                if (task.getResult().getDocuments().get(0).exists())
                    banner = task.getResult().getDocuments().get(0).toObject(HomeViewModel.Banner.class);
                assert banner != null;
            }
            vm.getBanner().setValue(banner);
        });
    }

    private void lightScroll() {
        NestedScrollView nestedScrollView = binding.scrollView;
        int currentScrollY = nestedScrollView.getScrollY();

        float dpToPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int destinationScrollY = (int) (currentScrollY + dpToPixels);

        nestedScrollView.smoothScrollTo(0, destinationScrollY);
    }
}