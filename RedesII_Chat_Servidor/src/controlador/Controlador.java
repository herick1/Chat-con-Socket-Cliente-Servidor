/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import paneles.Servidor_chat;
import paneles.pnlHome;

/**
 *
 * @author herick
 */
public class Controlador {

    public pnlHome pnlHome;
    public Servidor_chat Servidor_chat;
    BaseDeDatos BaseDeDatos;
    
   public Controlador(){
     pnlHome = new pnlHome();
     Servidor_chat = new Servidor_chat(this);
     BaseDeDatos = new BaseDeDatos();
   }
   
   
   ArrayList clientOutputStreams;
   ArrayList<String> users;
   int Socket = 10101;
   
   
   public void Conectar(){
        Thread starter = new Thread(new ServerStart());
        starter.start();
        Servidor_chat.ta_chat.append("Server started...\n");

   }
   
   public void usuariosConectados(){
               Servidor_chat.ta_chat.append("\n Online users : \n");
        for (String current_user : users)
        {
            Servidor_chat.ta_chat.append(current_user);
            Servidor_chat.ta_chat.append("\n");
        }
   }
   
   public void EnviarUsuariosConectado(){
       ArrayList<String[]>  usuarios= new ArrayList();
       ArrayList<String> usuarioArray= BaseDeDatos.ListaDeAmigosUsuario("");
       for(int i=0; i< usuarioArray.size(); i++){
           int bandera=0;
           String[] usuarioindividual = new String[2] ;
           for(int j=0; j< users.size(); j++){
                if(users.get(j).equals(usuarioArray.get(i))){
                    usuarioindividual[0]=usuarioArray.get(i);
                    usuarioindividual[1]= "true";
                    bandera= 1;
                }
            }
            if (bandera == 0){
                usuarioindividual[0]=usuarioArray.get(i);
                usuarioindividual[1]= "false";
            }
            usuarios.add(usuarioindividual);
       }
        Gson gson = new Gson();
        String JSON = gson.toJson(usuarios);
        String message =  "Server" + ":"+JSON + ":ListarIsConectado";
        tellEveryone(message);
   }
   
   public void Desconectar(){
        try
        {
            Thread.sleep(5000);                 //5000 milliseconds is five second.
        }
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}

        tellEveryone("Server:is stopping and all users will be disconnected.\n:Chat");
        Servidor_chat.ta_chat.append("Server stopping... \n");

