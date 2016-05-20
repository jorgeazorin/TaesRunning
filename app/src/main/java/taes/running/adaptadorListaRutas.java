package taes.running;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.Charset;
import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

/**
 * Created by jorge on 22/04/2016.
 */
public class adaptadorListaRutas  extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray=null;
    private int NumInstances=0;
    public  adaptadorListaRutas(Context context, JSONArray jsonArray){
        super();
        this.context=context;
        this.jsonArray=jsonArray;
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }
    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        try {
            return jsonArray.getJSONObject(getCount()-position-1).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  return position;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position=getCount()-position-1;
        final ImageView myImageView;

        LayoutInflater inflater =  LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.ruta, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.Ruta_imagen);
        TextView nombreRuta = (TextView) rowView.findViewById(R.id.Ruta_nombre);
        TextView distanciaRuta = (TextView) rowView.findViewById(R.id.Ruta_texto_pequeno);
        try {
            nombreRuta.setText(jsonArray.getJSONObject(position).getString("name"));
            distanciaRuta.setText("Distancia: "+jsonArray.getJSONObject(position).getString("distancia")+" km");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(context)
            .load("http://maps.googleapis.com/maps/api/staticmap?center=Spain&size=100x100&key=AIzaSyBMa3yVWGGSr1fcwI8LlVpk_7GTni2JJTE")
            .placeholder(R.drawable.staticmap)
            .into(imageView);
        return rowView;

    }
}

