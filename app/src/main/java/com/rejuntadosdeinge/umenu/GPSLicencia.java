package com.rejuntadosdeinge.umenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class GPSLicencia extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_licencia);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String licencia = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
        TextView tv = (TextView) findViewById(R.id.gps_licencia_text);

        if(licencia != null) {
            tv.setText(licencia);
        } else {
            tv.setText("Google Play Services no instalado.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

