package project.heko.models;

import java.util.Arrays;
import java.util.Date;

public class Book {
    private String author;
    private String book_cover;
    private int chapters_number;
    private Date create_at;
    private String description;
    private String genre;
    private String id;
    private Date last_update;
    private String title;

    public Book(String author, String book_cover, int chapters_number, Date create_at, String description, String genre, String id, Date last_update, String title) {
        this.author = author;
        this.book_cover = book_cover;
        this.chapters_number = chapters_number;
        this.create_at = create_at;
        this.description = description;
        this.genre = genre;
        this.id = id;
        this.last_update = last_update;
        this.title = title;
    }

    public Book() {
        this.author = "";
        this.book_cover = "";
        this.chapters_number = 0;
        this.create_at = null;
        this.description = "";
        this.genre = "";
        this.id = "";
        this.last_update = null;
        this.title = "";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook_cover() {
        return book_cover;
    }

    public void setBook_cover(String book_cover) {
        this.book_cover = book_cover;
    }

    public int getChapters_number() {
        return chapters_number;
    }

    public void setChapters_number(int chapters_number) {
        this.chapters_number = chapters_number;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
