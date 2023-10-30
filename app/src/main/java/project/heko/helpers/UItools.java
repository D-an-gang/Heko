package project.heko.helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import project.heko.ui.profile.ProfileFragment;

public class UItools {
    public static void toast(@NonNull Context cox, @NonNull CharSequence string) {
        Toast.makeText(cox, string, Toast.LENGTH_SHORT).show();
    }

}
