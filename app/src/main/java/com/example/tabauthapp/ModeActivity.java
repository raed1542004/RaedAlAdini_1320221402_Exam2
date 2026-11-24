package com.example.tabauthapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Arrays;
import java.util.List;

public class ModeActivity extends AppCompatActivity {

    private ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button modeBtn = findViewById(R.id.btn_set_mode);
        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModeMenu(v);
            }
        });

        TabLayout tabs = findViewById(R.id.tab_layout);
        pager = findViewById(R.id.view_pager);

        final List<String> tabNames = Arrays.asList(
                getString(R.string.tab_first),
                getString(R.string.tab_second),
                getString(R.string.tab_third)
        );

        TabsAdapter adapter = new TabsAdapter(this, tabNames);
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabs, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabNames.get(position));
            }
        }).attach();
    }

    private void showModeMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.mode_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.opt_light) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    return true;
                } else if (id == R.id.opt_night) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    return true;
                } else if (id == R.id.opt_system) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (pager != null && pager.getCurrentItem() > 0) {
            pager.setCurrentItem(0);
            return;
        }
        super.onBackPressed();
    }
}
