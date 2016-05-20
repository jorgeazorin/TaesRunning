package taes.running;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NotificacionesActivity extends AppCompatActivity {
    private JSONArray jsonArray;
    //intent.putExtra("id","id");
private Context context;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notificaciones");
        setContentView(R.layout.activity_notificaciones);
        id =  getIntent().getStringExtra("id");
        context=this;
        final ListView lista= (ListView)  findViewById(R.id.notificaciones_listview);
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Fuel.get(Principal.servidor+"/notifications/").responseString(new Handler<String>() {
            @Override
            public void failure(Request request, Response response, FuelError error) {

                pDialog.setTitleText("Error!")
                        .setContentText("No se han cargado las notificaciones!")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
            @Override
            public void success(Request request,Response response, String data) {
                try {
                    jsonArray=new JSONArray(data);
                    adaptadorNotificaciones adaptador=new adaptadorNotificaciones(context,jsonArray);
                    lista.setAdapter(adaptador);
                    pDialog.dismiss();
                } catch (JSONException e) {

                    pDialog.setTitleText("Error!")
                            .setContentText("JSON Error!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }

            }
        });

    }
}
