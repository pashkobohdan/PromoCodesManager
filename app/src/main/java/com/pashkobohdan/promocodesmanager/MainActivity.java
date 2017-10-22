package com.pashkobohdan.promocodesmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pashkobohdan.promocodesmanager.data.preference.AppsPreference;
import com.pashkobohdan.promocodesmanager.data.schema.AppDTO;
import com.pashkobohdan.promocodesmanager.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static String APP_NAME_EXTRA_KEY = "AppKeyExtraKey";

    @BindView(R.id.apps_recycler_view)
    ListView listView;
    List<AppDTO> appDTOList;

    @OnClick(R.id.add_new_app_button)
    void addNewAppClick() {
        showNewAppDialog(new Callback<String>() {
            @Override
            public void call(String name) {
                AppsPreference.addAppDTO(new AppDTO(name), MainActivity.this);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        appDTOList = AppsPreference.getAllAppList(MainActivity.this);
        listView.setAdapter(new ArrayAdapter<AppDTO>(MainActivity.this, R.layout.app_list_item, appDTOList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.app_list_item, parent, false);
                TextView appName = (TextView) rowView.findViewById(R.id.app_name_text_view);

                AppDTO dto = getItem(position);
                appName.setText(dto == null || dto.getName() == null ? Constants.EMPTY : dto.getName());

                return rowView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppDTO dto = appDTOList.get(position);
                Intent intent=  new Intent(MainActivity.this, CouponListActivity.class);
                intent.putExtra(APP_NAME_EXTRA_KEY, dto.getName());
                startActivity(intent);
            }
        });
    }

    private void showNewAppDialog(@NonNull final Callback<String> newAppNameCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_app_name);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAppNameCallback.call(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public interface Callback<T> {
        void call(T t);
    }
}
