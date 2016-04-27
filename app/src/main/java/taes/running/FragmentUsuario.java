package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.otro, container, false);
        SwipeSelector swipeSelector = (SwipeSelector) rootView.findViewById(R.id.swipeSelector);
        swipeSelector.setItems(
                new SwipeItem(0, Principal.user.getEmail(), "Description for slide one."),
                new SwipeItem(1, "Slide two", "Description for slide two."),
                new SwipeItem(2, "Slide three", "Description for slide three.")
        );
        return rootView;
    }
}
