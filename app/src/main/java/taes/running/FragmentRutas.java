package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by jorge on 22/04/2016.
 */
public class FragmentRutas extends Fragment {
    private View view;
    public static  JSONArray jsonArray=null;

    public static FragmentRutas newInstance() {
        FragmentRutas fragmentRutas = new FragmentRutas();
        Bundle args = new Bundle();

        try {
            jsonArray=  new JSONArray(Principal.rutas);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return fragmentRutas;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.rutas, container, false);
        final ListView rutasListView = (ListView) rootView.findViewById(R.id.rutasListView);
        adaptadorListaRutas adapter = new adaptadorListaRutas(inflater.getContext(),jsonArray);
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
