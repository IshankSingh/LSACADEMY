package developer.ishank.forlalit.sir.lsacademy.classes;

import android.content.Context;
import android.widget.Toast;

public class Constants {

    public static final String API_KEY = "AIzaSyAD4-ZBsq8a17yiiOKCCCT2Dw--3ZSfLUo";
    public static String PART = "snippet";
    public static final String PLAY_LIST_ITEM_URL = "https://www.googleapis.com/youtube/v3/playlistItems";

    public static final String SERVER_URL = "http://15.206.169.195/";
    public static final String EXTERNAL_MEMORY_PATH = "/Android/data/developer.ishank.forlalit.sir.lsacademy/files/Download/";

    public static void showMessage(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
