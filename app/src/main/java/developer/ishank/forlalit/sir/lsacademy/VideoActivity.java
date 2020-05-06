package developer.ishank.forlalit.sir.lsacademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.account.Login;
import developer.ishank.forlalit.sir.lsacademy.account.Synthesize;
import developer.ishank.forlalit.sir.lsacademy.adapter.CommentVideoActivityAdapter;
import developer.ishank.forlalit.sir.lsacademy.classes.AppController;
import developer.ishank.forlalit.sir.lsacademy.classes.CommentTime;
import developer.ishank.forlalit.sir.lsacademy.classes.FullScreenHelper;
import developer.ishank.forlalit.sir.lsacademy.classes.SentenceCase;
import developer.ishank.forlalit.sir.lsacademy.account.SessionManager;
import developer.ishank.forlalit.sir.lsacademy.datamodel.CommentSectionDataModel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoActivity extends AppCompatActivity implements TextWatcher,
        View.OnClickListener {

    private ArrayList<CommentSectionDataModel> commentList = new ArrayList<>();
    private YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    LinearLayout noCommentContainer;
    TextView titleText, descriptionText, countCommentDetailActivityTV, noCommentCountMessage;
    EditText commentBodyVideoActivityET;
    Button videoDownloadBtn, commentVideoActivityBtn, mShowCommentBtn, mCloseCommentBtn;
    RecyclerView commentVideoActivityRecyclerView;
    private BottomSheetBehavior mBottomSheetBehavior;

    SessionManager sessionManager;
    private YouTubePlayerTracker youTubePlayerTracker;
    String videoId, username, posted_by, count, setCountedComments;
    int TOTAL_COMMENTS;
    float time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        commentBodyVideoActivityET = findViewById(R.id.video_activity_edit_text);
        commentBodyVideoActivityET.addTextChangedListener(this);

        sessionManager = new SessionManager(this);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        videoDownloadBtn = findViewById(R.id.video_download_btn);
        youTubePlayerTracker = new YouTubePlayerTracker();
        youTubePlayerView.addYouTubePlayerListener(youTubePlayerTracker);
        titleText = findViewById(R.id.title);
        descriptionText = findViewById(R.id.description);


        /*
         *-----------Comment Section---------------
         */

        HashMap<String, String> user = sessionManager.getUserDetail();
        username = user.get(SessionManager.NAME);
        posted_by = user.get(SessionManager.EMAIL);
        noCommentCountMessage = findViewById(R.id.count_message);
        noCommentContainer = findViewById(R.id.no_comment_container);
        countCommentDetailActivityTV = findViewById(R.id.count_comment_video_activity_tv);
        commentVideoActivityRecyclerView = findViewById(R.id.comment_video_activity_RV);
        commentVideoActivityBtn = findViewById(R.id.comment_video_activity_btn);
        commentVideoActivityBtn.setOnClickListener(this);

        //comment section finish

        /*
         *----------Bottom sheet behaviour
         */

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mShowCommentBtn = findViewById(R.id.show_comment);
        mCloseCommentBtn = findViewById(R.id.comment_bottom_sheet_close_btn);
        mShowCommentBtn.setOnClickListener(this);
        mCloseCommentBtn.setOnClickListener(this);

        //bottom sheet finish

        videoDownloadBtn.setOnClickListener(this);

        getIncomingIntent();

        getLifecycle().addObserver(youTubePlayerView);

        if (savedInstanceState != null) {
            final String videoID = savedInstanceState.getString("Video_Id");
            final float t = savedInstanceState.getFloat("time");
            Log.d("TAG", "onRestoreInstanceState: "+videoID+" "+t);
            playVideo(videoID,time);
        }else {
            playVideo(videoId,time);
        }

        getComments();
    }

    public void getIncomingIntent() {
        if (getIntent().hasExtra("title") && getIntent().hasExtra("description") && getIntent().hasExtra("videoId")) {
            String title = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("description");
            videoId = getIntent().getStringExtra("videoId");
            final String thumbnail = getIntent().getStringExtra("thumbnail");

            title = SentenceCase.getTitleSentenceCase(title);
            description = SentenceCase.getDescriptionSentenceCase(description);

            titleText.setText(title);
            descriptionText.setText(description);

        }
    }



    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else
            super.onBackPressed();
    }





    private void addFullScreenListenerToPlayer() {

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                fullScreenHelper.enterFullScreen();


//                addCustomActionsToPlayer();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                fullScreenHelper.exitFullScreen();

//                removeCustomActionsFromPlayer();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoId != null) {
            outState.putString("Video_Id", videoId);
        }
        outState.putFloat("time",time);
    }

    public void playVideo(final String videoId,final float time) {
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                YouTubePlayerUtils.loadOrCueVideo(youTubePlayer,getLifecycle(),videoId,time);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        addFullScreenListenerToPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                if (state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.UNKNOWN || state == PlayerConstants.PlayerState.UNSTARTED
                        || state == PlayerConstants.PlayerState.ENDED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
                    time = youTubePlayerTracker.getCurrentSecond();
                    Log.d("TAG", "onStateChange: "+time);
                }
            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                time = second;
            }


        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!sessionManager.isLogging()) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void getComments() {
        String URL = "http://13.234.49.231/includes/classes/getComment.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            count = jsonObject.getString("count");
                            TOTAL_COMMENTS = Integer.valueOf(count);
                            if (TOTAL_COMMENTS > 1) {
                                setCountedComments = TOTAL_COMMENTS + " Comments";
                                noCommentContainer.setVisibility(View.GONE);
                                commentVideoActivityRecyclerView.setVisibility(View.VISIBLE);
                                noCommentCountMessage.setVisibility(View.GONE);
                            }else if (TOTAL_COMMENTS == 1){
                                setCountedComments = TOTAL_COMMENTS + " Comment";
                                noCommentContainer.setVisibility(View.GONE);
                                commentVideoActivityRecyclerView.setVisibility(View.VISIBLE);
                                noCommentCountMessage.setVisibility(View.GONE);
                            }else {
                                setCountedComments = TOTAL_COMMENTS + " Comment";
                                commentVideoActivityRecyclerView.setVisibility(View.GONE);
                                noCommentCountMessage.setVisibility(View.VISIBLE);
                                noCommentContainer.setVisibility(View.VISIBLE);
                            }
                            countCommentDetailActivityTV.setText(setCountedComments);
                            JSONArray jsonArray = jsonObject.getJSONArray("comment");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject commentJSONObject = jsonArray.getJSONObject(i);
                                String username = commentJSONObject.getString("name");
                                String posted_by = commentJSONObject.getString("posted_by");
                                String body = commentJSONObject.getString("body");
                                String timeStamp = commentJSONObject.getString("timestamp");
                                String id = commentJSONObject.getString("id");
                                commentList.add(new CommentSectionDataModel(username, posted_by, body, timeStamp, Integer.valueOf(id)));
                            }

                            setCommentAdapter(commentList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("error...");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> comment = new HashMap<>();
                comment.put("videoId",videoId);
                return comment;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setCommentAdapter(ArrayList<CommentSectionDataModel> commentList) {
        CommentVideoActivityAdapter commentVideoActivityAdapter = new CommentVideoActivityAdapter(this, commentList);
        commentVideoActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        commentVideoActivityRecyclerView.setAdapter(commentVideoActivityAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == commentVideoActivityBtn) {
            String body = commentBodyVideoActivityET.getText().toString();
            String timestamp = CommentTime.getCurrentTime();
            noCommentContainer.setVisibility(View.GONE);
            noCommentCountMessage.setVisibility(View.GONE);
            commentVideoActivityRecyclerView.setVisibility(View.VISIBLE);
            if (!sessionManager.isLogging()) {
                showMessage("Not logged in ! Please sign in first.");
            }else {
                insertComment(body, timestamp);
                commentBodyVideoActivityET.setText("");
            }

        }

        if (v == videoDownloadBtn) {
            String message = "https://www.youtube.com/watch?v=" + videoId;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }

        if (v == mShowCommentBtn) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            Log.d("mBottomSheetBehavior", "onClick: clicked");
        }
        if (v == mCloseCommentBtn) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void insertComment(final String body, final String timestamp)  {
        String URL = "http://13.234.49.231/includes/classes/insertComment.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject commentJSONObject = new JSONObject(response);
                                String username = commentJSONObject.getString("username");
                                String posted_by = commentJSONObject.getString("email");
                                String body = commentJSONObject.getString("body");
                                String timeStamp = commentJSONObject.getString("timestamp");
                                String id = commentJSONObject.getString("id");
                                commentList.add(new CommentSectionDataModel(username, posted_by, body, timeStamp, Integer.valueOf(id)));
                                setCommentAdapter(commentList);
                                //setting up total comments

                                TOTAL_COMMENTS = TOTAL_COMMENTS + 1;
                                if (TOTAL_COMMENTS > 1) {
                                    setCountedComments = TOTAL_COMMENTS + " Comments";
                                }else {
                                    setCountedComments = TOTAL_COMMENTS + " Comment";
                                }
                                countCommentDetailActivityTV.setText(setCountedComments);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("error...");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> comment = new HashMap<>();
                comment.put("posted_by", posted_by);
                comment.put("username", username);
                comment.put("body", body);
                comment.put("videoId", videoId);
                comment.put("timestamp", timestamp);
                return comment;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> user = sessionManager.getUserDetail();
        username = user.get(SessionManager.NAME);
        posted_by = user.get(SessionManager.EMAIL);
    }
}
