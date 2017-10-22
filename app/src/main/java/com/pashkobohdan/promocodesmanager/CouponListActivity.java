package com.pashkobohdan.promocodesmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pashkobohdan.promocodesmanager.util.DialogUtils;
import com.pashkobohdan.promocodesmanager.util.NullUtils;

public class CouponListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);

        Intent intent = getIntent();
        String appName = intent.getStringExtra(MainActivity.APP_NAME_EXTRA_KEY);

        if(NullUtils.emptyString(appName)) {
            DialogUtils.shoAlert(CouponListActivity.this, getString(R.string.default_alert_title));
        }
    }
}
