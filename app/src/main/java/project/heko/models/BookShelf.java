package project.heko.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class BookShelf {
    private String book_id;
    private Timestamp last_read;
    private int unread;
    private String user_id;
    private Timestamp last_update;

    public BookShelf(String book_id, Timestamp last_read, int unread, String user_id, Timestamp last_update) {
        this.book_id = book_id;
        this.last_read = last_read;
        this.unread = unread;
        this.user_id = user_id;
        this.last_update = last_update;
    }

    public BookShelf() {
        this.book_id = "";
        this.last_read = new Timestamp(new Date());
        this.unread = 0;
        this.user_id = "";
        this.last_update = new Timestamp(new Date());
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public Timestamp getLast_read() {
        return last_read;
    }

    public void setLast_read(Timestamp last_read) {
        this.last_read = last_read;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }
}
