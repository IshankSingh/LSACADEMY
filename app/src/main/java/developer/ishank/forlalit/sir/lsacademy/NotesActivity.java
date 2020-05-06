package developer.ishank.forlalit.sir.lsacademy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.adapter.NotesBookAdapter;
import developer.ishank.forlalit.sir.lsacademy.classes.Constants;
import developer.ishank.forlalit.sir.lsacademy.classes.MySingleton;
import developer.ishank.forlalit.sir.lsacademy.datamodel.NotesDataModel;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class NotesActivity extends AppCompatActivity implements NotesBookAdapter.NotesBookListener {

    private ArrayList<NotesDataModel> notesList = new ArrayList<>();
    private RecyclerView notesRecyclerView;
    private TextView notesTV;
    NotesBookAdapter notesBookAdapter;

    String url, toolbarTitle;
    static final int MY_PERMISSION_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesRecyclerView = findViewById(R.id.notes_recycler_view);
        notesTV = findViewById(R.id.notesTV);

        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }
        else{
            getIncomingIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Constants.showMessage(this,"Permission Granted! ");
                    getIncomingIntent();
                }else {
                    Constants.showMessage(this,"Permission not Granted! ");
                }
            }
        }
    }

    public void getIncomingIntent() {
        if (getIntent().hasExtra("PHP_CLASS_NAME")){
            toolbarTitle = getIntent().getStringExtra("TOOLBAR_TITLE");
            String phpClass = getIntent().getStringExtra("PHP_CLASS_NAME");
            url = Constants.SERVER_URL + phpClass;
            getNotesJson(url);
        }
    }

    private void getNotesJson(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String subject = jsonObject.getString("subject");
                                String url = jsonObject.getString("url");
                                String author = jsonObject.getString("author");
                                String category = jsonObject.getString("category");
                                String unit = jsonObject.getString("unit");
                                String semester = jsonObject.getString("semester");

                                int cat = Integer.valueOf(category);
                                int notes_unit = Integer.valueOf(unit);
                                int sem = Integer.valueOf(semester);
                                notesList.add(new NotesDataModel(title, subject, url, cat, notes_unit, sem, author));
                            }
                            setNotesAdapter(notesList);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(getApplicationContext()).addTORequestQueue(stringRequest);

    }

    private void setNotesAdapter(ArrayList<NotesDataModel> generalNotesList) {
        if (generalNotesList.size() == 0) {
            notesTV.setVisibility(View.VISIBLE);
            String content = toolbarTitle + " will be available soon";
            notesTV.setText(content);
        }else {
            notesTV.setVisibility(View.GONE);
        }
        notesBookAdapter = new NotesBookAdapter(this,generalNotesList,this);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        notesRecyclerView.setAdapter(notesBookAdapter);
    }

    @Override
    public void notesBookItemClickListener(int position) {

    }
}
