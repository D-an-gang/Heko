package project.heko.ui.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import project.heko.R;
import project.heko.databinding.LightNovelInfoBinding;
import project.heko.helpers.UItools;

public class LnFragment extends Fragment {

    public LightNovelInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = LightNovelInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null || getArguments().getString("id") == null) {
            UItools.toast(requireActivity(), getString(R.string.error_load_book));
            Navigation.findNavController(requireView()).popBackStack(R.id.nav_home, false);
            return;
        }
        String id = getArguments().getString("id");
        Log.i("XX", "Pass: " + getArguments().getString("id"));
        LnViewPagerAdapter lnViewPagerAdapter = new LnViewPagerAdapter(this, id);
        binding.lnViewpager.setAdapter(lnViewPagerAdapter);
        binding.lnTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.lnViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.lnViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position > 1) {
                    position = 0;
                }
                super.onPageSelected(position);
                Objects.requireNonNull(binding.lnTabs.getTabAt(position)).select();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setFragmentVolume() {
        binding.lnViewpager.setCurrentItem(1);
    }
}