package project.heko.dto;

import com.google.firebase.Timestamp;

import project.heko.models.Book;

public class HomePreviewDto extends Book {
    private String latest_vol;
    private String latest_chap;

    public HomePreviewDto(String author, String book_cover, int chapters_number, Timestamp create_at, String description, String genre, String id, Timestamp last_update, String title, String latest_vol, String latest_chap) {
        super(author, book_cover, chapters_number, create_at, description, genre, id, last_update, title);
        this.latest_vol = latest_vol;
        this.latest_chap = latest_chap;
    }

    public HomePreviewDto() {
        this.latest_vol = "";
        this.latest_chap = "";
    }

    public String getLatest_vol() {
        return latest_vol;
    }

    public void setLatest_vol(String latest_vol) {
        this.latest_vol = latest_vol;
    }

    public String getLatest_chap() {
        return latest_chap;
    }

    public void setLatest_chap(String latest_chap) {
        this.latest_chap = latest_chap;
    }
}
