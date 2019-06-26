/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import paneles.*;
import principal.*;

/**
 *
 * @author herick
 */
public class Controlador {
    public pnlCollection pnlCollection;
    public ArrayList<Cliente_chat> Cliente_chat;
    public pnlChat pnlChat;
    public JPanel panelPrincipal;
    public Principal principal;
    public String  UsuarioDeChatSeleccionado ;
    public int UsuarioDeChatSeleccionadoIndex;
    public Login login;
    public ArrayList<String> arrayDeListaUsuario;
    public ArrayList<Boolean> arrayDeListaUsuarioConectado;
    public ArrayList<Integer>  arrayMensajeSinLeer;   
    
    public Controlador() throws UnknownHostException{
      this.address = InetAddress.getLocalHost().getHostAddress();
      login = new Login(this);
      pnlCollection = new pnlCollection(); 
      Cliente_chat = new ArrayList();
      pnlChat = new pnlChat(this);
      principal = new Principal(this);
      arrayMensajeSinLeer = new ArrayList();
      arrayDeListaUsuarioConectado = new ArrayList();
    }          
    
    
    public String username;
    String address;
    ArrayList<String> users = new ArrayList();
    int port = 10101;
    public Boolean isConnected = false;
    
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;
    
    
    public  void Login(String Contra){
        try
        {
            Contra = ENCRYPT_KEY.encrypt(Contra);            
            System.out.println(Contra);
            writer.println(username + ":"+Contra + ":Login");
            writer.flush(); // flushes the buffer
        }catch (Exception ex) {
            int resp = JOptionPane.showConfirmDialog(null, "No se pudieron ogiar", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }   
    
    public  void ListarUsuarios(){
        try
        {
            writer.println(username + ":Listando usuarios del cliente:" + "Listar");
            writer.flush(); // flushes the buffer
        }catch (Exception ex) {
            int resp = JOptionPane.showConfirmDialog(null, "No se pudieron listar a tus amigos", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
       }
    }

    public  void LlenarListar(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        arrayDeListaUsuario = gson.fromJson(json, listType);
        DefaultListModel modelo= new DefaultListModel();
        pnlChat.jList1.setModel(modelo);
        for (int i =0 ; i< arrayDeListaUsuario.size(); i++){
            arrayMensajeSinLeer.add(0);
            arrayDeListaUsuarioConectado.add(false);
            modelo.addElement(arrayDeListaUsuario.get(i));  
            Cliente_chat.add(new Cliente_chat(this));
        }
        chatServidor(username+ ": " + json + "\n");
    }
    

    public  void LlenarListarMensajeSinLeer(){
        DefaultListModel modelo= new DefaultListModel();
        pnlChat.jList1.setModel(modelo);
        for (int i =0 ; i< arrayDeListaUsuario.size(); i++){
            if(arrayDeListaUsuarioConectado.get(i)){
                if(arrayMensajeSinLeer.get(i) == 0){
                    modelo.addElement(arrayDeListaUsuario.get(i)+"                 "+" EnLinea");
                }else{
                    modelo.addElement(arrayDeListaUsuario.get(i)+"   "+arrayMensajeSinLeer.get(i) +" mensaje sin leer"+" EnLinea" );
                }

            }else{
                if(arrayMensajeSinLeer.get(i) == 0){
                    modelo.addElement(arrayDeListaUsuario.get(i));
                }else{
                    modelo.addElement(arrayDeListaUsuario.get(i)+"   "+arrayMensajeSinLeer.get(i) +" mensaje sin leer");
                }
            }
        }
 }
    //--------------------------//
    
    
    public void conectar(String nombre, String pass){
        if (isConnected == false)
        {
            username= nombre;
            try
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(username + ":has connected.:Connect");
                writer.flush();
                isConnected = true;
            }
            catch (Exception e)
            {
            int resp = JOptionPane.showConfirmDialog(null, "Cannot Connect! , Revise Servidor", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            ListenThread();
        } /*else if (isConnected == true)
        {
            chatServidor("You are already connected. \n");
        }
        return true;*/
    }
    
    
    public void PanelPrincipal(JPanel panelPrincipa){
        panelPrincipal = panelPrincipa;
    }
    
    
    
    public void EnviarMensaje(String mensaje){
        String nothing = "";
        if (mensaje.equals(nothing)) {
            Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).tf_chat.setText("");
            Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).requestFocus();
        } else {
            try {
                writer.println(username+"-"+UsuarioDeChatSeleccionado + ":" + mensaje + ":" + "chatear");
                writer.flush(); // flushes the buffer
                Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).ta_chat.append("                                                                                                                                                                       "
                        +username+":"+mensaje+ "\n" );
            } catch (Exception ex) {
                Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).ta_chat.append("Message was not sent. \n");
            }
             Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).tf_chat.setText("");
             Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).tf_chat.requestFocus();
        }
        Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).tf_chat.setText("");
        Cliente_chat.get(UsuarioDeChatSeleccionadoIndex).tf_chat.requestFocus();
    }
    
    public void LeerMensaje(String[] data, String mensaje){
        if(data[1].equals(username)){
        for (int i =0 ; i< arrayDeListaUsuario.size(); i++){
            if(arrayDeListaUsuario.get(i).equals(data[0])){
                arrayMensajeSinLeer.set(i, arrayMensajeSinLeer.get(i)+1);
                LlenarListarMensajeSinLeer();
                Cliente_chat.get(i).ta_chat.append(data[0] + ": " + mensaje + "\n");
                Cliente_chat.get(i).ta_chat.setCaretPosition(
                    Cliente_chat.get(i).ta_chat.getDocument().getLength()
                );
                
            } 
        }
        }
    }
    
    public  void ListarConectados(){
        try
        {
            writer.println(username + ":Listando usuarios conectados" + ":ListarIsConectado");
            writer.flush(); // flushes the buffer
        }catch (Exception ex) {
            int resp = JOptionPane.showConfirmDialog(null, "No se pudieron listar a tus amigos", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
       }
    }
    public void LeerConectados(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<String[]>>(){}.getType();
        ArrayList<String[]> arrayALeer = gson.fromJson(json, listType);
        DefaultListModel modelo= new DefaultListModel();
        pnlChat.jList1.setModel(modelo);
        for (int i =0 ; i< arrayDeListaUsuario.size(); i++){
            for (int j =0 ; j< arrayALeer.size(); j++){
                if(arrayDeListaUsuario.get(i).equals(arrayALeer.get(j)[0])){
                    if("true".equals(arrayALeer.get(j)[1])){
                        arrayDeListaUsuarioConectado.set(i,true);
                        if(arrayMensajeSinLeer.get(i) == 0){
                            modelo.addElement(arrayDeListaUsuario.get(i)+"                 "+" EnLinea");
                        }else{
                            modelo.addElement(arrayDeListaUsuario.get(i)+"   "+arrayMensajeSinLeer.get(i) +" mensaje sin leer"+" EnLinea" );
                        }
                         
                    }else{
                        arrayDeListaUsuarioConectado.set(i,false);
                        if(arrayMensajeSinLeer.get(i) == 0){
                            modelo.addElement(arrayDeListaUsuario.get(i));
                        }else{
                            modelo.addElement(arrayDeListaUsuario.get(i)+"   "+arrayMensajeSinLeer.get(i) +" mensaje sin leer");
                        }
                    }       
                }
            }
        }
    }
    
    public void chatServidor(String mensaje){
        for (int i =0 ; i< arrayDeListaUsuario.size(); i++){
            if(arrayDeListaUsuario.get(i).equals("Server")){
               Cliente_chat.get(i).ta_chat.append(mensaje+ "\n");
            }
        }
    }
    
    
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
    //--------------------------//
    
    public void userAdd(String data) 
    {
         users.add(data);
    }
    
    //--------------------------//
    
    public void userRemove(String data) 
    {
         chatServidor(data + " is now offline.\n");
    }
    
    //--------------------------//
    
    public void writeUsers() 
    {
         String[] tempList = new String[(users.size())];
         users.toArray(tempList);
         for (String token:tempList) 
         {
             //users.append(token + "\n");
         }
    }
    
    //--------------------------//
    
    public void sendDisconnect() 
    {
        String bye = (username + ": :Disconnect");
        try
        {
            writer.println(bye); 
            writer.flush(); 
        } catch (Exception e) 
        {
            chatServidor("Could not send Disconnect message.\n");
        }
    }

    //--------------------------//
    
    public void Disconnect() 
    {
        try 
        {
            chatServidor("Disconnected.\n");
            sendDisconnect();
            sock.close();
        } catch(Exception ex) {
            chatServidor("Failed to disconnect. \n");
        }
        isConnected = false;
    }
    
    
            //--------------------------//
    
    public class IncomingReader implements Runnable
    {
        @Override
        public void run() 
        {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";
            String listar = "Listar", chatear="chatear", Login = "Login", ListarIsConectado="ListarIsConectado";
            try 
            {

                while ((stream = reader.readLine()) != null) 
                {
                     data = stream.split(":");;
                     if (data[2].equals(connect))
                     {
                        //Cliente_chat.ta_chat.removeAll();
                        userAdd(data[0]);
                     } 
                     else if (data[2].equals(listar))
                     {
                         if(data[0].equals(username))
                            LlenarListar(data[1]);
                     } 
                     else if (data[2].equals(disconnect)) 
                     {
                         userRemove(data[0]);
                     } 
                     else if (data[2].equals(done)) 
                     {
                        //users.setText("");
                        writeUsers();
                        users.clear();
                     }
                     else if (data[2].equals(chat)) 
                     { 
                         if("Server".equals(data[0])){
                             chatServidor(data[0]+":"+data[1]);
                         }
                         System.out.println(data[0]+data[1]);
                     } 
                     else if (data[2].equals(chatear)) 
                     {
                         String[] usuarios = data[0].split("-");
                         LeerMensaje(usuarios,data[1]);
                     } 
                     else if (data[2].equals(ListarIsConectado)) 
                     {
                       if (arrayDeListaUsuario != null)LeerConectados(data[1]);

                     } 
                     else if (data[2].equals(Login)) 
                     {
                       if("true".equals(data[1])){
                           ListarUsuarios();
                           principal.setVisible(true);                          
                           principal.jLabelNombre.setText("BIENVENIDO "+username);
                           ListarConectados();
                           login.setVisible(false); 
                       }
                       else{
                        int resp = JOptionPane.showConfirmDialog(null, "No se pudo hacer login", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                       }
                     }
                }
           }catch(Exception ex) { }
        }
    }

    //--------------------------// 
    
    
}
