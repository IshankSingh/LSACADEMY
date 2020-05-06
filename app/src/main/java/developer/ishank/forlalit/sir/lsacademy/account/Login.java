package developer.ishank.forlalit.sir.lsacademy.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.classes.AppController;
import developer.ishank.forlalit.sir.lsacademy.classes.Constants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView signInMessageTV, signInRegisterTV;
    EditText signInEmailET, signInPassET;
    Button signInButton;

    SessionManager mSessionManager;
    int count = 1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInMessageTV = findViewById(R.id.sign_in_message_tv);
        signInRegisterTV = findViewById(R.id.sign_in_tv);
        signInEmailET = findViewById(R.id.sign_in_email_et);
        signInPassET = findViewById(R.id.sign_in_pass_et);
        signInButton = findViewById(R.id.sign_in_btn);

        mSessionManager = new SessionManager(this);
        signInRegisterTV.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

    }

    @Override
    public void onClick(View v) {
        if (v == signInRegisterTV) {
            defaultIntent(CreateAccount.class);
            finish();
        }

        if (v == signInButton) {

            if (validate()) {

                progressDialog.show();
                count++;
                if (count > 1) {
                    signInButton.setEnabled(false);
                    signInButton.setBackground(getResources().getDrawable(R.drawable.custom_disable_button_bg));
                    signInButton.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                String email, password;

                email = signInEmailET.getText().toString().trim();
                email = Synthesize.synthesizeEmail(email);

                password = signInPassET.getText().toString().trim();
                password = Synthesize.synthesizePassword(password);

                requestRestApi(email, password);
            }
        }
    }

    public boolean validate() {
        if (Synthesize.isEmpty(signInEmailET)) {
            signInEmailET.setError("Please enter email address");
            signInEmailET.requestFocus();
            return false;
        }

        if (Synthesize.isEmpty(signInPassET)) {
            signInPassET.setError("Please enter password");
            signInPassET.requestFocus();
            return false;
        }

        return true;
    }

    public void defaultIntent (Class c) {
        Intent intent = new Intent(Login.this,c);
        startActivity(intent);
    }

    public void requestRestApi(final String email, final String password) {
         String url = Constants.SERVER_URL+"login.php";
        //String url = "http://192.168.56.1/LS_ACADEMY/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("TAG", "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            boolean err = jsonObject.getBoolean("error");
                            signInMessageTV.setVisibility(View.VISIBLE);


                            if (err) {
                                signInEmailET.setText("");
                                signInPassET.setText("");
                                signInButton.setEnabled(true);
                                signInButton.setBackground(getResources().getDrawable(R.drawable.custom_button_bg));
                                signInButton.setTextColor(getResources().getColor(R.color.white));
                                progressDialog.dismiss();
                                signInMessageTV.setText(message);
                                signInMessageTV.setTextColor(getResources().getColor(R.color.red));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                            }else {
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                mSessionManager.createSession(name, email);
                                signInEmailET.setText("");
                                signInPassET.setText("");
                                signInButton.setEnabled(false);
                                signInButton.setBackground(getResources().getDrawable(R.drawable.custom_disable_button_bg));
                                signInButton.setTextColor(getResources().getColor(R.color.colorAccent));
                                progressDialog.dismiss();
                                signInMessageTV.setText(message);
                                signInMessageTV.setTextColor(getResources().getColor(R.color.green));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> register = new HashMap<>();
                register.put("email",email);
                register.put("password",password);
                return register;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
