package developer.ishank.forlalit.sir.lsacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.account.Login;
import developer.ishank.forlalit.sir.lsacademy.activity.Feedback;
import developer.ishank.forlalit.sir.lsacademy.activity.KnowUs;
import developer.ishank.forlalit.sir.lsacademy.activity.download;
import developer.ishank.forlalit.sir.lsacademy.adapter.GeneralBooksAdapter;
import developer.ishank.forlalit.sir.lsacademy.adapter.SubjectAdapter;
import developer.ishank.forlalit.sir.lsacademy.adapter.TechnicalBooksAdapter;
import developer.ishank.forlalit.sir.lsacademy.classes.Constants;
import developer.ishank.forlalit.sir.lsacademy.classes.MySingleton;
import developer.ishank.forlalit.sir.lsacademy.account.SessionManager;
import developer.ishank.forlalit.sir.lsacademy.datamodel.BooksDataModel;
import developer.ishank.forlalit.sir.lsacademy.datamodel.NotesDataModel;
import developer.ishank.forlalit.sir.lsacademy.datamodel.RecentlyViewedDataModel;
import developer.ishank.forlalit.sir.lsacademy.datamodel.SubjectDataModel;
import developer.ishank.forlalit.sir.lsacademy.fragments.NetworkFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SubjectAdapter.OnSubjectListener,
        TechnicalBooksAdapter.TechnicalBookListener,
        GeneralBooksAdapter.GeneralBookListener,
        View.OnClickListener,
        NetworkFragment.OnNetworkCallback,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    TextView recentlyViewedTextView, userName, userEmail;

    // Array LIst
    ArrayList<RecentlyViewedDataModel> recentList = new ArrayList<>();
    private ArrayList<SubjectDataModel> subjectList = new ArrayList<>();
    private ArrayList<BooksDataModel> technicalList = new ArrayList<>();
    private ArrayList<BooksDataModel> generalList = new ArrayList<>();
    private ArrayList<BooksDataModel> questionPaperList = new ArrayList<>();


    // View Container
    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout mainLayoutContainer;
    private CardView notesCV, labManualCV, previousYearQPCV;

    private Toolbar mToolbar;

    // Recycler View

    private RecyclerView subjectRecyclerView, technicalBookRecyclerView,
            generalBookRecyclerView;

    // Class Objects initialization
    SessionManager mSessionManager;
    NetworkFragment mNetworkFragment;
    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSessionManager = new SessionManager(this);
        mToolbar = findViewById(R.id.main_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        /*
         *-------------------------Navigation Drawer Layout-------------------------------
         */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.getHeaderView(0);
        userName = navHeaderView.findViewById(R.id.username);
        userEmail = navHeaderView.findViewById(R.id.user_email);


        /*
         *------------check user sign in information-------------
         */
        if (mSessionManager.isLogging()) {
            HashMap<String, String> user = mSessionManager.getUserDetail();
            String name = user.get(SessionManager.NAME);
            String email = user.get(SessionManager.EMAIL);

            userName.setText(name);
            userEmail.setText(email);
        }

        if (!mSessionManager.isLogging()) {
            userName.setText(R.string.app_name);
            userEmail.setText("");
        }

        /*
         *----------------------Inflating the Xml items view through findByViewId method---------------------------------------
         */
        recentlyViewedTextView = findViewById(R.id.recently_visited_text);
        subjectRecyclerView = findViewById(R.id.subject_matters_recycler_view);
        technicalBookRecyclerView = findViewById(R.id.technical_recycler_view);
        generalBookRecyclerView = findViewById(R.id.general_recycler_view);

        /*
         *---------shimmer layout-----------------
         */

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mainLayoutContainer = findViewById(R.id.main_container);
        mainLayoutContainer.setVisibility(View.GONE);


        // CardView

        notesCV = findViewById(R.id.notes_CV);
        labManualCV = findViewById(R.id.lab_manual_CV);
        previousYearQPCV = findViewById(R.id.previous_year_qp_CV);

        notesCV.setOnClickListener(this);
        labManualCV.setOnClickListener(this);
        previousYearQPCV.setOnClickListener(this);
        /*
         *-------------------calling network connection checking network connection----------------------------------
         */

        mNetworkFragment = new NetworkFragment();
        mFragmentManager = getSupportFragmentManager();
        checkNetworkConnection();
    }



    public void implementedMethods() {
        setRecentView();
        getSubjectJson();
        getTechnicalJson();
        getGeneralJson();
    }

    public void refresh() {
        recentList.clear();
        subjectList.clear();
        technicalList.clear();
        generalList.clear();
        questionPaperList.clear();
        implementedMethods();
    }

    public void setRecentView() {
        if (recentList.size() <= 0) {
            recentlyViewedTextView.setVisibility(View.GONE);
        } else {
            recentlyViewedTextView.setVisibility(View.VISIBLE);

            /*
             *---------------Write code here for setting recycler view----------------
             */

        }
    }


    /*
     *-----------------------------Code for Subject Matters--------------------------------
     */


    public void getSubjectJson() {
        String url = Constants.SERVER_URL + "get_subject.php";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String image = jsonObject.getString("thumbnail");
                                String playlistId = jsonObject.getString("playlistId");
                                String thumbnail = "http://13.234.49.231/" + image;


                                subjectList.add(new SubjectDataModel(title, thumbnail, playlistId));
                                Log.d(TAG, "onResponse: " + thumbnail);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        mainLayoutContainer.setVisibility(View.VISIBLE);
                        setSubjectAdapter(subjectList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("error..."+error.getMessage());


            }
        });
        MySingleton.getInstance(this).addTORequestQueue(jsonArrayRequest);
    }


    /*
     *----------------------------------setting up subject adapter------------------------------------
     */

    private void setSubjectAdapter(ArrayList<SubjectDataModel> subjectDataList) {
        SubjectAdapter subjectAdapter = new SubjectAdapter(MainActivity.this, subjectDataList, this);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subjectRecyclerView.setAdapter(subjectAdapter);
    }

    /*
     *------------------------------setting up subject item click listener--------------------------------------
     */

    @Override
    public void onSubjectViewClick(int position) {
        if (mSessionManager.isLogging()) {
            Intent intent = new Intent(MainActivity.this, SpecificSubjectActivity.class);
            intent.putExtra("playlistName", subjectList.get(position).getTitle());
            intent.putExtra("playlistId", subjectList.get(position).getPlaylistId());
            startActivity(intent);
        }else {
            showMessage("Please Login First!");
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }


    /*
     *------------------------------------for menu Items------------------------------
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.refresh: checkNetworkConnection();
                               break;

            case R.id.logout: if (mSessionManager.isLogging()) {
                mSessionManager.logout();
                userName.setText(R.string.app_name);
                userEmail.setText("");
                break;
            }else {
                showMessage("Already logged out");
                break;
            }

            case R.id.login:  if (!mSessionManager.isLogging()) {
                                    startActivity(new Intent(MainActivity.this, Login.class));
                                    break;
                              }else {
                showMessage("Already logged in");
                break;
            }
        }
        return true;
    }


    /*
     *----------------for Toast message-----------------------
     */
    public void showMessage(String message) {
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }


    /*
     *---------Method for fetching Technical Json---------------------------------
     */

    private void getTechnicalJson() {
        String url = Constants.SERVER_URL + "get_technical_books.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("description");
                                String subject = jsonObject.getString("subject");
                                String author = jsonObject.getString("author");
                                String thumbnail = jsonObject.getString("thumbnail");
                                String url = jsonObject.getString("url");
                                String category = jsonObject.getString("category");
                                thumbnail = Constants.SERVER_URL + thumbnail;

                                int cat = Integer.valueOf(category);
                                technicalList.add(new BooksDataModel(title, description, subject, author, thumbnail, url, cat));
                            }
                            setTechnicalBookAdapter(technicalList);
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


    /*
     *---------Setting up technical book adapter---------------------------------
     */

    private void setTechnicalBookAdapter(ArrayList<BooksDataModel> technicalList) {
        TechnicalBooksAdapter technicalBooksAdapter = new TechnicalBooksAdapter(MainActivity.this, technicalList, this);
        technicalBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        technicalBookRecyclerView.setAdapter(technicalBooksAdapter);
    }

    @Override
      public void technicalBookItemClickListener(int position) {
        if (mSessionManager.isLogging()) {
            Intent intent = new Intent(MainActivity.this, BooksActivity.class);
            intent.putExtra("thumbnail",technicalList.get(position).getThumbnail());
            intent.putExtra("title",technicalList.get(position).getTitle());
            intent.putExtra("author",technicalList.get(position).getAuthor());
            intent.putExtra("description",technicalList.get(position).getDescription());
            intent.putExtra("url",technicalList.get(position).getUrl());
            intent.putExtra("subject",technicalList.get(position).getSubject());
            startActivity(intent);
        }else {
            showMessage("Please Login First!");
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private void getGeneralJson() {
        String url = Constants.SERVER_URL + "get_general_books.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("description");
                                String subject = jsonObject.getString("subject");
                                String author = jsonObject.getString("author");
                                String thumbnail = jsonObject.getString("thumbnail");
                                String url = jsonObject.getString("url");
                                String category = jsonObject.getString("category");
                                thumbnail = Constants.SERVER_URL + thumbnail;

                                int cat = Integer.valueOf(category);
                                generalList.add(new BooksDataModel(title, description, subject, author, thumbnail, url, cat));
                            }
                            setGeneralBookAdapter(generalList);
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

    /*
     *------------------------General Book Adapter--------------------------
     */
    private void setGeneralBookAdapter(ArrayList<BooksDataModel> generalList) {
        GeneralBooksAdapter generalBooksAdapter = new GeneralBooksAdapter(this, generalList, this);
        generalBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        generalBookRecyclerView.setAdapter(generalBooksAdapter);
    }

    @Override
    public void generalBookItemClickListener(int position) {
        if (mSessionManager.isLogging()) {
            Intent intent = new Intent(MainActivity.this, BooksActivity.class);
            intent.putExtra("thumbnail",generalList.get(position).getThumbnail());
            intent.putExtra("title",generalList.get(position).getTitle());
            intent.putExtra("author",generalList.get(position).getAuthor());
            intent.putExtra("description",generalList.get(position).getDescription());
            intent.putExtra("url",generalList.get(position).getUrl());
            intent.putExtra("subject",generalList.get(position).getSubject());
            startActivity(intent);
        }else {
            showMessage("Please Login First!");
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }


    /*
     *---------------------Checking Network Connection----------------------
     */

    public void checkNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {
            mFragmentManager.beginTransaction()
                    .remove(mNetworkFragment)
                    .commit();
            refresh();
        }else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.network_fragment, mNetworkFragment)
                    .commit();
        }
    }

    @Override
    public void onNetworkCallback() {
        checkNetworkConnection();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        if (mSessionManager.isLogging()) {
            HashMap<String, String> user = mSessionManager.getUserDetail();
            String name = user.get(SessionManager.NAME);
            String email = user.get(SessionManager.EMAIL);

            userName.setText(name);
            userEmail.setText(email);
        }

        if (!mSessionManager.isLogging()) {
            userName.setText(R.string.app_name);
            userEmail.setText("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmer();
        mainLayoutContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.feedback) {
            startActivity(new Intent(MainActivity.this, Feedback.class));
        }
        if (id == R.id.download) {
            startActivity(new Intent(MainActivity.this, download.class));
        }
        if (id == R.id.about_app) {
            Intent intent = new Intent(MainActivity.this, KnowUs.class);
            intent.putExtra("classname", "app");
            startActivity(intent);

        }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == notesCV) {
            if (mSessionManager.isLogging()) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtra("TOOLBAR_TITLE", "Notes");
                intent.putExtra("PHP_CLASS_NAME", "get_notes.php");
                startActivity(intent);
            }else {
                showMessage("Please Login First!");
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        }

        if (v == labManualCV) {
            if (mSessionManager.isLogging()) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtra("TOOLBAR_TITLE", "Lab Manual");
                intent.putExtra("PHP_CLASS_NAME", "getLabManual.php");
                startActivity(intent);
            }else {
                showMessage("Please Login First!");
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        }

        if (v == previousYearQPCV) {
            if (mSessionManager.isLogging()) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtra("TOOLBAR_TITLE", "Question Papers");
                intent.putExtra("PHP_CLASS_NAME", "getQuestionPaper.php");
                startActivity(intent);
            }
            else {
                showMessage("Please Login First!");
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        }
    }
}


