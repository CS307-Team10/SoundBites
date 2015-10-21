package com.soundbytes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Olumide on 10/21/2015.
 */
public class InfoLinearLayout extends LinearLayout {
    private TextView name;
    private TextView content;

    /**
     * Constructor
     * @param c Activity context
     */
    public InfoLinearLayout(Context c){
        super(c);
        init();
    }

    /**
     * onstructor
     * @param c the activity context
     * @param attr stuff
     */
    public InfoLinearLayout(Context c, AttributeSet attr){
        super(c, attr);
        init();
    }

    /**
     * Constructor
     * @param c the activity context
     * @param attr stuff
     * @param defStyle more stuff
     */
    public InfoLinearLayout(Context c, AttributeSet attr, int defStyle){
        super(c, attr, defStyle);
        init();
    }


    /**
     * Constructor. This is the main constructor used
     * @param c activity context
     * @param infoName the name of the information that is displayed
     * @param infoContent the information that is displayed
     */
    public InfoLinearLayout(Context c, String infoName, String infoContent){
        super(c);
        init();
        setDetails(infoName, infoContent);
    }

    /**
     * Convenience method used to avoid duplication code in the constructors
     */
    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_info_layout, this);
        name = (TextView)view.findViewById(R.id.name);
        content = (TextView)view.findViewById(R.id.content);
    }

    /**
     * This method sets the name and content textViews
     * @param infoName the name of the information to be displayed
     * @param infoContent the content to be displayed
     */
    public void setDetails(String infoName, String infoContent){
        name.setText(infoName);
        content.setText(infoContent);
    }
}
