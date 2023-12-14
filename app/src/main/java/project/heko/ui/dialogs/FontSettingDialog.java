package project.heko.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.slider.Slider;

import java.util.Objects;

import project.heko.MainActivity;
import project.heko.R;
import project.heko.helpers.UItools;

public class FontSettingDialog extends DialogFragment {
    public float size;
    public int font_id;
    FontDialogListener listener;
    View view;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        try {
            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
            size = sharedPref.getFloat(getString(R.string.text_size), 15);
            font_id = sharedPref.getInt(getString(R.string.font_family_id), 0);
        } catch (Exception ex) {
            size = 15;
            font_id = 0;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.layout_font_setting, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view).setMessage("Tùy Chỉnh")
                // Add action buttons
                .setPositiveButton("OK!", (dialog, id) ->
                        {
                            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(getString(R.string.font_family_id), font_id);
                            editor.putFloat(getString(R.string.text_size), size);
                            editor.apply();
                            listener.onDialogPositiveClick(FontSettingDialog.this);
                        }
                ).setNegativeButton("Hủy", (dialog, id) -> {
                    try {
                        Objects.requireNonNull(FontSettingDialog.this.getDialog()).cancel();
                    } catch (NullPointerException ex) {
                        FontSettingDialog.this.dismiss();
                    }
                });
        initSpinner();
        initSlider();
        return builder.create();
    }

    private void initSpinner() {
        try {
            Spinner spin = view.findViewById(R.id.fontset_picker);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.fonts_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
            int save = sharedPref.getInt(getString(R.string.font_family_id), 0);
            spin.setSelection(save);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    font_id = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spin.setSelection(0);
                }
            });
        } catch (Exception ex) {
            UItools.toast(requireActivity(), getResources().getString(R.string.error_norm));
            this.dismiss();
        }
    }

    private void initSlider() {
        Slider slide = (Slider) view.findViewById(R.id.fontset_fontsize);
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        float save = sharedPref.getFloat(getString(R.string.text_size), -1);
        slide.setValue(save);
        slide.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                size = value;
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (FontDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(MainActivity.class + " must implement NoticeDialogListener");
        }
    }

    public interface FontDialogListener {
        /**
         * @noinspection unused
         */
        void onDialogPositiveClick(FontSettingDialog dialog);
    }
}
