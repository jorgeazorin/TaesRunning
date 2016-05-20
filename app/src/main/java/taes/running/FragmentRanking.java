package taes.running;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.json.JSONArray;
import org.json.JSONException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jorge on 22/04/2016.
 */
public class FragmentRanking extends Fragment {

    public static FragmentRanking newInstance() {
        FragmentRanking fragmentRutas = new FragmentRanking();
        Bundle args = new Bundle();
        return fragmentRutas;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.vp_item, container, false);
        final ListView listaRanking = (ListView) rootView.findViewById(R.id.ranking_usuarios);
        JSONArray jsonArray= null;
        try {
            System.out.println("kkkk el ranking es"+Principal.ranking);
            jsonArray = new JSONArray(Principal.ranking);
            adaptadorRanking adaptadorRanking=new adaptadorRanking(inflater.getContext(),jsonArray);
            listaRanking.setAdapter(adaptadorRanking);
        } catch (JSONException e) {
        }

        return rootView;
    }
}
