package com.goodwell42.gcchat.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goodwell42.gcchat.R;
import com.goodwell42.gcchat.adapter.AdapterUserItem;
import com.goodwell42.gcchat.util.UserItemMsg;

import java.util.ArrayList;
import java.util.List;


public class LayoutContacts extends Fragment {

    private View rootView;
    private Context context;
    private List<UserItemMsg> groupMsgList;
    private List<UserItemMsg> contactMsgList;
    private PicAndTextBtn patbBarGroup;
    private PicAndTextBtn patbBarContact;
    private RecyclerView rvGroup;
    private RecyclerView rvContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_contacts, container, false);
        this.context = inflater.getContext();
        initGroupViews();
        initContactViews();
        return rootView;
    }

    private void initGroupViews() {
        patbBarGroup = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar_groups);
        rvGroup = (RecyclerView) rootView.findViewById(R.id.rv_list_groups);

        groupMsgList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            UserItemMsg userItemMsg = new UserItemMsg();
            userItemMsg.setIconID(R.drawable.avastertony);
            userItemMsg.setUsername("Group " + i);
            userItemMsg.setSign("onLine : " + i + "/10");
            groupMsgList.add(userItemMsg);
        }

        AdapterUserItem adapterGroup = new AdapterUserItem(context, groupMsgList);
        rvGroup.setLayoutManager(new LinearLayoutManager(context));
        rvGroup.setAdapter(adapterGroup);

        patbBarGroup.setOnClickListener(new PicAndTextBtn.picAndTextBtnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvGroup.getVisibility() == View.VISIBLE) {
                    rvGroup.setVisibility(View.GONE);
                    patbBarGroup.setImageView(R.drawable.shink);
                } else {
                    rvGroup.setVisibility(View.VISIBLE);
                    patbBarGroup.setImageView(R.drawable.rise);
                }
            }
        });
    }

    private void initContactViews() {
        patbBarContact = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar__contacts);
        rvContact = (RecyclerView) rootView.findViewById(R.id.rv_list_contacts);

        contactMsgList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            UserItemMsg userItemMsg = new UserItemMsg();
            userItemMsg.setIconID(R.drawable.avasterdr);
            userItemMsg.setUsername("Friend " + i);
            userItemMsg.setSign("You know who I am !");
            contactMsgList.add(userItemMsg);
        }

        AdapterUserItem adapterContact = new AdapterUserItem(context, contactMsgList);
        rvContact.setLayoutManager(new LinearLayoutManager(context));
        rvContact.setAdapter(adapterContact);

        patbBarContact.setOnClickListener(new PicAndTextBtn.picAndTextBtnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvContact.getVisibility() == View.VISIBLE) {
                    rvContact.setVisibility(View.GONE);
                    patbBarContact.setImageView(R.drawable.shink);
                } else {
                    rvContact.setVisibility(View.VISIBLE);
                    patbBarContact.setImageView(R.drawable.rise);
                }
            }
        });
    }
}
