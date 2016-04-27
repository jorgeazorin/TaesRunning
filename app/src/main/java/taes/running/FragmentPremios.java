package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by jorge on 22/04/2016.
 */
public class FragmentPremios extends Fragment {

    public static FragmentPremios newInstance() {
        FragmentPremios fragmentRutas = new FragmentPremios();
        Bundle args = new Bundle();
        return fragmentRutas;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.premios, container, false);
        return rootView;
    }
}
