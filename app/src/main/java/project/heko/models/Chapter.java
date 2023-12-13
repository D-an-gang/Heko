package project.heko.models;

import com.google.firebase.Timestamp;

public class Chapter {
    private String id;
    private String content;
    private Timestamp create_at;
    private String title;

    public Chapter(String id ,String content, Timestamp create_at, String title) {
        this.id = id;
        this.content = content;
        this.create_at = create_at;
        this.title = title;
    }

    public Chapter() {
        this.id = "";
        this.content = "";
        this.create_at = null;
        this.title = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
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
