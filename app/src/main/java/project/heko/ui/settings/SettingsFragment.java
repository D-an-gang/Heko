package project.heko.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import project.heko.R;
import project.heko.ui.dialogs.RestartDialog;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        SharedPreferences.Editor sp = requireActivity().getPreferences(Context.MODE_PRIVATE).edit();
        //noinspection DataFlowIssue
        findPreference("theme_setting").setOnPreferenceChangeListener((preference, newValue) -> {
            Log.i("XX", "setted: " + newValue.toString());
            try {
                int selected_theme = Integer.parseInt(newValue.toString());
                sp.putInt(getString(R.string.theme_id), selected_theme);
                sp.apply();
                showDialog();
            } catch (Exception ex) {
                return false;
            }
            return true;
        });
        //noinspection DataFlowIssue
        findPreference("notification").setOnPreferenceChangeListener((preference, newValue) -> {
            boolean state = Boolean.parseBoolean(newValue.toString());
            sp.putBoolean(getString(R.string.setting_notification), state);
            sp.apply();
            return true;
        });
    }

    public void showDialog() {
        RestartDialog x = new RestartDialog();
        x.show(requireActivity().getSupportFragmentManager(), "restart_required");
    }
}