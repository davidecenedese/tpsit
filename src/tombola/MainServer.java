/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombola;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author simonestella
 */
public class MainServer {

    public void start() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            int giocatori = 0;

            /*final int secondi = 0;
            new Runnable() {
                @Override
                public void run() {
                    secondi++;
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };*/
            

            while (true) {
                System.out.println("1 Server in attesa...");
                Socket socket = serverSocket.accept();
                System.out.println("3 Server socket " + socket);
                Server serverThread = new Server(socket);
                serverThread.start();
                
            }
            /*
            while(serverThread.extractNumber() != -1){
                
            }*/
            
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
