package developer.ishank.forlalit.sir.lsacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.account.SessionManager;
import developer.ishank.forlalit.sir.lsacademy.account.Synthesize;
import developer.ishank.forlalit.sir.lsacademy.classes.MySingleton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameET, userEmailET, descriptionET;
    private Button btn;
    private SessionManager mSessionManager;
    private TextView feedbackTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.feedback_toolbar);
        toolbar.setTitle(R.string.feedback);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userNameET = findViewById(R.id.feedback_name);
        userEmailET = findViewById(R.id.feedback_email);
        descriptionET = findViewById(R.id.feedback_description);
        feedbackTV = findViewById(R.id.feedback_message);
        feedbackTV.setVisibility(View.GONE);

        btn = findViewById(R.id.feedback_btn);
        btn.setOnClickListener(this);

        mSessionManager = new SessionManager(this);
        if (mSessionManager.isLogging()) {
            HashMap<String, String> user = mSessionManager.getUserDetail();
            String name = user.get(SessionManager.NAME);
            String email = user.get(SessionManager.EMAIL);
            userNameET.setText(name);
            userEmailET.setText(email);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn) {
           if (validate()) {
               String user_name = userNameET.getText().toString();
               String user_email = userEmailET.getText().toString();
               String description = descriptionET.getText().toString();

               sendFeedback(user_name, user_email, description);
           }
        }
    }

    public boolean validate() {
        if (Synthesize.isEmpty(userNameET)) {
            userNameET.setError("Please enter your name");
            userNameET.requestFocus();
            return false;
        }
        if (Synthesize.isEmpty(userEmailET)) {
            userEmailET.setError("Please enter your email");
            userEmailET.requestFocus();
            return false;
        }
        if (Synthesize.isEmpty(descriptionET)) {
            descriptionET.setError("Please enter your feedback");
            descriptionET.requestFocus();
            return false;
        }
        return true;
    }

    public void sendFeedback (final String user_name, final String user_email, final String description) {
        String url = "http://13.234.49.231/feedback.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        feedbackTV.setVisibility(View.VISIBLE);
                        feedbackTV.setText(response);
                        descriptionET.setText(" ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> feedMap = new HashMap<>();
                feedMap.put("user_name", user_name);
                feedMap.put("user_email", user_email);
                feedMap.put("description", description);
                return feedMap;
            }
        };
        MySingleton.getInstance(this).addTORequestQueue(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSessionManager.isLogging()) {
            HashMap<String, String> user = mSessionManager.getUserDetail();
            String name = user.get(SessionManager.NAME);
            String email = user.get(SessionManager.EMAIL);
            userNameET.setText(name);
            userEmailET.setText(email);
            feedbackTV.setVisibility(View.GONE);
        }
    }
}
