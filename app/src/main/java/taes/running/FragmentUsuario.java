package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        //final VerticalViewPager viewPager = (VerticalViewPager) rootView.findViewById(R.id.rsv_large);

        AdaptadorUsuario  adaptadorUsuario= new AdaptadorUsuario(getContext());
        viewPager.setAdapter(adaptadorUsuario);
        return rootView;
    }
}
