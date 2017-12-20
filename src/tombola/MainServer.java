/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombola;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simonestella
 */
public class MainServer {

    Socket socket;
    Server serverThread;
    List<Socket> clients = new ArrayList<>();
    
    public void start() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);

            /*final int secondi = 0;
            new Runnable() {
                @Override
                public void run() {
                    secondi++;
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };*/
            

            while (clients.size() < 1) {
                System.out.println("1 Server in attesa...");
                socket = serverSocket.accept();
                clients.add(socket);
                System.out.println("3 Server socket " + socket);
                serverThread = new Server(socket);
                serverThread.start();
                
            }
            
            int randomNumner = 0;
            while((randomNumner = serverThread.extractNumber(clients)) != -1){
                System.out.println(randomNumner);
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException, Exception {
        MainServer tcpServer = new MainServer();
        tcpServer.start();
    }
}
