package taes.running;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jorge on 18/05/2016.
 */
public class adaptadorListaEventos extends BaseAdapter {
    private Context context;
    private JSONArray jsonArrayTodosLosEventos = null;
    private JSONArray jsonArrayEnLosQueParticipo = null;
    private int NumInstances = 0;

    public adaptadorListaEventos(Context context, JSONArray jsonArrayTodosLosEventos, JSONArray jsonArrayEnLosQueParticipo) {
        super();
        this.context = context;
        this.jsonArrayTodosLosEventos = jsonArrayTodosLosEventos;
        this.jsonArrayEnLosQueParticipo = jsonArrayEnLosQueParticipo;
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    @Override
    public int getCount() {
        return jsonArrayTodosLosEventos.length()+jsonArrayEnLosQueParticipo.length()+2;
    }

    @Override
    public Object getItem(int position) {
        if(position>jsonArrayEnLosQueParticipo.length()+1)
            try {
                return jsonArrayTodosLosEventos.getJSONObject(jsonArrayTodosLosEventos.length()-jsonArrayEnLosQueParticipo.length()-2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            try {
                return jsonArrayEnLosQueParticipo.getJSONObject(jsonArrayEnLosQueParticipo.length()-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(position>jsonArrayEnLosQueParticipo.length()+1)
            try {
                return jsonArrayTodosLosEventos.getJSONObject(jsonArrayTodosLosEventos.length()-jsonArrayEnLosQueParticipo.length()-2).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            try {
                return jsonArrayEnLosQueParticipo.getJSONObject(jsonArrayEnLosQueParticipo.length()-1).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        //return null;
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position==0){
                TextView t = new TextView(context);
                t.setText("Eventos en los que participo");
                t.setPadding(20, 0, 20, 20);
            if(jsonArrayEnLosQueParticipo.length()==0){
                t.setVisibility(View.GONE);
                t.setPadding(0, 0, 0, 0);
                t.setHeight(0);
            }
                return t;

        }else if(position==jsonArrayEnLosQueParticipo.length()+1){
                TextView t = new TextView(context);
                t.setText("Todos los eventos");
                t.setPadding(20, 50, 20, 20);
            if(jsonArrayEnLosQueParticipo.length()==0){
                t.setVisibility(View.GONE);
                t.setPadding(0, 0, 0, 0);
                t.setHeight(0);

            }
                return t;

        }else {
            if (position <= jsonArrayEnLosQueParticipo.length()) {
                position=position-1;
                try {
                    final ImageView myImageView;
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View rowView = inflater.inflate(R.layout.ruta, parent, false);
                    ImageView imageView = (ImageView) rowView.findViewById(R.id.Ruta_imagen);
                    TextView nombreRuta = (TextView) rowView.findViewById(R.id.Ruta_nombre);
                    TextView distanciaRuta = (TextView) rowView.findViewById(R.id.Ruta_texto_pequeno);
                    nombreRuta.setText(jsonArrayEnLosQueParticipo.getJSONObject(position).getString("name"));
                    distanciaRuta.setText(jsonArrayEnLosQueParticipo.getJSONObject(position).getString("description"));
                    System.out.println("kkkkkk:   "+(position));
                    Glide.with(context)
                            .load("http://maps.googleapis.com/maps/api/staticmap?center=Spain&size=100x100&key=AIzaSyBMa3yVWGGSr1fcwI8LlVpk_7GTni2JJTE")
                            .placeholder(R.drawable.staticmap)
                            .into(imageView);
                    return rowView;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{

                position=position-jsonArrayEnLosQueParticipo.length()-2;
                System.out.println("kkkkkkkkkkkk: "+position+" : ");

                //return jsonArrayTodosLosEventos.getJSONObject(jsonArrayTodosLosEventos.length()-jsonArrayEnLosQueParticipo.length()-2).getInt("id");
                final ImageView myImageView;

                LayoutInflater inflater = LayoutInflater.from(context);
                View rowView = inflater.inflate(R.layout.ruta, parent, false);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.Ruta_imagen);
                TextView nombreRuta = (TextView) rowView.findViewById(R.id.Ruta_nombre);
                TextView distanciaRuta = (TextView) rowView.findViewById(R.id.Ruta_texto_pequeno);
                try {
                    nombreRuta.setText(jsonArrayTodosLosEventos.getJSONObject(position).getString("name"));
                    distanciaRuta.setText("" + jsonArrayTodosLosEventos.getJSONObject(position ).getString("description"));

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
        return null;

    }

}