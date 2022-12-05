package br.com.carv.file.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author joao
 */
public class ClientThread extends Thread {
    
    private Socket socket;
    private BufferedReader reader; 
    private BufferedOutputStream outputStream;
    private BufferedInputStream inputStream;
    
    public ClientThread(Socket socket) {
        this.socket = socket;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        
        try {
            
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputStream = new BufferedOutputStream(socket.getOutputStream());
            
            String fileName = reader.readLine();
            
            System.out.println("File name: " + fileName + " has been requested by " + socket.getInetAddress().getHostAddress());
            File file = new File(fileName);
            
            if (!file.exists()) {
                
                byte code = (byte) 0;
                outputStream.write(code);
                closeConnection();
                
            } else {
                
                outputStream.write((byte) 1);
                inputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer  = new byte[1024]; 
                int bytesRead = 0; 
                
                while((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                }
                
                closeConnection();
            }
            
            
        } catch(Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
    
    
    void closeConnection() {
        
        try {
            
            this.socket.close();
            this.reader.close();
            this.outputStream.close();
            this.inputStream.close();
        
        }catch(IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        
    }
    
}
