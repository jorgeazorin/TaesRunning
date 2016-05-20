package taes.running;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

public class Usuario implements Serializable {
    private  SweetAlertDialog pDialog;
    private Context c;
    private String email;
    public String getEmail(){
        return  email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    private String nombre;
    public String getNombre(){
        return  nombre;
    }
    public void setNombre(String email){
        this.nombre=email;
    }


    private String id;
    public String getId(){
        return  id;
    }
    public void setId(String id){
        this.id=id;
    }

    private int nivel;
    public int getNivel(){
        return  nivel;
    }
    public void setNivel(int email){
        this.nivel=email;
    }


    private int genero;
    public int getGenero(){
        return  nivel;
    }
    public void setGenero(int email){
        this.genero=email;
    }

    private Date nacimiento;

    public Date getNacimiento(){
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento){
        this.nacimiento=nacimiento;
    }

    private int calorias;
    public int getCalorias(){
        return  calorias;
    }
    public void setCalorias(int email){
        this.calorias=email;
    }

    private float distancia;
    public void setDistancia(float distancia){
        this.distancia=distancia;
    }
    private String foto;
    public  void setFoto(String foto){
        this.foto=foto;
    }
    public String getFoto(){
        return foto;
    }

    public boolean enviarAlServidor(final Context c){
        this.c=c;
        pDialog = new SweetAlertDialog(c, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        IniciarSesion();
        return true;
    }

    private void IniciarSesion(){
        final Usuario usuario=this;
        JSONObject json = new JSONObject();
        try {
            json.put("name", nombre);
            json.put("password",id);
            json.put("email",email);
            json.put("level",nivel);
            json.put("city","Alicante");
            json.put("rol","USER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Fuel.post(Principal.servidor+"/users").header(new Pair<>("Content-Type", "application/json")).body(json.toString(), Charset.defaultCharset()).responseString(new Handler<String>() {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                pDialog.setTitleText("Error!").setContentText("Error al hacer post del usuario").setConfirmText("OK").showCancelButton(false).setConfirmClickListener(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
            @Override
            public void success(Request request,Response response, String data) {
                GetRutas();
            }
        });
    }

    private void GetRutas(){
        Fuel.get(Principal.servidor+"/routes/").responseString(new Handler<String>() {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                pDialog.setTitleText("Error!").setContentText("No se han obtenido rutas").setConfirmText("OK").showCancelButton(false).setConfirmClickListener(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                System.out.println("kkk Error Obteniendo Rutas");
                GetUsuario("");
            }

            @Override
            public void success(Request request,Response response, String data) {
                GetUsuario(data);
            }
        });
    }

    private void GetUsuario(final String rutas){
        final Usuario usuario=this;
        Fuel.get(Principal.servidor+"/users/email/" + email).responseString(new Handler<String>()
        {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                pDialog.setTitleText("Error!").setContentText("No se ha obtenido el usaurio del email").setConfirmText("OK").showCancelButton(false).setConfirmClickListener(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
            @Override
            public void success(Request request,Response response, String data) {
                try {
                    JSONObject jsonbject = new JSONObject(data);
                    usuario.setId(jsonbject.getString("id"));
                    System.out.println("kkkk Id Obtenido del usuario" + getId());
                    Intent intent = new Intent(c, Principal.class);
                    intent.putExtra("Usuario", usuario);
                    intent.putExtra("Rutas",rutas);
                    c.startActivity(intent);
                } catch (JSONException e) {
                    pDialog.setTitleText("Error!").setContentText("Error con el json recibido al obtener id usuario").setConfirmText("OK").showCancelButton(false).setConfirmClickListener(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);

                }
                pDialog.dismiss();
            }
        });
    }
}
