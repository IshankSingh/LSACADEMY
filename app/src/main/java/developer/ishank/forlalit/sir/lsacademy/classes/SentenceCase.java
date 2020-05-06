package developer.ishank.forlalit.sir.lsacademy.classes;

public class SentenceCase {

    public static String getTitleSentenceCase(String text) {
        text = String.valueOf(text.charAt(0)).toUpperCase() + text.substring(1).toLowerCase();
        return text;
    }
    public static String getDescriptionSentenceCase(String description) {
        int n = description.length();
        if (!(String.valueOf(description.charAt(n-1))).equals(".")) {
            description = description + ".";
        }
        description = String.valueOf(description.charAt(0)).toUpperCase() + description.substring(1);
        return description;
    }

    public static String getTitleForSubjectDetailActivity(String title){
        int n = title.length();

        if (n > 40) {
            title = String.valueOf(title.charAt(0)).toUpperCase() + title.substring(1,40);
            title = title + "...";
        }
        else {
            title = String.valueOf(title.charAt(0)).toUpperCase() + title.substring(1);
        }
        return title;
    }

    public static String getTitleForBooks(String title){
        int n = title.length();

        if (n > 14) {
            title = String.valueOf(title.charAt(0)).toUpperCase() + title.substring(1,14);
            title = title + "...";
        }
        else {
            title = String.valueOf(title.charAt(0)).toUpperCase() + title.substring(1);
        }
        return title;
    }
}
