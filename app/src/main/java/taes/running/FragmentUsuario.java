package taes.running;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.library.NavigationTabBar;

import java.util.ArrayList;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;


public class FragmentUsuario extends Fragment {
    public static FragmentUsuario newInstance() {
        FragmentUsuario fragmentRutas = new FragmentUsuario();
        Bundle args = new Bundle();
        return fragmentRutas;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.usuario, container, false);
        final VerticalViewPager viewPager = (VerticalViewPager) rootView.findViewById(R.id.UsuarioVVP);
        AdaptadorUsuario  adaptadorUsuario= new AdaptadorUsuario(getContext());
        viewPager.setAdapter(adaptadorUsuario);
        return rootView;
    }
}
