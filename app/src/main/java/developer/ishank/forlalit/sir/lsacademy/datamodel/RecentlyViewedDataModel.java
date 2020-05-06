package developer.ishank.forlalit.sir.lsacademy.datamodel;

public class RecentlyViewedDataModel {

    private String title;
    private String thumbnail;

    public RecentlyViewedDataModel(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
