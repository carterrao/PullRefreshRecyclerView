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

public class StringAdapter3Column extends RefreshRecyclerViewBaseSubAdapter<StringAdapter3Column.StringHolder3Column> {
    private List<String> strList;
    private Context context;

    public StringAdapter3Column(List<String> strList, Context context) {
        this.strList = strList;
        this.context = context;
    }

    @Override
    public StringHolder3Column onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StringHolder3Column(View.inflate(parent.getContext(), R.layout.test_string_item, null));
    }

    @Override
    public void onBindViewHolder(StringHolder3Column holder, int position) {
        if (position < strList.size()) {
            holder.tvContent.setText(strList.get(position));
            holder.itemView.setVisibility(View.VISIBLE);
        } else {//因为后面是补齐的空列，所以需要隐藏
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return MainActivity.HOLDER_TYPE_STRING_3_COLUMN;
    }

    @Override
    public int getItemCount() {
        if (strList == null || strList.size() == 0) {
            return 0;
        }

        int count = strList.size();
        int mod = (count + 2) % 3;//因为getColumnWeight设置了有2个列占2倍列宽，所以后面加2，如果有n列列宽比较多，后面加的数是n % getColumnCount
        if (mod == 0) {
            return count;
        } else {//由于这里是一行3列，不足时3列时补齐3列
            return count + 3 - mod;
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public float getColumnWeight(int position) {
        if (position == 0 || position == 3) {
            return 2;
        }
        return super.getColumnWeight(position);
    }

    @Override
    public void onItemClick(int position, int itemViewType) {
        Toast.makeText(context, "点击了" + strList.get(position), Toast.LENGTH_SHORT).show();
    }

    static class StringHolder3Column extends RefreshRecyclerViewBaseSubHolder {
        public TextView tvContent;

        public StringHolder3Column(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}
