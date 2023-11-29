package project.heko.dto;

import java.util.ArrayList;

import project.heko.models.Chapter;
import project.heko.models.Volume;

public class LightNovelDto {
    private Volume vols;
    private ArrayList<Chapter> chaps;

    public LightNovelDto(Volume vols, ArrayList<Chapter> chaps) {
        this.vols = vols;
        this.chaps = chaps;
    }

    public LightNovelDto() {
        this.vols = null;
        this.chaps = null;
    }

    public Volume getVols() {
        return vols;
    }

    public void setVols(Volume vols) {
        this.vols = vols;
    }

    public ArrayList<Chapter> getChaps() {
        return chaps;
    }

    public void setChaps(ArrayList<Chapter> chaps) {
        this.chaps = chaps;
    }
}
