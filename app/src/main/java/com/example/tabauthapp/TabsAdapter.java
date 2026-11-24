package com.example.tabauthapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class TabsAdapter extends FragmentStateAdapter {

    private final List<String> titles;

    public TabsAdapter(@NonNull FragmentActivity activity, List<String> titles) {
        super(activity);
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String title = (position >= 0 && position < titles.size()) ? titles.get(position) : "";
        return TabFragment.newInstance(title);
    }

    @Override
    public int getItemCount() {
        return titles != null ? titles.size() : 0;
    }
}
