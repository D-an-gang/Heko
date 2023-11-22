package project.heko.models;

import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Chapter {
    private String content;
    private Timestamp create_at;
    private String title;

    public Chapter(String content, Timestamp create_at, String title) {
        this.content = content;
        this.create_at = create_at;
        this.title = title;
    }

    public Chapter() {
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

}
