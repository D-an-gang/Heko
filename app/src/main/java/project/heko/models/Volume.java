package project.heko.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;
public class Volume {
    private String book_id;
    private String cover;
    private String id;
    private String title;

    public Volume(@NonNull String book_id, String cover, String id,@Nullable String title) {
        this.book_id = book_id;
        this.cover = cover;
        this.id = id;
        this.title = title;
    }

    public Volume() {
        this.book_id = null;
        this.cover = null;
        this.id = null;
        this.title = null;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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
//    public static Volume volume_mapper(DocumentSnapshot document) throws NullPointerException {
//        Volume res = new Volume();
//        if (document.getString("book_id") != null && document.getString("cover") != null && document.getString("id") != null && document.getString("title") != null) {
//            res.setBook_id(Objects.requireNonNull(document.getString("book_id")));
//            res.setCover(document.getString("cover"));
//            res.setTitle(document.getString("title"));
//            res.setId(document.getId());
//        } else throw new NullPointerException("User not found");
//        return res;
//    }
}
