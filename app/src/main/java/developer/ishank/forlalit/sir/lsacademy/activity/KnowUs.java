package developer.ishank.forlalit.sir.lsacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.fragments.LSAcademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class KnowUs extends AppCompatActivity {

    LSAcademy lsAcademy;
    FragmentManager fragmentManager;
    String classname, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_us);
        Toolbar toolbar = findViewById(R.id.know_us_toolbar);

        lsAcademy = new LSAcademy();
        fragmentManager = getSupportFragmentManager();

        getIncomingIntent();
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classname.equals("app")) {
                    fragmentManager.beginTransaction()
                            .remove(lsAcademy)
                            .commit();

                    finish();
                }

            }
        });


    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("classname")) {
            classname = getIntent().getStringExtra("classname");

            if (classname.equals("app")) {
                title = getString(R.string.about_ls_academy);
                fragmentManager.beginTransaction()
                        .replace(R.id.know_us_fragment, lsAcademy)
                        .commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (classname.equals("app")) {
            fragmentManager.beginTransaction()
                    .remove(lsAcademy)
                    .commit();

            finish();
        }
    }
}
