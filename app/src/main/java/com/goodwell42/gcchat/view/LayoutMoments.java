package com.goodwell42.gcchat.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goodwell42.gcchat.R;
import com.goodwell42.gcchat.adapter.AdapterMomentItem;
import com.goodwell42.gcchat.util.MomentMsg;

import java.util.ArrayList;
import java.util.List;


public class LayoutMoments extends Fragment {

    private View rootView;
    private List<MomentMsg> momentMsgList;
    private RecyclerView momentRecyclerView;
    private AdapterMomentItem adapterMomentItem;
    private TextView tvNewMoment;
    private Button btnSend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_moments, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {
        momentRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_moments);
        tvNewMoment = (TextView) rootView.findViewById(R.id.tv_moment_new);
        btnSend = (Button) rootView.findViewById(R.id.btn_moment_send);

        momentMsgList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            MomentMsg momentMsg = new MomentMsg();
            momentMsg.setUsername("Stark " + i);
            momentMsg.setIconID(R.drawable.avasterwe);
            momentMsg.setMoment("moments,moments,moments,moments,moments,moments" + i);
            momentMsg.setGood((i % 3) == 1 ? R.drawable.good : R.drawable.ungood);
            momentMsgList.add(momentMsg);
        }

        adapterMomentItem = new AdapterMomentItem(getContext(), momentMsgList);
        momentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        momentRecyclerView.setAdapter(adapterMomentItem);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MomentMsg momentMsg = new MomentMsg();
                momentMsg.setUsername("Stark ");
                momentMsg.setIconID(R.drawable.avasterwe);
                momentMsg.setMoment(tvNewMoment.getText().toString());
                momentMsg.setGood(R.drawable.ungood);
                momentMsgList.add(momentMsg);
            }
        });
    }
}
