package project.heko.helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.heko.ui.profile.ProfileFragment;

public class UItools {
    public static void toast(@NonNull Context cox, @NonNull CharSequence string) {
        Toast.makeText(cox, string, Toast.LENGTH_SHORT).show();
    }
    public static boolean validateEmail(String email) {
        // Email regex pattern for basic validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
