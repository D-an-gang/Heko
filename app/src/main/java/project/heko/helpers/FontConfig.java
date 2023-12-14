package project.heko.helpers;

import android.content.Context;
import android.graphics.Typeface;

import java.util.ArrayList;

import project.heko.R;

public class FontConfig {
    public static ArrayList<Typeface> getFonts(Context context) {
        ArrayList<Typeface> fonts = new ArrayList<>();
        // danh sách phải đồng bộ với res/values/font_list
        fonts.add(Typeface.DEFAULT);
        fonts.add(context.getResources().getFont(R.font.noto));
        fonts.add(context.getResources().getFont(R.font.merri));
        return fonts;
    }
}
