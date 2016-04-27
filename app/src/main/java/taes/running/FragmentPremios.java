package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

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


        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(100,100,100,100);
        viewPager.setPageMargin(50);

        viewPager.setAdapter(new AdaptadorPremios(getContext()));
        CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        viewPager.setCurrentItem(3);
        indicator.setViewPager(viewPager);

        return rootView;
    }
}
