package io.droidtech.app.binarycalculator.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import io.droidtech.app.R;

public class AboutDevActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);
        Objects.requireNonNull(getSupportActionBar()).hide();
        try {

            TextView tv_version = findViewById(R.id.tv_version);
            String v = "v " + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tv_version.setText(v);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
