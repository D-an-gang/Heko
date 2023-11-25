package project.heko.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import project.heko.models.Book;
import project.heko.models.Chapter;
import project.heko.models.Volume;

public class LnDto extends Volume{
    /*private ArrayList<Chapter> chapters;

    public LnDto(@NonNull String book_id, String cover, String id, @Nullable String title, ArrayList<Chapter> chapters) {
        super(book_id, cover, id, title);
        this.chapters = chapters;
    }
    public LnDto(){
        this.chapters = null;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public LnDto(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void addChap(Chapter chap){
        this.chapters.add(chap);
    }*/
}
