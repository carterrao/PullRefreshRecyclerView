package com.rzc.pullrefreshrecyclerview.lib;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzc on 17/10/17.
 */

class RefreshRecyclerViewAdapter extends RecyclerView.Adapter {
    private RefreshRecyclerView mRefreshRecyclerView;
    List<RefreshRecyclerViewBaseSubAdapter> subAdapterList = new ArrayList<RefreshRecyclerViewBaseSubAdapter>();

    public RefreshRecyclerViewAdapter(RefreshRecyclerView refreshRecyclerView) {
        mRefreshRecyclerView = refreshRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RefreshRecyclerView.HOLDER_TYPE_REFRESH) {
            return mRefreshRecyclerView.refreshHolder;
        } else if (viewType == RefreshRecyclerView.HOLDER_TYPE_LOAD_MORE) {
            return mRefreshRecyclerView.loadMoreHolder;
        } else {
            for (RefreshRecyclerViewBaseSubAdapter subAdapter : subAdapterList) {
                if (subAdapter.viewTypeSet.contains(viewType)) {
                    return subAdapter.onCreateViewHolder(parent, viewType);
                }
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RefreshRecyclerViewBaseSubHolder) {
            int pos = 1;
            for (int i = 0; i < subAdapterList.size(); i++) {
                RefreshRecyclerViewBaseSubAdapter subAdapter = subAdapterList.get(i);
                int subCount = subAdapter.getItemCount();
                if (position - pos < subCount) {
                    RefreshRecyclerViewBaseSubHolder subHolder = (RefreshRecyclerViewBaseSubHolder) holder;
                    subHolder.subAdapter = subAdapter;
                    subHolder.subPosition = position - pos;
                    subAdapter.onBindViewHolder(subHolder, position - pos);
                    return;
                } else {
                    pos += subCount;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return RefreshRecyclerView.HOLDER_TYPE_REFRESH;
        } else if (position == getItemCount() - 1) {
            return RefreshRecyclerView.HOLDER_TYPE_LOAD_MORE;
        }
        int pos = 1;
        for (int i = 0; i < subAdapterList.size(); i++) {
            RefreshRecyclerViewBaseSubAdapter subAdapter = subAdapterList.get(i);
            int subCount = subAdapter.getItemCount();
            if (position - pos < subCount) {
                int viewType = subAdapter.getItemViewType(position - pos);
                subAdapter.viewTypeSet.add(viewType);
                return viewType;
            } else {
                pos += subCount;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int count = 1 + 1;//refresh + loadMore
        for (RefreshRecyclerViewBaseSubAdapter adapter : subAdapterList) {
            count += adapter.getItemCount();
        }
        return count;
    }

    public boolean addSubAdapter(RefreshRecyclerViewBaseSubAdapter adapter) {
        if (!subAdapterList.contains(adapter)) {
            subAdapterList.add(adapter);
            adapter.setRefreshRecyclerViewAdapter(this);
            return true;
        }
        return false;
    }

    public boolean insertSubAdapter(RefreshRecyclerViewBaseSubAdapter adapter, RefreshRecyclerViewBaseSubAdapter anchorAdapter, boolean before) {
        if (!subAdapterList.contains(adapter)) {
            for (int i = 0; i < subAdapterList.size(); i++) {
                if (subAdapterList.get(i) == anchorAdapter) {
                    subAdapterList.add(before ? i : i + 1, adapter);
                    adapter.setRefreshRecyclerViewAdapter(this);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeSubAdapter(RefreshRecyclerViewBaseSubAdapter adapter) {
        return subAdapterList.remove(adapter);
    }
}
