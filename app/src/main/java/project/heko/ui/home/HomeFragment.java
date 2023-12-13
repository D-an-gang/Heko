package project.heko.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.heko.R;
import project.heko.databinding.FragmentHomeBinding;
import project.heko.dto.HomePreviewDto;
import project.heko.helpers.UItools;
import project.heko.models.Pagination;

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
    Pagination pag;

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
        binding.search.setOnClickListener(v -> {
            Log.i("XX", "Search");
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_searchFragment);
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
        fetch.count().get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            long total = task.getResult().getCount();
            pag = new Pagination(total, PAGE_SIZE);
        });

        fetch.limit(PAGE_SIZE).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cursor = task.getResult().getDocuments().get(task.getResult().size() - 1);
                for (DocumentSnapshot x : task.getResult()) {
                    if (x.exists()) {
                        HomePreviewDto item = x.toObject(HomePreviewDto.class);
                        if (item != null) {
                            x.getReference().collection("volume").orderBy("create_at", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task1 -> {
                                if (!task1.isSuccessful()) {
                                    rowsArrayList.add(item);
                                    recyclerViewAdapter.notifyDataSetChanged();
                                } else {
                                    DocumentSnapshot i = task1.getResult().getDocuments().get(0);
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
                }
            }
        });
        initAdapter();
        initScrollListener();
    }

    private void initAdapter() {
        recyclerViewAdapter = new HomePageRecycleAdapter(rowsArrayList, Navigation.findNavController(requireView()));
        GridLayoutManager grid = new GridLayoutManager(requireActivity(), 2,RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(grid);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initScrollListener() {
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (binding == null)
                return;
            View view = binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (binding.scrollView.getHeight() + binding.scrollView.getScrollY()));
            if (diff == 0 && Boolean.FALSE.equals(homeViewModel.getLoading().getValue()))
                loadMore();
        });
    }

    private void loadMore() {
        boolean debounce = false;
        if (cursor != null) {
            Log.i("XX", "before size: " + rowsArrayList.size());
            if (rowsArrayList.size() != 0 && rowsArrayList.get(rowsArrayList.size() - 1) != null) {
                rowsArrayList.add(null);
                recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);
                debounce = true;
            }
            if (debounce) {
                fetch.startAfter(cursor).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            rowsArrayList.remove(rowsArrayList.size() - 1);
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
                                            if (task1.getResult().size() <= 0 && !task1.getResult().getDocuments().get(0).exists())
                                                return;
                                            DocumentSnapshot i = task1.getResult().getDocuments().get(0);
                                            assert item != null;
                                            item.setLatest_vol(i.getString("title"));
                                            i.getReference().collection("chapters").orderBy("create_at", Query.Direction.ASCENDING).limit(1).get().addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    if (task2.getResult().size() > 0) {
                                                        DocumentSnapshot z = task2.getResult().getDocuments().get(0);
                                                        if (z.exists())
                                                            item.setLatest_chap(z.getString("title"));
                                                    }
                                                }
                                                rowsArrayList.add(item);
                                                recyclerViewAdapter.notifyItemRangeInserted(recyclerViewAdapter.getItemCount(), rowsArrayList.size() - 1);
                                                homeViewModel.getLoading().setValue(false);
                                            });
                                        }
                                    });
                                }
                            } else {
                                cursor = null;
                                Log.i("XX", "End of col");
                            }
                        }).addOnFailureListener(e -> UItools.toast(requireActivity(), getResources().getString(R.string.error_norm)))
                        .addOnCompleteListener(task -> {
                            pag.CurrentPage++;
                            if (pag.CurrentPage == pag.TotalPage) {
                                cursor = null;
                            }
                        });
            }
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
}