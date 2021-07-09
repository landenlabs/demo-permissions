/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.system.runtimepermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {

    private static final int REQUEST_GPS = 0;
    private static final int REQUEST_BG_LOC = 1;
    private static String[] PERMISSIONS_GPS =
            {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static String[] PERMISSIONS_GPS_BG =
            {Manifest.permission.ACCESS_BACKGROUND_LOCATION };
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.sample_main_layout);
        mLayout.findViewById(R.id.get_gps_permission).setOnClickListener(this);
        mLayout.findViewById(R.id.get_bg_permission).setOnClickListener(this);
    }

    public void getGpsPermission() {
        if (hasPerm( PERMISSIONS_GPS[0])) {
            showGotGpsPermission();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_GPS, REQUEST_GPS);
        }
    }

    public void getBgLocationPermission() {
        if (hasPerm(PERMISSIONS_GPS_BG)) {
            showGotBgLocPermission();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_GPS_BG, REQUEST_BG_LOC);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        boolean gotPerm = true;
        for (int idx = 0; idx < grantResults.length; idx++) {
            if (grantResults[idx] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, permissions[idx] + " not granted",
                        Snackbar.LENGTH_SHORT)
                        .show();
                gotPerm = false;
            }
        }

        switch (requestCode) {
            case REQUEST_GPS:
                if (gotPerm) showGotGpsPermission();
                break;
            case REQUEST_BG_LOC:
                if (gotPerm) showGotBgLocPermission();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean hasPerm(String ... permissions) {
        for (String perm : permissions) {
            if (ActivityCompat.checkSelfPermission(this,perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void showGotGpsPermission() {
        TextView tv = mLayout.findViewById(R.id.status_gps_permission);
        tv.setText(R.string.gps_permission_granted);
    }
    private void showGotBgLocPermission() {
        TextView tv = mLayout.findViewById(R.id.status_bg_permission);
        tv.setText(R.string.background_permission_granted);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.get_gps_permission) {
            getGpsPermission();
        } else if (id == R.id.get_bg_permission) {
            getBgLocationPermission();
        }
    }
}
