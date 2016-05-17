package taes.running;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

public class Usuario implements Serializable {
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

        final SweetAlertDialog pDialog = new SweetAlertDialog(c, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Fuel.post("http://13.95.145.255/users").header(new Pair<>("Content-Type", "application/json")).body(json.toString(), Charset.defaultCharset()).responseString(new Handler<String>() {
            @Override
            public void failure(Request request, Response response, FuelError error) {
                System.out.println("nokkkkkkkkkk");
                pDialog.setTitleText("Error!")
                        .setContentText("No se ha podedio conectar con el servidor")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                Fuel.get("http://13.95.145.255/routes/").responseString(new Handler<String>() {
                    @Override
                    public void failure(Request request, Response response, FuelError error) {
                        System.out.println("nokkkkkkkkkk");
                        pDialog.setTitleText("Error!")
                                .setContentText("No se han obtenido rutas")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        //do something when it is failure
                    }

                    @Override
                    public void success(Request request,Response response, String data) {
                        System.out.println("okkkkkkkkkk");
                        JSONArray jsonArray=new JSONArray();
                        Intent intent = new Intent(c, Principal.class);
                        intent.putExtra("Usuario", usuario);
                        intent.putExtra("Rutas",data);
                        c.startActivity(intent);
                        pDialog.dismiss();
                    }
                });
                pDialog.dismiss();
                //do something when it is failure
            }

            @Override
            public void success(Request request,Response response, String data) {


                Fuel.get("http://13.95.145.255/routes/").responseString(new Handler<String>() {
                    @Override
                    public void failure(Request request, Response response, FuelError error) {
                        System.out.println("nokkkkkkkkkk");
                        pDialog.setTitleText("Error!")
                                .setContentText("No se han obtenido rutas")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void success(Request request,Response response, String data) {
                        System.out.println("okkkkkkkkkk");
                        JSONArray jsonArray=new JSONArray();
                        Intent intent = new Intent(c, Principal.class);
                        intent.putExtra("Usuario", usuario);
                        intent.putExtra("Rutas",data);
                        c.startActivity(intent);
                        pDialog.dismiss();
                    }
                });
                System.out.println("okkkkkkkkkk");
                System.out.println(data);

            }
        });
        return true;
    }
}
