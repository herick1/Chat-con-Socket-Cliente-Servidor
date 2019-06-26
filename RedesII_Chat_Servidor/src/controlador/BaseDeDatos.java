/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.ArrayList;

/**
 *
 * @author herick
 */
public class BaseDeDatos {
    
    ArrayList<String> usuariosServidor; 
    
    public BaseDeDatos(){
        usuariosServidor = new ArrayList(); ;
        usuariosServidor.add("herick");
        usuariosServidor.add("diorfelis");
        usuariosServidor.add("luis");
        usuariosServidor.add("jorge"); 
        usuariosServidor.add("Server"); 
    }
    
    
    public  ArrayList<String>  ListaDeAmigosUsuario(String Usuario){
         
         ArrayList<String>  listaAuciiar = new  ArrayList<String>();
         for (int i=0 ; i< usuariosServidor.size(); i++ ){
             if(!usuariosServidor.get(i).equals(Usuario)){
                listaAuciiar.add(usuariosServidor.get(i));   
             }
         }
         return listaAuciiar;
    }
    
    public boolean AgregarUsuarioServidor(String Usuario, String contra){
        usuariosServidor.add(Usuario);
        return true;
    }
    public boolean EliminarUsuarioServidor(String Usuario){
        for (int i=0 ; i< usuariosServidor.size(); i++ ){
             if(usuariosServidor.get(i).equals(Usuario)){        
                 usuariosServidor.remove(i); 
                 return true;
             }
         }        
        return false;
    }
    public boolean AgregarUsuarioListaCliente(String Usuario, String NuevoNombre){
     return true;   
    }
    public boolean EliminarUsuarioListaCliente(String Usuario, String NuevoNombre){
     return true;    
    }
    public boolean ConsultarClienteLogin(String Usuario, String contraseña){
         for (int i=0 ; i< usuariosServidor.size(); i++ ){
             if( usuariosServidor.get(i).equals(Usuario)){
                 if("hola123".equals(contraseña)){
                     return true;
                }
             }
         }        
        return false;
    }
}
