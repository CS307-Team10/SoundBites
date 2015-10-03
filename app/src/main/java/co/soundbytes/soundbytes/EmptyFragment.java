package co.soundbytes.soundbytes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Olumide on 10/3/2015.
 */
public class EmptyFragment extends TitledFragment {
    private static String[] titles = new String[]{"Page A", "Page B", "Page C"};
    private static LinkedList<String> list = new LinkedList(Arrays.asList(titles));
    private String title;
    @Override
    public String getTitle(){
        if(title == null) {
            if(list.size() == 0)
                list = new LinkedList(Arrays.asList(titles));
            title = list.pop();
        }
        return title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(getTitle());
        text.setTextSize(20 * getResources().getDisplayMetrics().density);
        text.setPadding(20, 20, 20, 20);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);

        return layout;
    }
}
