package project.heko.models;

import java.util.Date;

public class Chapter {
    private String book_id;
    private String content;
    private Date create_at;
    private String id;
    private String title;
    private String volume_id;

    public Chapter(String book_id, String content, Date create_at, String id, String title, String volume_id) {
        this.book_id = book_id;
        this.content = content;
        this.create_at = create_at;
        this.id = id;
        this.title = title;
        this.volume_id = volume_id;
    }

    public Chapter() {
        this.book_id = null;
        this.content = null;
        this.create_at = null;
        this.id = null;
        this.title = null;
        this.volume_id = null;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVolume_id() {
        return volume_id;
    }

    public void setVolume_id(String volume_id) {
        this.volume_id = volume_id;
    }

}
