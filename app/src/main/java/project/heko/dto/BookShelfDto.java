package project.heko.dto;

public class BookShelfDto {
    private String id;
    private String cover;
    private String title;
    private long unread;
    private String docId;
    public BookShelfDto(String id,String cover, String title, long unread,String docId) {
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.unread = unread;
        this.docId = docId;
    }

    public BookShelfDto() {
        this.id = "";
        this.cover = "";
        this.title = "";
        this.unread = 0;
        this.docId = "";
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

    public long getUnread() {
        return unread;
    }

    public void setUnread(long unread) {
        this.unread = unread;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
