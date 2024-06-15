package com.android.safezone.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.safezone.Adapter.WebKeyBlockAdapter;
import com.android.safezone.Database.BlockDatabase;
import com.android.safezone.Model.WebKeyModel;
import com.android.safezone.R;
import com.android.safezone.Utility.AccessibilityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class KeywordBlockActivity extends AppCompatActivity {
    private final BlockDatabase blockDatabase = new BlockDatabase(this);
    private final List<WebKeyModel> webKeyModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private TextView noKeyBlock;
    private Button blockButton;
    private WebKeyBlockAdapter webKeyBlockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_block);

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        setUpButton();
        getBlockedWebsite();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.blocked_keyword);
        searchEditText = findViewById(R.id.search_add_keyword);
        noKeyBlock = findViewById(R.id.no_keyword_block);
        blockButton = findViewById(R.id.add_key_button);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupSearchView() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void setUpButton() {
        blockButton.setOnClickListener(v -> {
            if (webKeyModelList.contains(new WebKeyModel(searchEditText.getText().toString()))) {
                Toast.makeText(this, "Keyword already blocked", Toast.LENGTH_SHORT).show();
            } else if (Pattern.matches("^(http|https)://.*", searchEditText.getText().toString()) ||
                    Pattern.matches("^(www\\.)?([a-zA-Z0-9]+\\.)+[a-zA-Z]{2,}$", searchEditText.getText().toString())
            ) {
                Toast.makeText(this, "Please enter a keyword, not a URL", Toast.LENGTH_SHORT).show();
            } else {
                if (new AccessibilityUtil().isAccessibilitySettingsOn(this)) {
                    if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                        Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(this, NewScheduleActivity.class);
                        intent.putExtra("name", searchEditText.getText().toString().split(" ")[0].toLowerCase());
                        intent.putExtra("type", "key");
                        startActivity(intent);
                    }
                } else {
                    Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(i);
                }
            }
        });
    }

    private void filter(String text) {
        if (!webKeyModelList.isEmpty()) {
            List<WebKeyModel> filteredList = new ArrayList<>();
            for (WebKeyModel item : webKeyModelList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            webKeyBlockAdapter.updateListBlock(filteredList);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void getBlockedWebsite() {
        List<HashMap<String, String>> list = blockDatabase.readAllRecordsKey();
        if (!list.isEmpty()) {
            for (HashMap<String, String> map : list) {
                webKeyModelList.add(new WebKeyModel(Objects.requireNonNull(map.get("name"))));
            }
        }

        if (webKeyModelList.isEmpty()) {
            noKeyBlock.setVisibility(TextView.VISIBLE);
        } else {
            Set<String> keys = new HashSet<>();
            List<WebKeyModel> filteredList = new ArrayList<>();

            for (WebKeyModel model : webKeyModelList) {
                if (keys.add(model.getName())) {
                    filteredList.add(model);
                }
            }

            webKeyBlockAdapter = new WebKeyBlockAdapter(filteredList, false);
            recyclerView.setAdapter(webKeyBlockAdapter);

            webKeyBlockAdapter.setOnItemClickListener(view -> {
                int position = recyclerView.getChildAdapterPosition(view);
                WebKeyModel app = webKeyBlockAdapter.getItemAt(position);
                launchActivity(app.getName());
            });
        }
    }

    private void launchActivity(String webName) {
        Intent intent = new Intent(this, EditScheduleActivity.class);
        intent.putExtra("name", webName);
        intent.putExtra("type", "key");
        startActivity(intent);
    }

    public void finish(View v) {
        finish();
    }
}