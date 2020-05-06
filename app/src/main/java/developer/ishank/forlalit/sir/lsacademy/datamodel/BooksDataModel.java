package developer.ishank.forlalit.sir.lsacademy.datamodel;

public class BooksDataModel {

    private String title;
    private String description;
    private String subject;
    private String author;
    private String thumbnail;
    private String url;
    private int category;

    public BooksDataModel(String title, String description, String subject, String author, String thumbnail, String url, int category) {
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.author = author;
        this.thumbnail = thumbnail;
        this.url = url;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public int getCategory() {
        return category;
    }
}
