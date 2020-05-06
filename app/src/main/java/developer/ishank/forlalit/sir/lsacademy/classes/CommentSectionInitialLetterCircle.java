package developer.ishank.forlalit.sir.lsacademy.classes;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Random;

import androidx.core.content.ContextCompat;
import developer.ishank.forlalit.sir.lsacademy.R;

public class CommentSectionInitialLetterCircle {

    private Context mContext;

    public CommentSectionInitialLetterCircle(Context mContext) {
        this.mContext = mContext;
    }

    private static final int [] drawable = {R.drawable.edit_text_comment_initial_letter_bg, R.drawable.edit_text_comment_initial_letter_bg_blue,
            R.drawable.edit_text_comment_initial_letter_bg_green, R.drawable.edit_text_comment_initial_letter_bg_orange,
            R.drawable.edit_text_comment_initial_letter_bg_purple, R.drawable.edit_text_comment_initial_letter_bg_skyblue};

    public Drawable getRequiredDrawable(char c) {
        if (c == 'A' || c == 'I') {
            return ContextCompat.getDrawable(mContext, drawable[0]);
        }

        if (c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F') {
            return ContextCompat.getDrawable(mContext, drawable[1]);
        }

        if (c == 'G' || c == 'H' || c == 'J' || c == 'K') {
            return ContextCompat.getDrawable(mContext, drawable[2]);
        }

        if (c == 'L' || c == 'M' || c == 'N' || c == 'O' || c == 'T') {
            return ContextCompat.getDrawable(mContext, drawable[3]);
        }

        if (c == 'U' || c == 'V' || c == 'W' || c == 'X' || c == 'Y') {
            return ContextCompat.getDrawable(mContext, drawable[4]);
        }

        if (c == 'P' || c == 'Q' || c == 'R' || c == 'S') {
            return ContextCompat.getDrawable(mContext, drawable[5]);
        }

        Random random = new Random(drawable.length);
        int randomId = random.nextInt(drawable.length);

        return ContextCompat.getDrawable(mContext, drawable[randomId]);
    }
}
