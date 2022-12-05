package br.com.carv.file.transfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao
 */
public class Server {

    public static void main(String[] args) {
        
        try {
            
            ServerSocket serverSocket = new ServerSocket(9090);
            Boolean isStoped = false; 
            
            while(!isStoped) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket);
                clientThread.start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        
        
    }
}