        Servidor_chat.ta_chat.setText("");
    }
   
   
   public void EnviarMensaje(String mensage){
       tellEveryone("Server:"+mensage+":Chat");
   }
   
   public void loginCliente(String usuario, String pass){
       
            //System.out.println(pass);
       if(BaseDeDatos.ConsultarClienteLogin(usuario, pass)){
            tellEveryone(usuario+":true:Login");
       }else{
            tellEveryone(usuario+":false:Login");      
       }
   }
   
   public void agregarUsuarioAlServidor(String usuario, String Contraseña){
       if(BaseDeDatos.AgregarUsuarioServidor(usuario, Contraseña)){
            int resp = JOptionPane.showConfirmDialog(null, "agregado", "Correcto", JOptionPane.OK_OPTION, JOptionPane.OK_OPTION);
       }
        listaDeUsuarios("todos");
           
   }
   public void eliminarUsuarioAlServidor(String usuario){
       if(BaseDeDatos.EliminarUsuarioServidor(usuario)){
            int resp = JOptionPane.showConfirmDialog(null, "eliminado", "Correcto", JOptionPane.OK_OPTION, JOptionPane.OK_OPTION);
        listaDeUsuarios("todos");
       }else{
          int resp = JOptionPane.showConfirmDialog(null, "No se pudo eliminar", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
       }
   }
   public void ListaAgregar(String usuario, String Agregado){
       BaseDeDatos.AgregarUsuarioListaCliente(usuario, Agregado);
   }
   public void ListaEliminar(String usuario, String Agregado){
       BaseDeDatos.EliminarUsuarioListaCliente(usuario, Agregado);
   }
   
    public void listaDeUsuarios(String destinoUsuario) 
    {
        ArrayList<String>  usuarios= BaseDeDatos.ListaDeAmigosUsuario(destinoUsuario);
        Gson gson = new Gson();
        String JSON = gson.toJson(usuarios);
        String message =  destinoUsuario + ":"+JSON + ":Listar";
        tellEveryone(message);
    }
    
    
   public class ClientHandler implements Runnable	
   {
       BufferedReader reader;
       Socket sock;
       PrintWriter client;

       public ClientHandler(Socket clientSocket, PrintWriter user) 
       {
            client = user;
            try 
            {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            }
            catch (Exception ex) 
            {
                Servidor_chat.ta_chat.append("Unexpected error... \n");
            }

       }

       @Override
       public void run() 
       {
            String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat" ;
            String listar = "Listar", chatear="chatear", login= "Login";
            String ListarAgregar = "ListarAgregar", ListarEliminar = "ListarEliminar";
            String ListarIsConectado= "ListarIsConectado";
            String[] data;

            try 
            {
                while ((message = reader.readLine()) != null) 
                {
                    Servidor_chat.ta_chat.append("Received: " + message + "\n");
                    data = message.split(":");
                    
                    for (String token:data) 
                    {
                        Servidor_chat.ta_chat.append(token + "\n");
                    }

                    if (data[2].equals(connect)) 
                    {
                        tellEveryone((data[0] + ":" + data[1] + ":" + chat));
                        userAdd(data[0]);
                    } 
                    else if (data[2].equals(disconnect)) 
                    {
                        tellEveryone((data[0] + ":has disconnected." + ":" + chat));
                        userRemove(data[0]);
                    } 
                    else if (data[2].equals(listar)) 
                    {
                        listaDeUsuarios(data[0]);
                    } 
                    else if (data[2].equals(chat)) 
                    {
                        tellEveryone(message);
                    } 
                    else if (data[2].equals(chatear)) 
                    {
                        tellEveryone(message);
                    }
                    else if (data[2].equals(login)) 
                    {   
                        data[1]= ENCRYPT_KEY.decrypt(data[1]);                
                        Servidor_chat.ta_chat.append("Contraseña:Desencriptada"+data[1] + "\n");
                        loginCliente(data[0],data[1]);
                    } 
                    else if (data[2].equals(ListarAgregar)) 
                    {
                        ListaAgregar(data[0],data[1]);
                    }
                    else if (data[2].equals(ListarEliminar)) 
                    {
                        ListaEliminar(data[0],data[1]);
                    }
                     else if (data[2].equals(ListarIsConectado)) 
                     {
                        EnviarUsuariosConectado();
                     }
                    else 
                    {
                        Servidor_chat.ta_chat.append("No Conditions were met. \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                Servidor_chat.ta_chat.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(client);
             } 
	} 
    }
   
   
       public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();
            users = new ArrayList();  

            try 
            {
                ServerSocket serverSock = new ServerSocket(Socket);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				clientOutputStreams.add(writer);

				Thread listener = new Thread(new ClientHandler(clientSock, writer));
				listener.start();
				Servidor_chat.ta_chat.append("Got a connection. \n");
                }
            }
            catch (Exception ex)
            {
                Servidor_chat.ta_chat.append("Error making a connection. \n");
            }
        }
    }
    
    public void userAdd (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        Servidor_chat.ta_chat.append("Before " + name + " added. \n");
        users.add(name);
        Servidor_chat.ta_chat.append("After " + name + " added. \n");
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void userRemove (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);

        EnviarUsuariosConectado();
    }
    
    public void tellEveryone(String message) 
    {
	Iterator it = clientOutputStreams.iterator();

        while (it.hasNext()) 
        {
            try 
            {
                PrintWriter writer = (PrintWriter) it.next();
		writer.println(message);
		Servidor_chat.ta_chat.append("Sending: " + message + "\n");
                writer.flush();
                Servidor_chat.ta_chat.setCaretPosition(Servidor_chat.ta_chat.getDocument().getLength());

            } 
            catch (Exception ex) 
            {
		Servidor_chat.ta_chat.append("Error telling everyone. \n");
            }
        } 
    }
   


}



