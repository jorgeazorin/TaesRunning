package taes.running;

import android.net.Uri;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

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
}
