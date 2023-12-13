package project.heko.ui.book;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LnViewPagerAdapter extends FragmentStateAdapter {

    private final String id;
    public LnViewPagerAdapter(@NonNull Fragment fragmentActivity,String id) {
        super(fragmentActivity);
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            VolumeFragment ln_volume = new VolumeFragment();
            if (this.id != null) {
                Bundle rs = new Bundle();
                rs.putString("id", this.id);
                ln_volume.setArguments(rs);
                Log.i("adapter", id);
            }
            return ln_volume;
        }
        InfoLnFragment ln_info = new InfoLnFragment();
        if (this.id != null) {
            Bundle rs = new Bundle();
            rs.putString("id", this.id);
            ln_info.setArguments(rs);
            Log.i("adapter", id);
        }
        return ln_info;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
