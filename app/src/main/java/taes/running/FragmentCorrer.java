package taes.running;

import android.Manifest;
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

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class FragmentCorrer extends Fragment  {
    private SupportMapFragment fragment;
    public static GoogleMap map;
    public static View v;
    private boolean corriendo=false;
    private float distancia=0;
    private Location ultimaLocation=null;
    private int calorias;
    private boolean moverMapa=false;
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
                            .width(-2)
                            .height(-2)
                            .color(getResources().getColor(R.color.boton_correr_otro));
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
        final PolylineOptions p = new PolylineOptions().color(this.getResources().getColor(R.color.colorPrimary));
        SmartLocation.with(v.getContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
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
                    FragmentCorrer.map.addPolyline(p);
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
        Principal.cronometro=0;
    }


}