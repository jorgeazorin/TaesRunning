package taes.running;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jorge on 19/05/2016.
 */
public class adaptadorRanking extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray=null;
    private int NumInstances=0;
    public  adaptadorRanking(Context context, JSONArray jsonArray){
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
        try {
            LayoutInflater inflater =  LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.usuarioenranking, parent, false);
            RelativeLayout relativeLayout = (RelativeLayout) rowView.findViewById(R.id.usuarioranking_fondonumero);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.usuarioraking_img);
            TextView nombre = (TextView) rowView.findViewById(R.id.usuarioranking_nombre);
            TextView num = (TextView) rowView.findViewById(R.id.usuarioranking_num);
            TextView puntos = (TextView) rowView.findViewById(R.id.usuarioranking_puntos);

            nombre.setText(jsonArray.getJSONObject(position).getString("name"));
            num.setText(String.valueOf(position+1));
            puntos.setText(String.valueOf((jsonArray.length()-position)*1000+(jsonArray.length()-position)*23+(jsonArray.length()-position)*45+(jsonArray.length()-position)*4234));
     /*       if(position<2)
                imageView.setBackgroundDrawable( inflater.getContext().getResources().getDrawable(R.drawable.animals25) );
            else if(position<3)
                imageView.setBackgroundDrawable( inflater.getContext().getResources().getDrawable(R.drawable.animals1) );
            else if(position<7)
                imageView.setBackgroundDrawable( inflater.getContext().getResources().getDrawable(R.drawable.animals2) );
            else if(position<10)
                imageView.setBackgroundDrawable( inflater.getContext().getResources().getDrawable(R.drawable.animals3) );
            else if(position<12)
                imageView.setBackgroundDrawable( inflater.getContext().getResources().getDrawable(R.drawable.animals4) );
*/
            if(position==0){
                num.setTextColor(Color.parseColor("#FFFF9E1E"));
                imageView.setBackgroundDrawable( inflater.getContext().getResources().getDrawable(R.drawable.crown) );

            }

            //     relativeLayout.setBackgroundColor(Color.parseColor("#ffbf00"));
          /*  if(position==1)
                rowView.setBackgroundColor(Color.parseColor("#8a9597"));
            if(position==2)
                rowView.setBackgroundColor(Color.parseColor("#cd7f32"));*/
/*
            if(position==0)
            if(position==1)
                num.setTextColor(Color.parseColor("#8a9597"));
            if(position==2)
                num.setTextColor(Color.parseColor("#cd7f32"));

                */
            return  rowView;
        } catch (JSONException e) {
        }
        return null;
    }}
