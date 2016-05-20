package taes.running;

import android.Manifest;
import android.app.Activity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.load.engine.Resource;
import com.gigamole.library.NavigationTabBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class Principal extends FragmentActivity {
    public static FragmentManager fragmentManager;
    public static Usuario user;
    public static String rutas;
    public  static String servidor="http://192.168.1.41:3000";
    //public  static String servidor="http://13.95.145.255";
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
        navigationTabBar.setViewPager(viewPager,0);
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
