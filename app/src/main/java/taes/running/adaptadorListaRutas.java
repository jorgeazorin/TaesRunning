package taes.running;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.zip.Inflater;

/**
 * Created by jorge on 22/04/2016.
 */
public class adaptadorListaRutas  extends BaseAdapter {
    private Context context;
    public  adaptadorListaRutas(Context context){
        super();
        this.context=context;

    }
    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView myImageView;

        LayoutInflater inflater =  LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.ruta, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.Ruta_imagen);
        Glide
                .with(context)

                .load("http://maps.googleapis.com/maps/api/staticmap?center=Spain&size=100x100&key=AIzaSyBMa3yVWGGSr1fcwI8LlVpk_7GTni2JJTE")
        .placeholder(R.drawable.staticmap)
                .into(imageView);


        return rowView;

    }
}

