package taes.running;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

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



                /////////////////////////
                //Provincias
                lista=(Spinner)layout.findViewById(R.id.usuario_provincia);
                ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(mContext, R.array.valores_array, android.R.layout.simple_spinner_dropdown_item);
                lista.setAdapter(adaptador);
                for(int i=0; i<listaProvincias.size();i++){
                    if(listaProvincias.get(i).equals(Principal.user.getprovincia())){
                        lista.setSelection(i);
                        break;
                    }
                }
                /////////////////////////





                //Nivel
                final SwipeSelector nivel = (SwipeSelector) layout.findViewById(R.id.usuario_nivel);
                nivel.setItems(
                        new SwipeItem(0, "Inútil", "Te cuesta andar 5 pasos."),
                        new SwipeItem(1, "Débil", "Vas lento y te cuesta."),
                        new SwipeItem(2, "Normal", "Corres cuando vas al bus."),
                        new SwipeItem(3, "Veloz", "Sueles hacer running para llenar tus vacios."),
                        new SwipeItem(4, "SuperHombre", "Tienes una obsesión, un problema.")
                );
                if(Principal.user.getNivel().equals("Inútil"))
                     nivel.selectItemAt(0);
                else if(Principal.user.getNivel().equals("Débil"))
                    nivel.selectItemAt(1);
                else if(Principal.user.getNivel().equals("Normal"))
                    nivel.selectItemAt(2);
                else if(Principal.user.getNivel().equals("Veloz"))
                    nivel.selectItemAt(3);
                else if(Principal.user.getNivel().equals("SuperHombre"))
                    nivel.selectItemAt(4);
                else
                    nivel.selectItemAt(0);
                /////////////////////////




                //Sexo
                final SwipeSelector sexo = (SwipeSelector) layout.findViewById(R.id.usuario_sexo);
                sexo.setItems(
                        new SwipeItem(0, "Hombre", "Selecciona esta opción si eres hombre."),
                        new SwipeItem(1, "Mujer", "Esta opción si eres mujer.")
                );

                if(Principal.user.getGenero().equals("Mujer"))
                   sexo.selectItemAt(1);
                else
                    sexo.selectItemAt(0);

                /////////////////////////




                //GuardarCambios
                Button guardarCambios=(Button) layout.findViewById(R.id.usuario_guardarcambios);
                final SweetAlertDialog pDialog= new SweetAlertDialog( mContext, SweetAlertDialog.PROGRESS_TYPE);
                guardarCambios.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Loading");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        JSONObject json = new JSONObject();
                        try {
                            json.put("name", Principal.user.getNombre());
                            json.put("password",Principal.user.getId());
                            json.put("email",Principal.user.getEmail());
                            json.put("level",nivel.getSelectedItem().title);
                            json.put("city",lista.getSelectedItem());
                            json.put("rol",sexo.getSelectedItem().title);
                        } catch (JSONException e) {
                            
                        }
                        Fuel.put(Principal.servidor+"/users/"+Principal.user.getId()).header(new Pair<>("Content-Type", "application/json")).body(json.toString(), Charset.defaultCharset()).responseString(new Handler<String>() {
                            @Override
                            public void failure(Request request, Response response, FuelError error) {
                                pDialog.setTitleText("Error!").setContentText("No se ha guardar el usuario").setConfirmText("OK").showCancelButton(false).setConfirmClickListener(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }

                            @Override
                            public void success(Request request,Response response, String data) {
                                pDialog.setTitleText("Guardado!").setContentText("Se ha guardar el usuario").setConfirmText("OK").showCancelButton(false).setConfirmClickListener(null).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        });
                    }
                });
                ///////////////////////
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
