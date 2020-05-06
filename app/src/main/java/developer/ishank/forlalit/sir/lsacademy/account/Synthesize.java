package developer.ishank.forlalit.sir.lsacademy.account;

import android.text.TextUtils;
import android.widget.EditText;

 public class Synthesize {


      public static boolean isEmpty(EditText editText) {
        CharSequence str = editText.getText().toString();
        return TextUtils.isEmpty(str);
    }
     static String synthesizeEmail(String str) {
         str = str.replaceAll(" ","");
         str = str.toLowerCase();
         return str;
     }

     static String synthesizePassword(String str) {
         String passAdditional = "#@$%123";
         str = str.replaceAll(" ","");
         str = str.substring(0,2).toUpperCase() + str.substring(3).toLowerCase() + passAdditional;
         return str;
     }

     static String[] synthesizeUsername(String str) {
         String [] username = str.split("\\s+");
         for (int i = 0; i < username.length; i++) {
             username[i] = String.valueOf(username[i].charAt(0)).toUpperCase() + username[i].substring(1).toLowerCase();
         }
         return username;
     }

     public static String synthesizeInitialLetter(String str) {
         str = String.valueOf(str.charAt(0)).toUpperCase();
         return str;
     }
}
