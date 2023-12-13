package project.heko.models;

import androidx.annotation.Nullable;
public class Volume {
    private String id;
    private String cover;
    private String title;

    public Volume(String id,String cover,@Nullable String title) {
        this.id = id;
        this.cover = cover;
        this.title = title;
    }

    public Volume() {
        this.id = "";
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
