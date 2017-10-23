package com.rzc.pullrefreshrecyclerview.lib;

import android.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rzc on 17/10/17.
 */

public abstract class RefreshRecyclerViewBaseSubAdapter<VH extends RefreshRecyclerViewBaseSubHolder> {
    private RefreshRecyclerViewAdapter mRefreshRecyclerViewAdapter;
    Set<Integer> viewTypeSet = new HashSet<Integer>();

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position);

    public abstract int getItemViewType(int position);

    public abstract int getItemCount();

    public int getColumnCount() {
        return 1;
    }

    public float getColumnWeight(int position) {
        return 1;
    }

    public abstract void onItemClick(int position, int itemViewType);

    public void notifyDataSetChanged() {
        mRefreshRecyclerViewAdapter.notifyDataSetChanged();
    };

    void setRefreshRecyclerViewAdapter(RefreshRecyclerViewAdapter refreshRecyclerViewAdapter) {
        mRefreshRecyclerViewAdapter = refreshRecyclerViewAdapter;
    }
}
