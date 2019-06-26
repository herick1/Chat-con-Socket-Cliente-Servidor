/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redesii_chat_cliente;

import controlador.Controlador;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import principal.Login;

/**
 *
 * @author herick
 */
public class RedesII_Chat_Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
                // TODO code application logic here
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Controlador controlador;
                try {
                    controlador = new Controlador();
                    controlador.login.setVisible(true);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(RedesII_Chat_Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        };
        runnable.run();
    }
    
}
