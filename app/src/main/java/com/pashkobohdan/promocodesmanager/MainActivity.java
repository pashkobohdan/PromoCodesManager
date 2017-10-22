package com.pashkobohdan.promocodesmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.pashkobohdan.promocodesmanager.data.schema.AppDTO;
import com.pashkobohdan.promocodesmanager.util.Constants;
import com.pashkobohdan.promocodesmanager.util.DialogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static String APP_NAME_EXTRA_KEY = "AppKeyExtraKey";

    @BindView(R.id.apps_recycler_view)
    ListView listView;

    List<AppDTO> appDTOList;

    private RefreshableArrayAdapter adapter;

    @OnClick(R.id.add_new_app_button)
    void addNewAppClick() {
        DialogUtils.showInputDialog(MainActivity.this, getString(R.string.new_app_name), new DialogUtils.Callback<String>() {
            @Override
            public void call(String name) {
                AppDTO newAppDTO = new AppDTO(name);
                boolean saveAppResult = AppsPreference.addAppDTO(newAppDTO, MainActivity.this);
                if (saveAppResult) {
                    appDTOList.add(newAppDTO);
                    adapter.setObjects(appDTOList);
                    adapter.notifyDataSetChanged();
                } else {
                    DialogUtils.showAlert(MainActivity.this, null, getString(R.string.app_is_already_used), null, null);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle(getString(R.string.apps_title));

        appDTOList = AppsPreference.getAllAppList(MainActivity.this);
        adapter = new RefreshableArrayAdapter(MainActivity.this, R.layout.app_list_item, appDTOList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppDTO dto = appDTOList.get(position);
                Intent intent = new Intent(MainActivity.this, CouponListActivity.class);
                intent.putExtra(APP_NAME_EXTRA_KEY, dto.getName());
                startActivity(intent);
            }
        });
    }

    class RefreshableArrayAdapter extends ArrayAdapter<AppDTO> {
        private List<AppDTO> objects;

        public RefreshableArrayAdapter(Context context, int resource, List<AppDTO> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.app_list_item, parent, false);
            TextView appName = (TextView) rowView.findViewById(R.id.app_name_text_view);
            ImageView deleteAppButton = (ImageView) rowView.findViewById(R.id.delete_app);

            final AppDTO dto = getItem(position);
            appName.setText(dto == null || dto.getName() == null ? Constants.EMPTY : dto.getName());

            deleteAppButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showConfirm(MainActivity.this, null, getString(R.string.delete_app_confirm), new DialogUtils.ClickCallback() {
                        @Override
                        public void call() {
                            AppsPreference.deleteApp(MainActivity.this, dto);
                            appDTOList.remove(dto);
                            adapter.setObjects(appDTOList);
                            adapter.notifyDataSetChanged();
                        }
                    }, null);
                }
            });

            return rowView;
        }

        public void setObjects(List<AppDTO> objects) {
            this.objects = objects;
        }
    }
}
