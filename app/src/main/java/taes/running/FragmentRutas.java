package taes.running;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.roughike.swipeselector.OnSwipeItemSelectedListener;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jorge on 22/04/2016.
 */
public class FragmentRutas extends Fragment {
    private View view;
    private JSONArray TodoslosEvetos;
    private JSONArray EnlosQueParticipo;

    private ListView rutasListView;
    public static  JSONArray jsonArray=null;
    private LayoutInflater inflater;
    public static FragmentRutas newInstance() {
        FragmentRutas fragmentRutas = new FragmentRutas();
        Bundle args = new Bundle();
        try {
            jsonArray=  new JSONArray(Principal.rutas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragmentRutas;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater =inflater;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.rutas, container, false);
        rutasListView = (ListView) rootView.findViewById(R.id.rutasListView);
        SwipeSelector swipeSelector = (SwipeSelector) rootView.findViewById(R.id.Rutas_Selector);
        swipeSelector.setItems(
                new SwipeItem(0, "Eventos", "Running en la provincia de Alicante."),
                new SwipeItem(1, "Rutas", "Rutas compartidas por los usuarios.")
        );
        rutasListView.setOnItemClickListener(clickEnEvento);
        swipeSelector.selectItemAt(0);
        getEventos();
        swipeSelector.setOnItemSelectedListener(new OnSwipeItemSelectedListener() {
            @Override
            public void onItemSelected(SwipeItem item) {
                if((int)item.value==1){
                    getRutas();
                    rutasListView.setOnItemClickListener(clickEnRuta);
                }else{
                    getEventos();
                    rutasListView.setOnItemClickListener(clickEnEvento);
                }
            }
        });
        return rootView;
    }

    protected AdapterView.OnItemClickListener clickEnRuta = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                SweetAlertDialog sweetalert;

                sweetalert=new SweetAlertDialog(inflater.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Mapa")
                        .setContentText("¿Quieres mostrar la ruta en el mapa?")
                        .setConfirmText("Si!")
                        .showCancelButton(true)
                        .setCancelText("No!");
                sweetalert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {

                        sDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sDialog.setTitleText("Loading").showCancelButton(false);
                        sDialog.setContentText("");
                        sDialog.setCancelable(false);
                        Fuel.get(Principal.servidor + "/routes/" + id).responseString(new Handler<String>() {
                            @Override
                            public void failure(Request request, Response response, FuelError error) {
                                sDialog.setTitleText("Error!")
                                        .setContentText("No se ha podido obtener la ruta!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }

                            @Override
                            public void success(Request request, Response response, String data) {
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    FragmentCorrer.RutaACorrer = new PolylineOptions().color(Color.RED);

                                    JSONArray puntosRuta = jsonObject.getJSONArray("puntosRuta");
                                    for (int i = 0; i < puntosRuta.length(); i++) {
                                        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).bearing(70).tilt(25).target(new LatLng(puntosRuta.getJSONObject(i).getDouble("lati"), puntosRuta.getJSONObject(i).getDouble("longi"))).build();
                                        FragmentCorrer.map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        FragmentCorrer.RutaACorrer.add(new LatLng(puntosRuta.getJSONObject(i).getDouble("lati"), puntosRuta.getJSONObject(i).getDouble("longi")));
                                        FragmentCorrer.map.addPolyline(FragmentCorrer.RutaACorrer);
                                    }
                                    FragmentCorrer.map.clear();
                                    FragmentCorrer.map.addPolyline(FragmentCorrer.RutaACorrer);
                                    Principal.viewPager.setCurrentItem(2);
                                    sDialog.dismiss();
                                } catch (JSONException e) {
                                    sDialog.setTitleText("Error!")
                                            .setContentText("No se ha podido entender la ruta que envia el servidor!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            }
                        });
                    }}).show();
            }
    };

    protected AdapterView.OnItemClickListener clickEnEvento = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
            boolean participa = false;
            for (int i = 0; i < EnlosQueParticipo.length(); i++) {
                try {
                    if (EnlosQueParticipo.getJSONObject(i).getInt("EventId") == id)
                        participa = true;
                } catch (JSONException e) {

                }
            }
            final boolean finalParticipa = participa;
            SweetAlertDialog sweetalert;
            if(participa)
                sweetalert=new SweetAlertDialog(inflater.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Seguro?")
                .setContentText("¿Quieres desapuntarte al evento?")
                .setConfirmText("Si!")
                .showCancelButton(true)
                .setCancelText("No!");
            else
                sweetalert=new SweetAlertDialog(inflater.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Quieres apuntarte al evento?")
                .setContentText("")
                .setConfirmText("Si!")
                .showCancelButton(true)
                .setCancelText("No!");
                sweetalert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog sDialog) {
                            sDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                            sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            sDialog.setTitleText("Loading").showCancelButton(false);
                            sDialog.setContentText("");
                            sDialog.setCancelable(false);
                            if(!finalParticipa)
                                Fuel.post(Principal.servidor + "/events/" + id + "/users/" + Principal.user.getId()).responseString(new Handler<String>() {
                                    @Override
                                    public void failure(Request request, Response response, FuelError error) {
                                        sDialog.setTitleText("Error!")
                                        .setContentText("No se ha podido apuntarte!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    }

                                    @Override
                                    public void success(Request request, Response response, String data) {
                                        sDialog.setTitleText("Correcto!")
                                        .setContentText("Te hemos apuntado!")
                                        .setConfirmText("OK").showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        getEventos();
                                    }
                                });
                            else
                                Fuel.delete(Principal.servidor + "/events/" + id + "/users/" + Principal.user.getId()).responseString(new Handler<String>() {
                                    @Override
                                    public void failure(Request request, Response response, FuelError error) {
                                        sDialog.setTitleText("Error!")
                                        .setContentText("No se ha podido desapuntarte!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    }

                                    @Override
                                    public void success(Request request, Response response, String data) {
                                        sDialog.setTitleText("Correcto!")
                                        .setContentText("Te hemos desapuntado!")
                                        .setConfirmText("OK").showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        getEventos();
                                    }
                                });
                        }}).show();
        }

    };

    private void getRutas(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(inflater.getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Fuel.get(Principal.servidor+"/routes/").responseString(new Handler<String>() {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                pDialog.setTitleText("Error!")
                .setContentText("No se han cargado las rutas!")
                .setConfirmText("OK")
                .showCancelButton(false)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
            @Override
            public void success(Request request,Response response, String data) {
                JSONArray jsonArray= null;
                try {
                    jsonArray = new JSONArray(data);
                    adaptadorListaRutas adaptadorEventos = new adaptadorListaRutas(inflater.getContext(),jsonArray);
                    rutasListView.setAdapter(adaptadorEventos);
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getEventos(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(inflater.getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Fuel.get(Principal.servidor+"/events/").responseString(new Handler<String>() {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                System.out.println("kkkkkkkkkcaca");
                pDialog.setTitleText("Error!")
                .setContentText("No se han cargado eventos!")
                .setConfirmText("OK")
                .showCancelButton(false)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
            @Override
            public void success(Request request,Response response, String data) {
                //JSONArray jsonArray= null;
                try {
                     TodoslosEvetos = new JSONArray(data);

                    Fuel.get(Principal.servidor+"/events/users/"+Principal.user.getId()+"/").responseString(new Handler<String>() {
                        @Override
                        public void failure(Request request, Response response, FuelError error) {
                            System.out.println("kkkkkErroru/users/id/events");
                            adaptadorListaEventos adaptadorEventos = new adaptadorListaEventos(inflater.getContext(),TodoslosEvetos, new JSONArray());
                            rutasListView.setAdapter(adaptadorEventos);
                            pDialog.setTitleText("Error!")
                            .setContentText("No se han cargado tu eventos!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                        @Override
                        public void success(Request request,Response response, String data) {
                            //JSONArray En= null;
                            try {
                                EnlosQueParticipo = new JSONArray(data);
                                adaptadorListaEventos adaptadorEventos = new adaptadorListaEventos(inflater.getContext(),TodoslosEvetos, EnlosQueParticipo);
                                rutasListView.setAdapter(adaptadorEventos);
                                pDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
