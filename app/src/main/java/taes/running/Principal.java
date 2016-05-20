package taes.running;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.load.engine.Resource;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gigamole.library.NavigationTabBar;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class Principal extends FragmentActivity {
    public static FragmentManager fragmentManager;
    public static Usuario user;
    public static String rutas;
    public  static String servidor="http://192.168.1.36:3000";
    private int NumNotificaciones=-1;
    Activity contexto;
    public static int cronometro;
   public static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        contexto = this;
        user = (Usuario) getIntent().getSerializableExtra("Usuario");
        rutas =  getIntent().getStringExtra("Rutas");
        initUI();
        SmartLocation.with(this).location().config(LocationParams.NAVIGATION).stop();

        final ImageView notificaciones=(ImageView) findViewById(R.id.notificacion_botonimagen);
        notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificaciones.setBackgroundColor(Color.parseColor("#ffffff"));

                Intent intent = new Intent(contexto, NotificacionesActivity.class);
                intent.putExtra("id",""+Principal.user.getId());
                contexto.startActivity(intent);
            }
        });
        BuscarNotificaciones();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                BuscarNotificaciones();

            }
        }, 0, 10000);

    }

private void BuscarNotificaciones(){
    final ImageView notificaciones=(ImageView) findViewById(R.id.notificacion_botonimagen);
    Fuel.get(Principal.servidor+"/notifications/").responseString(new Handler<String>() {
        @Override
        public void failure(Request request, Response response, FuelError error) {
            System.out.println("kkk notificaciones error");
        }
        @Override
        public void success(Request request,Response response, String data) {
            try {
                JSONArray jsonArray=new JSONArray(data);
                int aNumNotificaciones= jsonArray.length();
                System.out.println("kkk "+NumNotificaciones+"-"+aNumNotificaciones);
                if(NumNotificaciones>0){
                    if(NumNotificaciones<aNumNotificaciones){
                        notificaciones.setBackgroundColor(Color.parseColor("#f3ff48"));
                    }
                } else
                    NumNotificaciones=aNumNotificaciones;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
    //return -1;
}


    private void initUI() {
       viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        principalPageadapter principalPageadapte = new principalPageadapter(getSupportFragmentManager());
        fragmentManager=getSupportFragmentManager();
        viewPager.setAdapter(principalPageadapte);
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_third), Color.parseColor(colors[0]), "Usuario"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_second), Color.parseColor(colors[1]), "Premios"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_sixth), Color.parseColor(colors[2]), "Correr"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_fourth), Color.parseColor(colors[3]), "Rutas"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_seventh), Color.parseColor(colors[4]), "Ranking"));
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager,2);
        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View bgNavigationTabBar = findViewById(R.id.bg_ntb_horizontal);
                bgNavigationTabBar.getLayoutParams().height = (int) navigationTabBar.getBarHeight();
                bgNavigationTabBar.requestLayout();
            }
        });

    }

    public static class principalPageadapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 5;
        public static FragmentCorrer fragmentCorrer;
        public principalPageadapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return FragmentUsuario.newInstance();
                case 1: return FragmentPremios.newInstance();
                case 2: return new FragmentCorrer();
                case 3: return FragmentRutas.newInstance();
                case 4: return FragmentRanking.newInstance();
                default: return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }
}