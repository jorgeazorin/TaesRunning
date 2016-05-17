package taes.running;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.channguyen.rsv.RangeSliderView;

/**
 * Created by jorge on 27/04/2016.
 */
public class AdaptadorUsuario extends PagerAdapter {
    private Context mContext;

    public AdaptadorUsuario(Context context) {
        mContext = context;
    }

    private RangeSliderView smallSlider;

    private RangeSliderView largeSlider;


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = null;
        switch (position) {
            case 0:
                layout=  (ViewGroup) inflater.inflate(R.layout.usuario_perfil, collection, false);
                ImageView imagenPerfil=(ImageView) layout.findViewById(R.id.usuario_imagen_Perfil);
                TextView nombrePerfil=(TextView) layout.findViewById(R.id.usuario_Nombre);
                nombrePerfil.setText(Principal.user.getNombre());
                Glide.with(mContext)
                        .load(Principal.user.getFoto())
                        .placeholder(R.drawable.staticmap)
                        .into(imagenPerfil);
                break;
            case 1:
                layout=  (ViewGroup) inflater.inflate(R.layout.ruta_recorrida, collection, false);
                //smallSlider = (RangeSliderView) layout.findViewById(R.id.rsv_large);
                largeSlider = (RangeSliderView) layout.findViewById(R.id.rsv_large);
                final RangeSliderView.OnSlideListener listener = new RangeSliderView.OnSlideListener() {
                    @Override
                    public void onSlide(int index) {
                        Toast.makeText(
                                inflater.getContext(),"Hi index: " + index,Toast.LENGTH_SHORT).show();
                    }
                };
                //smallSlider.setOnSlideListener(listener);
                largeSlider.setOnSlideListener(listener);

                //-------------------------------




                break;
            case 2:
                layout=  (ViewGroup) inflater.inflate(R.layout.usuario_perfil, collection, false);
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
        return 3;
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
