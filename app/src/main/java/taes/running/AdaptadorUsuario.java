package taes.running;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.channguyen.rsv.RangeSliderView;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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

    //-----------------------------------------------------------------
    Spinner lista;
    String nivel="";
    String provincia="";
    int posProvincia;
    ArrayList<String> listaProvincias = new ArrayList<String>();
    String[] aux={"a coruna","alava","albacete","alicante","almeria","asturias","avila","badajoz","baleares","barcelona","burgos","caceres","cadiz","cantabria","castellon","ceuta","ciudad real","cordoba","cuenca","girona","granada","guadalajara",
            "guipuzcoa","huelva","huesca","jaen","la rioja","las palmas","leon","lleida","lugo","madrid","malaga","melilla","murcia","navarra","ourense","palencia","pontevedra","salamanca","tenerife","segovia","sevilla","soria","tarragona","teruel",
            "toledo","valencia","valladolid","vizcaya","zamora","zaragoza"};
//--------------------------------------------------------------------

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = null;

        //---------------------------------------------
        listaProvincias.addAll(Arrays.asList(aux));

        //consulto los datos del usuario a partir de su id
        Fuel.get(Principal.servidor+"/users/" + Principal.user.getId()).responseString(new Handler<String>()
        {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                System.out.println("nokkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                System.out.println("kkkk Usuario Principal "+Principal.user.getId());
            }

            @Override
            public void success(Request request,Response response, String data) {
                System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                try {
                    JSONObject jsonobject = new JSONObject(data);
                    //cojo el nivel del usuario y lo guardo en la variable nivel
                    nivel=jsonobject.getString("level");
                    //cojo la provincia y la guardo en la variable provincia
                    provincia=jsonobject.getString("city");
                    //compruebo en que posición está y la almaceno en posProvincia
                    posProvincia=listaProvincias.indexOf(provincia);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        switch (position) {
            case 0:
                layout=  (ViewGroup) inflater.inflate(R.layout.usuario_perfil, collection, false);
                ImageView imagenPerfil=(ImageView) layout.findViewById(R.id.usuario_imagen_Perfil);
                TextView nombrePerfil=(TextView) layout.findViewById(R.id.usuario_Nombre);
                TextView campoNivel=(TextView) layout.findViewById(R.id.textView3);
                nombrePerfil.setText(Principal.user.getNombre());
                Glide.with(mContext)
                        .load(Principal.user.getFoto())
                        .placeholder(R.drawable.staticmap)
                        .into(imagenPerfil);

                campoNivel.setText("nivel: " + nivel);
                break;

            case 1:
                layout=  (ViewGroup) inflater.inflate(R.layout.ruta_recorrida, collection, false);
                //smallSlider = (RangeSliderView) layout.findViewById(R.id.rsv_large);
                final RangeSliderView.OnSlideListener listener = new RangeSliderView.OnSlideListener() {
                    @Override
                    public void onSlide(int index) {
                        Toast.makeText(
                                inflater.getContext(),"Hi index: " + index,Toast.LENGTH_SHORT).show();
                    }
                };
                //smallSlider.setOnSlideListener(listener);
                String result1;
                final SwipeNumberPicker swipeNumberPicker = (SwipeNumberPicker) layout.findViewById(R.id.nivel_picker);
                swipeNumberPicker.setOnValueChangeListener(new OnValueChangeListener() {
                    @Override
                    public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                        boolean isValueOk = (newValue & 1) == 0;
                        if (isValueOk)
                            swipeNumberPicker.setValue(newValue,false);
                        return true;
                    }
                });
                //-------------------------------
                //comienza Spinner

                lista=(Spinner)layout.findViewById(R.id.spinner);

                ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(mContext, R.array.valores_array, android.R.layout.simple_spinner_dropdown_item);
                lista.setAdapter(adaptador);
                lista.setSelection(posProvincia);

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
