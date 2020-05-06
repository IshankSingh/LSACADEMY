package developer.ishank.forlalit.sir.lsacademy.datamodel;

public class CommentSectionDataModel {

    private String username;
    private String posted_by;
    private String body;
    private String timestamp;
    private int id;

    public CommentSectionDataModel(String username, String posted_by, String body, String timestamp, int id) {
        this.username = username;
        this.posted_by = posted_by;
        this.body = body;
        this.timestamp = timestamp;
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return id;
    }
}
