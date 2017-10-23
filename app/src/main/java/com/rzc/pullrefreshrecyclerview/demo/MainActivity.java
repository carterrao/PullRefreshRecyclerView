package com.rzc.pullrefreshrecyclerview.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rzc.pullrefreshrecyclerview.R;
import com.rzc.pullrefreshrecyclerview.demo.data.MockData;
import com.rzc.pullrefreshrecyclerview.lib.RefreshRecyclerView;
import com.rzc.pullrefreshrecyclerview.lib.RefreshRecyclerViewSingleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private RefreshRecyclerView mRecyclerView;

    private List msgList = new ArrayList();
    private MsgAdapter msgAdapter;

    private List<String> strList2Column = new ArrayList<String>();
    private StringAdapter2Column stringAdapter2Column;

    private List<String> strList3Column = new ArrayList<String>();
    private StringAdapter3Column stringAdapter3Column;

    //注意，SubAdapter中ItemViewType值必须都不能相同，
    //并且也不能跟RefreshRecyclerView.HOLDER_TYPE_REFRESH和HOLDER_TYPE_LOAD_MORE值相同
    public static final int HOLDER_TYPE_MY_MSG = 1;
    public static final int HOLDER_TYPE_RECEIVED_MSG = 2;
    public static final int HOLDER_TYPE_STRING_2_COLUMN = 3;
    public static final int HOLDER_TYPE_STRING_3_COLUMN = 4;
    public static final int HOLDER_TYPE_HEADER = 5;
    public static final int HOLDER_TYPE_FOOTER = 6;
    public static final int HOLDER_TYPE_LINE = 7;
    public static final int HOLDER_TYPE_PAGER = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        setRefreshLoadMore();

        //演示怎么添加一个header或类似的单个itemView
        TextView textView = new TextView(this);
        textView.setText("刚进页面不显示内容，请试试下拉刷新");
        int padding = (int) (getResources().getDisplayMetrics().density * 12);
        textView.setPadding(padding, padding, padding, padding);
        mRecyclerView.addSubAdapter(new RefreshRecyclerViewSingleViewAdapter(HOLDER_TYPE_HEADER, textView));

        //演示添加一个ViewPager
        View pager = View.inflate(this, R.layout.test_view_pager, null);
        ViewPager viewPager = pager.findViewById(R.id.viewPager);
        final TextView tvIndex = pager.findViewById(R.id.tvIndex);
        tvIndex.setText("1/6");
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                ImageView item = new ImageView(MainActivity.this);
                if (position % 2 == 0) {
                    item.setImageResource(R.mipmap.ic_launcher);
                } else {
                    item.setImageResource(R.mipmap.ic_launcher_round);
                }
                container.addView(item);
                return item;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvIndex.setText((position + 1) + "/6");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRecyclerView.addSubAdapter(new RefreshRecyclerViewSingleViewAdapter(HOLDER_TYPE_PAGER, pager));

        msgAdapter = new MsgAdapter(msgList, this);
        mRecyclerView.addSubAdapter(msgAdapter);

        stringAdapter2Column = new StringAdapter2Column(strList2Column, this);
        mRecyclerView.addSubAdapter(stringAdapter2Column);

        //在2列的adapter和3列的adapter之间插入间距12dp
        View line = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (getResources().getDisplayMetrics().density * 12));
        line.setLayoutParams(lp);
        mRecyclerView.addSubAdapter(new RefreshRecyclerViewSingleViewAdapter(HOLDER_TYPE_LINE, line));

        stringAdapter3Column = new StringAdapter3Column(strList3Column, this);
        mRecyclerView.addSubAdapter(stringAdapter3Column);

        //演示怎么添加一个footer或类似的单个itemView
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.mipmap.ic_launcher_round);
        mRecyclerView.addSubAdapter(new RefreshRecyclerViewSingleViewAdapter(HOLDER_TYPE_FOOTER, iv));
    }

    private void setRefreshLoadMore() {
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setRefreshLoadMoreListener(new RefreshRecyclerView.RefreshLoadMoreListener() {
            @Override
            public void onRefresh() {
                MockData.mockQueryMsgList(new MockData.OnMsgListListener() {
                    @Override
                    public void onMsgList(List msgList) {
                        mRecyclerView.stopRefresh();

                        MainActivity.this.msgList.clear();
                        MainActivity.this.msgList.addAll(msgList);

                        MainActivity.this.strList2Column.clear();
                        MainActivity.this.strList3Column.clear();

                        mRecyclerView.notifyDataSetChanged();

                        mRecyclerView.setPullLoadEnable(true);
                        mRecyclerView.loadMoreResetState();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                MockData.mockQueryStringList(new MockData.OnStringListListener() {
                    @Override
                    public void onStringList(List<String> strList) {
                        if (strList2Column.size() == 0) {
                            strList2Column.addAll(strList);
                            stringAdapter2Column.notifyDataSetChanged();
                            mRecyclerView.loadMoreCurrOver();
                        } else if (strList3Column.size() == 0) {
                            strList3Column.addAll(strList);
                            stringAdapter3Column.notifyDataSetChanged();
                            mRecyclerView.loadMoreNoMoreState();
                        }
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(MainActivity.this, "加载更多数据失败了", Toast.LENGTH_SHORT).show();
                        mRecyclerView.loadMoreCurrOver();
                    }
                }, false); //改成true看模拟请求失败，即回调onFailed
            }
        });
    }
}
