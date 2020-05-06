package developer.ishank.forlalit.sir.lsacademy.datamodel;

public class NotesDataModel {

    private String title;
    private String subject;
    private String downloadUrl;
    private int category;
    private int unit;
    private int semester;
    private String author;

    public NotesDataModel(String title, String subject, String downloadUrl, int category, int unit, int semester, String author) {
        this.title = title;
        this.subject = subject;
        this.downloadUrl = downloadUrl;
        this.category = category;
        this.unit = unit;
        this.semester = semester;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public int getCategory() {
        return category;
    }

    public int getUnit() {
        return unit;
    }

    public int getSemester() {
        return semester;
    }

    public String getAuthor() {
        return author;
    }
}
