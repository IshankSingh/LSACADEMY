package developer.ishank.forlalit.sir.lsacademy.datamodel;


/*
 * This class is used im SpecificSubjectActivity as a data model class for retrieving youtube playlist detail
 * @author Ishank Singh
 */
public class SubjectDetailsDataModel {

    private String title;
    private String description;
    private String thumbnail;
    private String videoId;

    public SubjectDetailsDataModel(String title, String description, String thumbnail, String videoId) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getVideoId() {
        return videoId;
    }
}
