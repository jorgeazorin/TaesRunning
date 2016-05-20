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
public class adaptadorNotificaciones  extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray=null;
    private int NumInstances=0;
    public  adaptadorNotificaciones(Context context, JSONArray jsonArray){
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
            return jsonArray.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  return position;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =  LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.notificacion, parent, false);
        TextView texto = (TextView) rowView.findViewById(R.id.notificacion_texto);
        try {
            texto.setText(jsonArray.getJSONObject(position).getString("cuerpo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;

    }
}

