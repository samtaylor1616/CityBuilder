package au.edu.staylor.citybuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SETTINGS = 0;
    private Button settingsBtn;
    private Button mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsBtn = findViewById(R.id.settingsBtn);
        mapBtn = findViewById(R.id.mapBtn);
        setOnClicks();
    }

    private void setOnClicks() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Settings activity
                startActivityForResult(SetttingsActivity.getIntent(MainActivity.this), REQUEST_SETTINGS);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to map
                startActivity(MapActivity.getIntent(MainActivity.this));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_SETTINGS) {

        }
    }

}
