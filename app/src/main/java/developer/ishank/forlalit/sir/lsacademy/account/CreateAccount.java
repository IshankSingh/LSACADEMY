package developer.ishank.forlalit.sir.lsacademy.account;

import androidx.appcompat.app.AppCompatActivity;
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

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    TextView signUpTV, signUpMessageTV;
    Button signUpBtn;
    EditText signUpUsernameET, signUpEmailET, signUpPassET, signUpConfPassET;
    int count = 1;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        signUpTV = findViewById(R.id.sign_up_tv);
        signUpMessageTV = findViewById(R.id.sign_up_message_tv);
        signUpBtn = findViewById(R.id.sign_up_btn);
        signUpUsernameET = findViewById(R.id.sign_up_username_et);
        signUpEmailET = findViewById(R.id.sign_up_email_et);
        signUpPassET = findViewById(R.id.sign_up_pass_et);
        signUpConfPassET = findViewById(R.id.sign_up_conf_pass_et);

        signUpTV.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        signUpBtn.setEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
    }

    @Override
    public void onClick(View v) {
        if (v == signUpTV) {
            defaultIntent(Login.class);
            finish();
        }

        if (v == signUpBtn) {
            if (validate()) {
                count++;
                mProgressDialog.show();
                if (count > 1) {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setBackground(getResources().getDrawable(R.drawable.custom_disable_button_bg));
                    signUpBtn.setTextColor(getResources().getColor(R.color.colorAccent));
                }

                String username, email, pass, firstName, lastName, middleName;
                username = signUpUsernameET.getText().toString().trim();
                email = signUpEmailET.getText().toString().trim();
                pass = signUpPassET.getText().toString().trim();

                String[] user = Synthesize.synthesizeUsername(username);
                int n = user.length;
                if (n == 1) {
                    firstName = user[0];
                    username = firstName;
                } else if (n == 2) {
                    firstName = user[0];
                    lastName = user[1];
                    username = firstName + " " + lastName;
                } else {
                    firstName = user[0];
                    middleName = user[1];
                    lastName = user[2];
                    username = firstName + " " + middleName + " " + lastName;
                }
                email = Synthesize.synthesizeEmail(email);
                pass = Synthesize.synthesizePassword(pass);
                insertData(username,email,pass);

            }
        }
    }

    public void defaultIntent (Class c) {
        Intent intent = new Intent(CreateAccount.this,c);
        startActivity(intent);
    }

    public boolean validate() {
        if (Synthesize.isEmpty(signUpUsernameET)) {
            signUpUsernameET.setError("Please enter username");
            signUpUsernameET.requestFocus();
            return false;

        }else if (Synthesize.isEmpty(signUpEmailET)) {
            signUpEmailET.setError("Please enter email");
            signUpEmailET.requestFocus();
            return false;

        }else if (Synthesize.isEmpty(signUpPassET)) {
            signUpPassET.setError("Please enter password");
            signUpPassET.requestFocus();
            return false;

        }else if (Synthesize.isEmpty(signUpConfPassET)) {
            signUpConfPassET.setError("Please confirm your password");
            signUpConfPassET.requestFocus();
            return false;

        }else if (signUpPassET.getText().toString().length() <= 6) {
            signUpPassET.setError("Password should be more than 6 characters long");
            return false;

        }else if (!signUpPassET.getText().toString().equals(signUpConfPassET.getText().toString())) {
            signUpConfPassET.setError("Password does not match with confirm password");
            return false;
        }
        return true;
    }

    public void insertData(final String username, final String email, final String password) {
        String url = Constants.SERVER_URL+"register.php";
        //String url = "http://192.168.56.1/LS_ACADEMY/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("TAG", "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            boolean err = jsonObject.getBoolean("error");
                            signUpMessageTV.setVisibility(View.VISIBLE);


                            if (err) {
                                signUpUsernameET.setText("");
                                signUpEmailET.setText("");
                                signUpPassET.setText("");
                                signUpConfPassET.setText("");
                                signUpBtn.setEnabled(true);
                                signUpBtn.setBackground(getResources().getDrawable(R.drawable.custom_button_bg));
                                signUpBtn.setTextColor(getResources().getColor(R.color.white));
                                mProgressDialog.dismiss();
                                signUpMessageTV.setText(message);
                                signUpMessageTV.setTextColor(getResources().getColor(R.color.red));
                            }else {
                                signUpUsernameET.setText("");
                                signUpEmailET.setText("");
                                signUpPassET.setText("");
                                signUpConfPassET.setText("");
                                signUpBtn.setEnabled(false);
                                signUpBtn.setBackground(getResources().getDrawable(R.drawable.custom_disable_button_bg));
                                signUpBtn.setTextColor(getResources().getColor(R.color.colorAccent));
                                signUpMessageTV.setText(message);
                                mProgressDialog.dismiss();
                                signUpMessageTV.setTextColor(getResources().getColor(R.color.green));

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            defaultIntent(Login.class);
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
                register.put("username",username);
                register.put("email",email);
                register.put("password",password);
                return register;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

}
