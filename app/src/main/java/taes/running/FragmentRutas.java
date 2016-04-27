package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

/**
 * Created by jorge on 22/04/2016.
 */
public class FragmentRutas extends Fragment {
    private View view;

    // newInstance constructor for creating fragment with arguments
    public static FragmentRutas newInstance() {
        FragmentRutas fragmentRutas = new FragmentRutas();
        Bundle args = new Bundle();
        return fragmentRutas;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.rutas, container, false);

        final ListView rutasListView = (ListView) rootView.findViewById(R.id.rutasListView);
       adaptadorListaRutas adapter = new adaptadorListaRutas(inflater.getContext());
        rutasListView.setAdapter(adapter);
        final SwipeNumberPicker swipeNumberPicker =(SwipeNumberPicker) rootView.findViewById(R.id.number_picker);
        swipeNumberPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                boolean isValueOk = (newValue & 1) == 0;
                if (isValueOk)
                    swipeNumberPicker.setValue(newValue,false);

                return true;
            }
        });



       return rootView;
    }


}
