package com.himusharier.searchviewapplication.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.himusharier.searchviewapplication.R;
import com.himusharier.searchviewapplication.adapter.CustomGridAdapter;
import com.himusharier.searchviewapplication.model.GridItem;

import java.util.ArrayList;
import java.util.List;

public class SearchableGridViewActivity extends AppCompatActivity {
    private EditText searchEditText;
    private GridView gridView;
    private CustomGridAdapter adapter;
    private List<GridItem> originalList;
    private List<GridItem> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_searchable_grid_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enable the Up button (back arrow) in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        setupData();
        setupSearch();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initViews() {
        searchEditText = findViewById(R.id.searchEditText);
        gridView = findViewById(R.id.gridView);
    }

    private void setupData() {
        originalList = new ArrayList<>();
        // Sample data - replace with your actual data
        originalList.add(new GridItem("Apple", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Banana", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Cherry", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Date", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Elderberry", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Fig", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Grape", R.drawable.ic_launcher_foreground));
        originalList.add(new GridItem("Honeydew", R.drawable.ic_launcher_foreground));

        filteredList = new ArrayList<>(originalList);
        adapter = new CustomGridAdapter(this, filteredList);
        gridView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGrid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterGrid(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (GridItem item : originalList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}