package developer.ishank.forlalit.sir.lsacademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.adapter.SubjectDetailAdapter;
import developer.ishank.forlalit.sir.lsacademy.classes.Constants;
import developer.ishank.forlalit.sir.lsacademy.classes.MySingleton;
import developer.ishank.forlalit.sir.lsacademy.datamodel.SubjectDetailsDataModel;
import developer.ishank.forlalit.sir.lsacademy.fragments.NetworkFragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SpecificSubjectActivity extends AppCompatActivity implements SubjectDetailAdapter.OnSubjectDetailListener,
        NetworkFragment.OnNetworkCallback {
    private String playlistId, playlistName;
    private ArrayList<SubjectDetailsDataModel> mSubjectDetailList = new ArrayList<>();
    private RecyclerView recyclerView;

    private ShimmerFrameLayout mShimmerViewContainer;
    private NetworkFragment mNetworkFragment;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_subject);

        recyclerView = findViewById(R.id.specific_subject_recycler_view);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);


        mNetworkFragment = new NetworkFragment();
        mFragmentManager = getSupportFragmentManager();
        checkNetworkConnection();
        Toolbar toolbar = findViewById(R.id.subject_toolbar);
        toolbar.setTitle(playlistName);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getIncomingIntent() {
        if (getIntent().hasExtra("playlistName") && getIntent().hasExtra("playlistId")){
            playlistId = getIntent().getStringExtra("playlistId");
            playlistName = getIntent().getStringExtra("playlistName");
            Log.d("SpecificSubjectActivity", "onResponse: "+playlistId);
            getJsonRequest();
        }
    }


    /*
     *--------------------setting up httpConnection with Youtube Data Api for fetching response in JSON Object-------------------
     */

    public void getJsonRequest() {
        String url = Constants.PLAY_LIST_ITEM_URL+"?part="+Constants.PART+"&playlistId="+playlistId+"&maxResults=50&key="+Constants.API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                /*
                                 *------------------Creating Snippet Object for fetching items like title, description, thumbnail Object etc---------------
                                 */
                                JSONObject snippetObject = jsonObject1.getJSONObject("snippet");
                                String title = snippetObject.getString("title");
                                String description = snippetObject.getString("description");


                                /*
                                 *------------------Creating thumbnail Object for fetching thumbnail url----------------
                                 */

                                JSONObject thumbnailObject = snippetObject.getJSONObject("thumbnails").getJSONObject("high");
                                String thumbnail = thumbnailObject.getString("url");


                                /*
                                 *-----------------creating resource Json Object for fetching videoId here----------
                                 */

                                JSONObject resourceObject = snippetObject.getJSONObject("resourceId");
                                String videoId = resourceObject.getString("videoId");

                                mSubjectDetailList.add(new SubjectDetailsDataModel(title,description,thumbnail,videoId));
                            }
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            setAdapter(mSubjectDetailList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addTORequestQueue(stringRequest);
    }
    public void setAdapter(ArrayList<SubjectDetailsDataModel> dataList) {
        SubjectDetailAdapter subjectDetailAdapter = new SubjectDetailAdapter(SpecificSubjectActivity.this,dataList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(SpecificSubjectActivity.this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(subjectDetailAdapter);
        recyclerView.setHasFixedSize(true);
    }


    /*
     *---------------------Function to perform on recycler View items click-------------------------------------------
     */

    @Override
    public void onClickSubjectDetailListener(int position) {
        Intent intent = new Intent(SpecificSubjectActivity.this, VideoActivity.class);
        intent.putExtra("title", mSubjectDetailList.get(position).getTitle());
        intent.putExtra("description", mSubjectDetailList.get(position).getDescription());
        intent.putExtra("thumbnail",mSubjectDetailList.get(position).getThumbnail());
        intent.putExtra("videoId", mSubjectDetailList.get(position).getVideoId());
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    public void checkNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {
            mFragmentManager.beginTransaction()
                    .remove(mNetworkFragment)
                    .commit();
            getIncomingIntent();
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
}
