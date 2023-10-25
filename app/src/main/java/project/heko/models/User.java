package project.heko.models;

public class User {
    private String username;
    private String email;
    private String imgUrl;
     private String defaultImgUrl = "https://firebasestorage.googleapis.com/v0/b/heko-a6b88.appspot.com/o/logo-9.png?alt=media&token=37e6dac7-aff9-4563-8cdf-c63fe8537319";
    public User(String username, String email, String imgUrl, String id) {
        this.username = username;
        this.email = email;
        this.imgUrl = imgUrl;
        this.id = id;
    }

    public User(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.imgUrl = defaultImgUrl;
    }

    public User() {
        this.id= null;
        this.email = null;
        this.username = "Guest";
        this.imgUrl = defaultImgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}
