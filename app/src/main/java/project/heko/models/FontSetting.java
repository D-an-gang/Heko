package project.heko.models;

import android.graphics.Typeface;

public class FontSetting {
    public Typeface font;
    public float size;

    public FontSetting(Typeface font, float size) {
        this.font = font;
        this.size = size;
    }

    public FontSetting() {
        this.font = Typeface.DEFAULT;
        this.size = 15;
    }
}
