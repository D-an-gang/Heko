package project.heko.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class RestartDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo")
                .setMessage("Cần phải khởi động lại ứng dụng để có hiệu lực")
                .setPositiveButton("Uki!!", (dialog, id) -> {
                    Intent intent = requireActivity().getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    requireActivity().finish();
                    startActivity(intent);
                })
                .setNegativeButton("Không phải lúc này!", (dialog, id) -> dialog.cancel());
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
