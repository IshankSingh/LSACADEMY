package developer.ishank.forlalit.sir.lsacademy.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.NotesActivity;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.activity.download;
import developer.ishank.forlalit.sir.lsacademy.classes.Constants;
import developer.ishank.forlalit.sir.lsacademy.classes.SentenceCase;
import developer.ishank.forlalit.sir.lsacademy.datamodel.BooksDataModel;
import developer.ishank.forlalit.sir.lsacademy.datamodel.NotesDataModel;

import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class NotesBookAdapter extends RecyclerView.Adapter<NotesBookAdapter.NotesViewHolder>{

    private Context mContext;
    private ArrayList<NotesDataModel> notesList;
    private NotesBookListener notesBookListener;

    public NotesBookAdapter(Context mContext, ArrayList<NotesDataModel> notesList, NotesBookListener notesBookListener) {
        this.mContext = mContext;
        this.notesList = notesList;
        this.notesBookListener = notesBookListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notes_list_item,parent,false);
        return new NotesViewHolder(view, notesBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.subject.setText(notesList.get(position).getSubject());

        String author = "By:  "+notesList.get(position).getAuthor();
        String unit = "Unit: " + notesList.get(position).getUnit();
        final String semester = "Sem: "+ notesList.get(position).getSemester();

        holder.author.setText(author);
        holder.unit.setText(unit);
        holder.sem.setText(semester);
        holder.downloadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.progressBar.setIndeterminate(false);
                    holder.progressBar.setMax(100);
                    holder.progressBar.setVisibility(View.VISIBLE);

                    DownloadTask downloadTask = new DownloadTask(mContext, holder.progressBar, notesList.get(position).getTitle());
                    downloadTask.execute(notesList.get(position).getDownloadUrl());
            }
        });

        holder.viewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.EXTERNAL_MEMORY_PATH + notesList.get(position).getTitle() + ".pdf";
                Log.d("TAG", "onClick: "+path);
                File file = new File(path);
                Log.d("TAG", "downloadFileItemClickListener: " + path);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, subject, author, unit, sem;
        ImageView downloadIV;
        NotesBookListener notesBookListener;
        ProgressBar progressBar;
        Button viewBTN;

        NotesViewHolder(@NonNull View itemView, NotesBookListener notesBookListener) {
            super(itemView);
            title = itemView.findViewById(R.id.notes_title);
            subject = itemView.findViewById(R.id.notes_subject_name);
            author = itemView.findViewById(R.id.notes_author);
            unit = itemView.findViewById(R.id.notes_unit);
            sem = itemView.findViewById(R.id.notes_sem);
            downloadIV = itemView.findViewById(R.id.notes_IV2);
            viewBTN = itemView.findViewById(R.id.notes_BTN);

            progressBar = itemView.findViewById(R.id.notes_PB);
            this.notesBookListener = notesBookListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notesBookListener.notesBookItemClickListener(getAdapterPosition());
        }
    }

    public interface NotesBookListener {
        void notesBookItemClickListener(int position);
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        ProgressBar progressBar;
        String title;

        public DownloadTask(Context context, ProgressBar progressBar, String title) {
            this.context = context;
            this.progressBar = progressBar;
            this.title = title;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        Constants.EXTERNAL_MEMORY_PATH + title + ".pdf");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }
}


