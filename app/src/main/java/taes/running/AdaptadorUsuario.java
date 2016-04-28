package taes.running;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.model.ChartSet;
import com.db.chart.view.StackBarChartView;
import com.db.chart.view.animation.Animation;

/**
 * Created by jorge on 27/04/2016.
 */
public class AdaptadorUsuario extends PagerAdapter {
    private Context mContext;

    public AdaptadorUsuario(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = null;
        switch (position) {
            case 0:
                layout=  (ViewGroup) inflater.inflate(R.layout.usuario_perfil, collection, false);
                break;
            case 1:
                layout=  (ViewGroup) inflater.inflate(R.layout.ruta_recorrida, collection, false);
                StackBarChartView barras= (StackBarChartView) layout.findViewById(R.id.Usuariolinechart);
                BarSet data= new BarSet();
               // float[] values={ 5 , 3 , 2 };
                data.addBar("hola",10);
                data.addBar("hola",4);
                data.addBar("hola",3);
                data.addBar("hola",2);
                data.addBar("hola",10);
                data.addBar("hola",4);
                data.addBar("hola",3);
                data.addBar("hola",2);
                data.addBar("hola",10);
                data.addBar("hola",4);
                data.addBar("hola",3);
                data.addBar("hola",2);
                barras.addData(data);
                Animation anim = new Animation(20);
                barras.show(anim);
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
