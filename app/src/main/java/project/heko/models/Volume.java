package project.heko.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;
public class Volume {
    private String cover;
    private String title;

    public Volume(String cover,@Nullable String title) {
        this.cover = cover;
        this.title = title;
    }

    public Volume() {
        this.cover = "";
        this.title = "";
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
