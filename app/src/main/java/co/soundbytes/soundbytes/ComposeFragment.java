package co.soundbytes.soundbytes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Olumide on 10/3/2015.
 */
public class ComposeFragment extends TitledFragment {
    private String title = "Mix it!";

    @Override
    public String getTitle(){
        return title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_audio, container, false);
        return view;
    }
}
