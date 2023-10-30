package project.heko.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class User {
    @NonNull
    private String username;
    private String email;
    private String imgUrl;
    private final String defaultImgUrl = "https://firebasestorage.googleapis.com/v0/b/heko-a6b88.appspot.com/o/logo-9.png?alt=media&token=37e6dac7-aff9-4563-8cdf-c63fe8537319";

    public User(@NonNull String username, String email, String imgUrl, @Nullable String id) {
        this.username = username;
        this.email = email;
        this.imgUrl = imgUrl;
        this.id = id;
    }

    public User(@NonNull String username, String email, @Nullable String id) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.imgUrl = defaultImgUrl;
    }

    public User() {
        this.id = null;
        this.email = null;
        this.username = "Guest";
        this.imgUrl = defaultImgUrl;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    private String id;

    public static User user_mapper(DocumentSnapshot document) throws NullPointerException {
        User res = new User();
        if (document.getString("username") != null && document.getString("imgUrl") != null && document.getString("email") != null) {
            res.setUsername(Objects.requireNonNull(document.getString("username")));
            res.setImgUrl(document.getString("imgUrl"));
            res.setEmail(document.getString("email"));
            res.setId(document.getId());
        } else throw new NullPointerException("User not found");
        return res;
    }

}
