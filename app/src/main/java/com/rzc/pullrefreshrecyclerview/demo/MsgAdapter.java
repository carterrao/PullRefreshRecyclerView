package com.rzc.pullrefreshrecyclerview.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rzc.pullrefreshrecyclerview.R;
import com.rzc.pullrefreshrecyclerview.demo.data.MyMsg;
import com.rzc.pullrefreshrecyclerview.demo.data.ReceivedMsg;
import com.rzc.pullrefreshrecyclerview.lib.RefreshRecyclerViewBaseSubAdapter;
import com.rzc.pullrefreshrecyclerview.lib.RefreshRecyclerViewBaseSubHolder;

import java.util.List;

/**
 * Created by rzc on 17/10/18.
 */

public class MsgAdapter extends RefreshRecyclerViewBaseSubAdapter {
    private List msgList;
    private Context context;

    public MsgAdapter(List msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
    }

    @Override
    public RefreshRecyclerViewBaseSubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MainActivity.HOLDER_TYPE_RECEIVED_MSG) {
            return new ReceivedHolder(View.inflate(parent.getContext(), R.layout.received_msg_item, null));
        } else if (viewType == MainActivity.HOLDER_TYPE_MY_MSG) {
            return new MyHolder(View.inflate(parent.getContext(), R.layout.my_msg_item, null), msgList);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RefreshRecyclerViewBaseSubHolder holder, int position) {
        if (holder instanceof ReceivedHolder) {
            ReceivedHolder receivedHolder = (ReceivedHolder) holder;
            ReceivedMsg msg = (ReceivedMsg) msgList.get(position);
            receivedHolder.tvName.setText(msg.name);
            receivedHolder.tvContent.setText(msg.content);
        } else if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            MyMsg msg = (MyMsg) msgList.get(position);
            myHolder.tvContent.setText(msg.content);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return msgList.get(position) instanceof ReceivedMsg
                ? MainActivity.HOLDER_TYPE_RECEIVED_MSG : MainActivity.HOLDER_TYPE_MY_MSG;
    }

    @Override
    public int getItemCount() {
        return msgList != null ? msgList.size() : 0;
    }

    @Override
    public void onItemClick(int position, int itemViewType) {
        if (itemViewType == MainActivity.HOLDER_TYPE_RECEIVED_MSG) {
            ReceivedMsg msg = ((ReceivedMsg) msgList.get(position));
            Toast.makeText(context, "点击了" + msg.name + "的消息："
                    + msg.content, Toast.LENGTH_SHORT).show();
        } else if (itemViewType == MainActivity.HOLDER_TYPE_MY_MSG) {
            Toast.makeText(context, "点击了我发的消息："
                    + ((MyMsg) msgList.get(position)).content, Toast.LENGTH_SHORT).show();
        }
    }

    static class ReceivedHolder extends RefreshRecyclerViewBaseSubHolder {
        public TextView tvName;
        public TextView tvContent;

        public ReceivedHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }

    static class MyHolder extends RefreshRecyclerViewBaseSubHolder {
        public TextView tvContent;

        public MyHolder(final View itemView, final List msgList) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tvContent);
            itemView.findViewById(R.id.ivTest).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyMsg msg = (MyMsg) msgList.get(getSubPosition());
                    Toast.makeText(itemView.getContext(), "点击了我的头像：" + msg.content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
