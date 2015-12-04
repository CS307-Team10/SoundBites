package com.soundbytes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * Created by Olumide on 10/3/2015.
 */
public class EmptyFragment extends TitledFragment {
    private String title = "News Feed";

    @Override
    public String getTitle(){
        return title;
    }

    @Override
    public void onVisible(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Just adding text to the empty fragment
        TextView text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(getTitle());
        text.setTextSize(20 * getResources().getDisplayMetrics().density);
        text.setPadding(20, 20, 20, 20);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);

        return layout;
    }
}
