package developer.ishank.forlalit.sir.lsacademy.datamodel;


/*
 * This class is used im MainActivity as a data model class for retrieving youtube playlist detail
 * @author Ishank Singh
 */
public class SubjectDataModel {

    private String title;
    private String thumbnail;
    private String playlistId;

    public SubjectDataModel(String title, String thumbnail, String playlistId) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.playlistId = playlistId;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPlaylistId (){
        return playlistId;
    }


}
