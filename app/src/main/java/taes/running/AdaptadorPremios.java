package taes.running;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jorge on 27/04/2016.
 */
public class AdaptadorPremios extends PagerAdapter {
    private Context mContext;

    public AdaptadorPremios(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.premio, collection, false);
        ImageView imagen =(ImageView) layout.findViewById(R.id.PremioImagen);
        TextView texto =(TextView) layout.findViewById(R.id.premioTextView);
        switch (position) {
            case 0:
                imagen.setImageResource(R.drawable.animals14);
                imagen.setBackgroundColor(Color.argb(255,135, 63, 156));
                texto.setText("Tortuga");
                break;

            case 1:
                imagen.setImageResource(R.drawable.animals3);
                imagen.setBackgroundColor(Color.argb(255,63, 135, 156));
                texto.setText("Perezoso");
                break;
            case 2:
                imagen.setImageResource(R.drawable.animals24);
                imagen.setBackgroundColor(Color.argb(255,63, 156, 135));
                texto.setText("Ñu");
                break;
            case 3:
                imagen.setImageResource(R.drawable.animals5);
                imagen.setBackgroundColor(Color.argb(255, 135, 156, 63));
                texto.setText("Ganador de batalla");
                break;
            case 4:
                imagen.setImageResource(R.drawable.animals25);
                imagen.setBackgroundColor(Color.argb(255, 156, 135, 63));

                texto.setText("Mejor maratón");
                break;
            case 5:
                imagen.setImageResource(R.drawable.animals5);
                imagen.setBackgroundColor(Color.argb(255, 255, 255, 255));
                texto.setText("Perezoso");
                break;
            default:
                break;

        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
       return "na";
    }
}
