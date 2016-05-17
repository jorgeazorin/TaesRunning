package taes.running;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dd.morphingbutton.MorphingButton;
import com.github.akashandroid90.googlesupport.location.AppLocationFragment;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import kotlin.Pair;

public class FragmentCorrer extends Fragment  {
    private SupportMapFragment fragment;
    public static GoogleMap map;
    public static View v;
    private boolean corriendo=false;
    private float distancia=0;
    private Location ultimaLocation=null;
    private int calorias;
    private boolean moverMapa=false;
    private PolylineOptions p;
    private int wMorphingButton=0;
    private int hMorphingButton=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v =inflater.inflate(R.layout.correr, container, false);
        return v;
    }
    public Timer timer;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        final MorphingButton btnMorph = (MorphingButton) v.findViewById(R.id.correr_boton_empezar);


        btnMorph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!corriendo) {
                    EmpezarACorrer();
                    corriendo = true;
                    wMorphingButton=btnMorph.getWidth();
                    hMorphingButton=btnMorph.getHeight();

                    MorphingButton.Params circle = MorphingButton.Params.create().text("Parar")
                            .duration(300)
                            .cornerRadius(150)
                            .width(150)
                            .height(150)
                            .color(getResources().getColor(R.color.boton_correr_otro));
                    btnMorph.morph(circle);
                }else{
                    PararDeCorrer();
                    corriendo = false;
                    MorphingButton.Params circle = MorphingButton.Params.create().text("Empezar")
                            .duration(300)
                            .cornerRadius(0)
                            .width(wMorphingButton)
                            .height(hMorphingButton)
                           .color(getResources().getColor(R.color.boton_correr_otro1));
                    btnMorph.morph(circle);
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    moverMapa=!moverMapa;
                }
            });
        }
    }






    public void EmpezarACorrer(){
        map.clear();
        p=new PolylineOptions().color(this.getResources().getColor(R.color.colorPrimary));
        map.addPolyline(p);

       // final PolylineOptions p = new PolylineOptions().color(this.getResources().getColor(R.color.colorPrimary));
        SmartLocation.with(v.getContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                map.addPolyline(p);
                if(ultimaLocation!=null)
                distancia+=location.distanceTo(ultimaLocation);
                TextView txtDistancia = (TextView) v.findViewById(R.id.correr_distancia);
                TextView txtCalorias = (TextView) v.findViewById(R.id.correr_calorias);
                DecimalFormat dff = new DecimalFormat();
                dff.setMaximumFractionDigits(0);
                if(!moverMapa) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).bearing(70).tilt(25).target(new LatLng(location.getLatitude(), location.getLongitude())).build();
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                float calori=375*distancia/5000;
                txtCalorias.setText(dff.format(calori));
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                txtDistancia.setText(df.format(distancia/1000));
                ultimaLocation=location;
                if (FragmentCorrer.map != null) {
                    p.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    } else
                        FragmentCorrer.map.setMyLocationEnabled(true);
                }
            }
        });


         timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        TextView txtClicks = (TextView) v.findViewById(R.id.correr_contador);
                        TextView txtVelocidadMedia = (TextView) v.findViewById(R.id.correr_velocidadmedia);
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);
                        txtVelocidadMedia.setText(df.format(distancia*3.6/Principal.cronometro));
                        Principal.cronometro =Principal.cronometro+  1;
                        txtClicks.setText(String.valueOf(toTiempo(Principal.cronometro)));
                    }
                });

            }
        }, 0, 1000);




    }

    public String toTiempo(int i){
        String s="";
        int horas=i/3600;
        String shoras=String.valueOf(horas);
        if (horas<10)
            shoras="0"+shoras;
        int minutos=(i-(horas*3600))/60;
        String sm=String.valueOf(minutos);
        if (minutos<10)
            sm="0"+sm;


        int segundos=(i-(horas*3600)-(minutos*60));
        String ss=String.valueOf(segundos);
        if (segundos<10)
            ss="0"+ss;
        s=shoras+":"+sm+":"+ss;

        return s;
    }
    public void PararDeCorrer(){
        SmartLocation.with(v.getContext()).location().config(LocationParams.NAVIGATION).stop();
        timer.cancel();
        new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Quieres guardar la ruta?")
                .setContentText("La ruta será compartida con todos!")
                .setConfirmText("Si, guardarla!")
                .showCancelButton(true)
                .setCancelText("No, no guardar!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                       // SweetAlertDialog.PROGRESS_TYPE);
                        sDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sDialog.setTitleText("Loading").showCancelButton(false);
                        sDialog.setContentText("");

                        sDialog.setCancelable(false);
                        final JSONObject json = new JSONObject();
                        try {

                            JSONObject jsonRuta = new JSONObject();
                            jsonRuta.put("nombre",Principal.user.getNombre());
                            jsonRuta.put("distancia",distancia);
                            JSONObject jsonDatosCorridos = new JSONObject();
                            jsonDatosCorridos.put("velocidad",distancia*3.6/Principal.cronometro);
                            Principal.cronometro=0;
                            jsonDatosCorridos.put("km",distancia/1000);
                            jsonDatosCorridos.put("calorias",calorias);
                            jsonDatosCorridos.put("fecha","12/12/2015");
                            json.put("ruta", jsonRuta);
                            json.put("datosCorridos", jsonDatosCorridos);
                            JSONArray Puntos = new JSONArray();
                            for(int i=0;i<p.getPoints().size();i++){
                                JSONObject punto = new JSONObject();
                                punto.put("lati",p.getPoints().get(i).latitude);
                                punto.put("longi",p.getPoints().get(i).longitude);
                                Puntos.put(punto);
                            }
                            json.put("puntosRuta",Puntos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("kkkkkkkkkkkkkkkk "+json.toString());

                        Fuel.post("http://13.95.145.255/routes/").header(new Pair<>("Content-Type", "application/json")).body(json.toString(), Charset.defaultCharset()).responseString(new Handler<String>() {
                            @Override
                            public void failure(Request request, Response response, FuelError error) {
                                sDialog.setTitleText("Error!")
                                        .setContentText("Tu ruta no ha sido guardada!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }

                            @Override
                            public void success(Request request,Response response, String data) {
                                sDialog.setTitleText("Guardada!")
                                        .setContentText("Tu ruta ha sido guardada!")
                                        .setConfirmText("OK").showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                                Fuel.get("http://13.95.145.255/routes/").responseString(new Handler<String>() {
                                    @Override
                                    public void failure(Request request, Response response, FuelError error) {
                                        System.out.println("nokkkkkkkkkk");

                                    }

                                    @Override
                                    public void success(Request request,Response response, String data) {
                                        System.out.println("okkkkkkkkkk");
                                        Principal.rutas=data;
                                        try {
                                            FragmentRutas.jsonArray=new JSONArray(data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }
                })
                .show();

    }


}