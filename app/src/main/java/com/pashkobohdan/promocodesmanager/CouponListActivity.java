package com.pashkobohdan.promocodesmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pashkobohdan.promocodesmanager.data.preference.AppsPreference;
import com.pashkobohdan.promocodesmanager.util.Constants;
import com.pashkobohdan.promocodesmanager.util.DialogUtils;
import com.pashkobohdan.promocodesmanager.util.NullUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouponListActivity extends AppCompatActivity {

    private static final String COUPON_CLIPBOARD_KEY = "coupon";
    private static final int MAX_COUPON_LENGTH = 6;
    private static final String END_ELLIPSIZE = "...";

    private String appName;
    private List<String> couponsList;

    @OnClick(R.id.add_new_coupons_button)
    void addCouponsClick() {
        DialogUtils.showMultiLineInputDialog(CouponListActivity.this, getString(R.string.input_coupons_hint), new DialogUtils.Callback<String>() {
            @Override
            public void call(String s) {
                String lines[] = s.split("\\r?\\n");
                List<String> coupons = new ArrayList<>();
                for (String line : lines) {
                    String[] commaSeparatedCoupons = line.intern().split(",");
                    for (String couponsString : commaSeparatedCoupons) {
                        if (!NullUtils.emptyString(couponsString))
                            coupons.addAll(Arrays.asList(couponsString));
                    }
                }

                AppsPreference.addCouponsForApp(CouponListActivity.this, appName, coupons);
                couponsList.addAll(coupons);
                listAdapter.setObjects(couponsList);
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @BindView(R.id.coupons_recycler_view)
    ListView couponsListView;
    @BindView(R.id.activity_coupon_list)
    CoordinatorLayout coordinatorLayout;


    RefreshableCouponListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        ButterKnife.bind(this);

        setTitle(getString(R.string.coupons_title));

        Intent intent = getIntent();
        appName = intent.getStringExtra(MainActivity.APP_NAME_EXTRA_KEY);

        if (NullUtils.emptyString(appName)) {
            DialogUtils.showAlert(CouponListActivity.this, null, getString(R.string.wrong_app_name),
                    new DialogUtils.ClickCallback() {
                        @Override
                        public void call() {
                            finish();
                        }
                    }, null);
        }

        couponsList = AppsPreference.getAllCouponsOfApp(CouponListActivity.this, appName);

        listAdapter = new RefreshableCouponListAdapter(CouponListActivity.this, R.layout.coupons_list_item, couponsList);
        couponsListView.setAdapter(listAdapter);
        couponsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String coupon = couponsList.get(position);
                DialogUtils.showAlert(CouponListActivity.this, getString(R.string.coupon_dialog_title), coupon, null, null);
            }
        });
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public boolean copyToClipboard(Context context, String text) {
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                        .getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(COUPON_CLIPBOARD_KEY, text);
                clipboard.setPrimaryClip(clip);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    class RefreshableCouponListAdapter extends ArrayAdapter<String> {
        private List<String> objects;

        public RefreshableCouponListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String coupon = couponsList.get(position);

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.coupons_list_item, parent, false);

            TextView number = (TextView) view.findViewById(R.id.coupons_number);
            TextView couponShortText = (TextView) view.findViewById(R.id.coupon_short);
            ImageView delete = (ImageView) view.findViewById(R.id.delete_coupon);
            ImageView share = (ImageView) view.findViewById(R.id.share_coupon);
            ImageView copy = (ImageView) view.findViewById(R.id.copy_coupon);

            couponShortText.setText(NullUtils.getLinesWithMaxLength(coupon, MAX_COUPON_LENGTH).concat(END_ELLIPSIZE));
            number.setText((position + 1) + Constants.EMPTY);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showConfirm(CouponListActivity.this, null, getString(R.string.delete_coupon_alert), new DialogUtils.ClickCallback() {
                        @Override
                        public void call() {
                            deleteCoupon(coupon);
                        }
                    }, null);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, coupon);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(CouponListActivity.this, coupon);
                    Snackbar.make(coordinatorLayout, R.string.coupon_was_copied, Snackbar.LENGTH_LONG).setAction(R.string.delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteCoupon(coupon);
                        }
                    }).show();
                }
            });

            return view;
        }

        public void setObjects(List<String> objects) {
            this.objects = objects;
        }
    }

    private void deleteCoupon(String coupon) {
        AppsPreference.deleteCouponsForApp(CouponListActivity.this, appName, coupon);
        listAdapter.notifyDataSetChanged();
        couponsList.remove(coupon);
        listAdapter.setObjects(couponsList);
        listAdapter.notifyDataSetChanged();
    }
}
