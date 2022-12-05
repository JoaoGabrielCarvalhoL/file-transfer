package br.com.carv.file.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 *
 * @author joao
 */
public class Client {
    public static void main(String[] args) {
        
        try {
            
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            
            String ipAddress = ""; 
            String fileName = "";
            
            Boolean isValid = false;
            
            while(!isValid) {
                
                System.out.println("Please enter a valid Server Ip Address: ");
                ipAddress = reader.readLine(); 
                InetAddressValidator validator = new InetAddressValidator();
                isValid = validator.isValid(ipAddress);
                        
            }
            
            System.out.println("Please enter a file name: ");
            fileName = reader.readLine();
            
            Socket socket = new Socket(ipAddress, 9090);
            InputStream inputByte = socket.getInputStream();
            BufferedInputStream input = new BufferedInputStream(inputByte);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            
            printWriter.println(fileName);
            
            int code = input.read();
            
            if (code == 1) {
                BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream("/media/joao/7687187209A1FF69/Teste/" + fileName));
                byte[] buffer = new byte[1024];
                int bytesRead = 0; 
                
                while ((bytesRead = input.read(buffer)) != -1) {
                    System.out.print(".");
                    outputFile.write(buffer, 0, bytesRead);
                    outputFile.flush();
                }
                
                System.out.println();
                System.out.println("File:  " + fileName + " was successfully downloaded!");
                
            } else {
                System.out.println("File is not present on the server");
            }
            
            
        } catch(Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
