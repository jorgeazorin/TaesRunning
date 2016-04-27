package taes.running;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jorge on 22/04/2016.
 */
public class FragmentRanking extends Fragment {

    public static FragmentRanking newInstance() {
        FragmentRanking fragmentRutas = new FragmentRanking();
        Bundle args = new Bundle();
        return fragmentRutas;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.vp_item, container, false);
        new SweetAlertDialog(inflater.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
        return rootView;
    }
}
