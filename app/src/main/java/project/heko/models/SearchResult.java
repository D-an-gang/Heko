package project.heko.models;

public class SearchResult {
    private String display;
    private String genre;
    private String key;
    private String ref;

    public SearchResult() {
        this.display = "";
        this.genre = "";
        this.key = "";
        this.ref = "";
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
