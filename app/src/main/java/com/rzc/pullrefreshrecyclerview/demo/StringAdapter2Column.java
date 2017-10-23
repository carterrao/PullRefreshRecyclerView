package com.rzc.pullrefreshrecyclerview.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rzc.pullrefreshrecyclerview.R;
import com.rzc.pullrefreshrecyclerview.lib.RefreshRecyclerViewBaseSubAdapter;
import com.rzc.pullrefreshrecyclerview.lib.RefreshRecyclerViewBaseSubHolder;

import java.util.List;

/**
 * Created by rzc on 17/10/18.
 */

public class StringAdapter2Column extends RefreshRecyclerViewBaseSubAdapter<StringAdapter2Column.StringHolder2Column> {
    private List<String> strList;
    private Context context;

    public StringAdapter2Column(List<String> strList, Context context) {
        this.strList = strList;
        this.context = context;
    }

    @Override
    public StringHolder2Column onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StringHolder2Column(View.inflate(parent.getContext(), R.layout.test_string_item, null));
    }

    @Override
    public void onBindViewHolder(StringHolder2Column holder, int position) {
        if (position < strList.size()) {
            holder.tvContent.setText(strList.get(position));
            holder.itemView.setVisibility(View.VISIBLE);
        } else {//因为后面是补齐的空列，所以需要隐藏
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return MainActivity.HOLDER_TYPE_STRING_2_COLUMN;
    }

    @Override
    public int getItemCount() {
        if (strList == null || strList.size() == 0) {
            return 0;
        }
        int count = strList.size();
        if (count % 2 == 0) {
            return count;
        } else {//由于这里是一行两列，不足时两列时补一列
            return count + 1;
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public float getColumnWeight(int position) {
        if (position == 0 || position == 2) {
            return 2.0f / 3;
        } else if (position == 1) {
            return 4/3.0f;
        }
        return super.getColumnWeight(position);
    }

    @Override
    public void onItemClick(int position, int itemViewType) {
        Toast.makeText(context, "点击了" + strList.get(position), Toast.LENGTH_SHORT).show();
    }

    static class StringHolder2Column extends RefreshRecyclerViewBaseSubHolder {
        public TextView tvContent;

        public StringHolder2Column(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}
