package com.goodwell42.gcchat.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goodwell42.gcchat.R;
import com.goodwell42.gcchat.adapter.AdapterUserItem;
import com.goodwell42.gcchat.util.UserItemMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LayoutChats extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private List<UserItemMsg> userItemMsgList = new ArrayList<>();
    private Context context;
    private AdapterUserItem adapterUserItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_chats, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {

        context = getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chatsRecycleView);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(userItemMsgList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(userItemMsgList, i, i - 1);
                    }
                }
                adapterUserItem.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                userItemMsgList.remove(position);
                adapterUserItem.notifyItemRemoved(position);
            }
        };

        loadData();

        adapterUserItem = new AdapterUserItem(context, userItemMsgList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterUserItem);
    }

    private void loadData() {
        for (int i = 0; i < 12; i++) {
            UserItemMsg userItemMsg = new UserItemMsg();
            userItemMsg.setIconID(R.drawable.avastertony);
            userItemMsg.setUsername("Goodwell");
            userItemMsg.setSign("You know who I am !");
            userItemMsgList.add(userItemMsg);
        }
    }
}
