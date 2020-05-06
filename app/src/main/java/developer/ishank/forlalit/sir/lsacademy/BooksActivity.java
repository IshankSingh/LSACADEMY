package developer.ishank.forlalit.sir.lsacademy;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.reflect.Method;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import developer.ishank.forlalit.sir.lsacademy.classes.Constants;
import developer.ishank.forlalit.sir.lsacademy.classes.SentenceCase;
import developer.ishank.forlalit.sir.lsacademy.database.DatabaseHelper;

public class BooksActivity extends AppCompatActivity implements View.OnClickListener {

    //viewGroup
    LinearLayout booksIVHolderLinearLayout;

    //view
    String thumbnail, title, description, author, url;
    ImageView booksIV;
    TextView booksTitleTV, booksDescriptionTV;
    Button booksDownloadButton, booksViewBtn;

    private int colorVibrant, colorLightVibrant, colorDarkVibrant, colorMuted, colorLightMuted, colorDarkMuted;
    private long downloadID;
    private ProgressDialog progressDialog;
    private static final int DOWNLOAD_STATUS = 1;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        booksIVHolderLinearLayout = findViewById(R.id.books_linear_bg);
        booksIV = findViewById(R.id.books_image_view);
        booksTitleTV = findViewById(R.id.books_title);
        booksDescriptionTV = findViewById(R.id.books_description);
        booksDownloadButton = findViewById(R.id.books_download_btn);
        booksViewBtn = findViewById(R.id.books_view_btn);

        booksDownloadButton.setOnClickListener(this);
        booksViewBtn.setOnClickListener(this);
        progressDialog = new ProgressDialog(BooksActivity.this);

        myDB = new DatabaseHelper(this, "books");

        getIncomingIntent();
        holdView();

        //checking download status of book

        Cursor cursor = myDB.getDownloadStatus(title, author);
        if (cursor.getCount() == 0) {
            booksDownloadButton.setTextColor(getResources().getColor(R.color.white));
            booksDownloadButton.setBackgroundResource(R.drawable.custom_youtube_downloadable_button);
            booksDownloadButton.setEnabled(true);

        }else{
            StringBuffer buffer = new StringBuffer();
            int status;
            while (cursor.moveToNext()) {
                status = Integer.valueOf(cursor.getString(3));
                if (DOWNLOAD_STATUS == status) {
                    booksDownloadButton.setVisibility(View.INVISIBLE);
                    booksViewBtn.setBackground(getResources().getDrawable(R.drawable.custom_youtube_downloadable_button));
                    booksViewBtn.setTextColor(getResources().getColor(R.color.white));
                    booksViewBtn.setEnabled(true);
                }
            }

        }

        /*
         * Implementing broadcast receiver for checking the status of the download manager
         */

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        registerReceiver(new BroadcastReceiver()  {
            @Override
            public void onReceive(Context context, Intent intent) {
                long broadCastedDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (broadCastedDownloadID == downloadID) {
                    if (getDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                        Constants.showMessage(getApplicationContext(),"Download complete.");
                        booksViewBtn.setBackground(getResources().getDrawable(R.drawable.custom_youtube_downloadable_button));
                        booksViewBtn.setTextColor(getResources().getColor(R.color.white));
                        booksViewBtn.setEnabled(true);

                    }else {
                        Constants.showMessage(BooksActivity.this,"Download Failed.");
                    }
                }

            }
            },intentFilter);
    }

    public void getIncomingIntent() {
        if (getIntent().hasExtra("thumbnail") && getIntent().hasExtra("title")
                && getIntent().hasExtra("description") && getIntent().hasExtra("author")){

            thumbnail = getIntent().getStringExtra("thumbnail");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            author = getIntent().getStringExtra("author");
            Log.d("TAG", "getIncomingIntent: "+thumbnail+title+description+author);
        }

        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
            Log.d("TAG", "getIncomingIntent: "+url);
        }
    }

    /*
     *--------------inflating data to the view in holdView() method------------
     */
    public void holdView() {

        Picasso.get()
                .load(thumbnail)
                .centerCrop()
                .resize(130,180)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        booksIV.setImageBitmap(bitmap);
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int defaultColor = 0xffffff;
                                if (palette != null) {
                                    colorVibrant = palette.getVibrantColor(defaultColor);
                                    colorLightVibrant = palette.getLightVibrantColor(defaultColor);
                                    colorDarkVibrant = palette.getDarkVibrantColor(defaultColor);
                                    colorMuted = palette.getMutedColor(defaultColor);
                                    colorLightMuted = palette.getLightMutedColor(defaultColor);
                                    colorDarkMuted = palette.getDarkMutedColor(defaultColor);

                                    int [] color = {colorVibrant, colorLightVibrant, colorMuted, colorLightMuted};
                                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL,color);
                                    gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                                    gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                                    booksIVHolderLinearLayout.setBackground(gradientDrawable);
                                     booksDownloadButton.setTextColor(getResources().getColor(R.color.white));
                                    if (colorDarkVibrant == 0xffffff){
                                        booksTitleTV.setTextColor(colorVibrant);
                                    }else {
                                        booksTitleTV.setTextColor(colorDarkVibrant);
                                    }
                                    booksDescriptionTV.setTextColor(colorDarkMuted);
                                }
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        title = SentenceCase.getTitleSentenceCase(title);
        description = SentenceCase.getDescriptionSentenceCase(description);
        booksTitleTV.setText(title);
        booksDescriptionTV.setText(description);

    }

    @Override
    public void onClick(View v) {
        if (v == booksDownloadButton){
//            progressDialog.setTitle(title);
//            progressDialog.setMessage("Downloading....");
//            progressDialog.show();

            myDB.insertData(title, author, true);
            Cursor cursor = myDB.getDownloadStatus(title, author);
            if (cursor.getCount() == 0) {
                Constants.showMessage(this, "No Entry Yet");
            }else{
                StringBuffer buffer = new StringBuffer();
                int status;
                while (cursor.moveToNext()) {
                    status = Integer.valueOf(cursor.getString(3));
                    if (DOWNLOAD_STATUS == status) {
                        booksDownloadButton.setBackgroundResource(R.drawable.custom_disable_button_bg);
                        booksDownloadButton.setEnabled(false);
                        booksDownloadButton.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(title);
            request.setDescription("Download PDF");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, title + ".pdf");

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);
        }

        if (v == booksViewBtn) {
            if(Build.VERSION.SDK_INT>=24){
                try{
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.EXTERNAL_MEMORY_PATH + title + ".pdf";
            Log.d("TAG", "onClick: "+path);
            File file = new File(path);
            Log.d("TAG", "downloadFileItemClickListener: " + path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

    }

    /*
     *-------checking the status the file whether it is downloaded or not-----------------
     */
    private int getDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            return cursor.getInt(columnIndex);
        }
        return DownloadManager.ERROR_UNKNOWN;
    }

}
