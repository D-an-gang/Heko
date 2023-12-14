package project.heko.helpers;

import java.util.ArrayList;

import project.heko.R;

public class ThemeConfig {
    public static ArrayList<Integer> getAllThemes(){
        ArrayList<Integer> themes = new ArrayList<>();
        themes.add(R.style.Theme_Heko_NoActionBar);
        themes.add(R.style.Theme_Cherry_NoActionBar);
        return themes;
    }
}
