package tombola;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @authors Cenedese, Stella
 */
public class MainServer {

    Socket socket;
    Server serverThread;
    List<Socket> clients = new ArrayList<>();
    
    public void start() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);

            while (clients.size() < 1) {
                System.out.println("Server in attesa...");
                socket = serverSocket.accept();
                clients.add(socket);
                System.out.println("Server socket " + socket);
                serverThread = new Server(socket);
                serverThread.start();
                serverThread.join();
            }
            
            int randomNumber = 0;
            while((randomNumber = serverThread.extractNumber(clients)) != -1){
                System.out.println("Numero inviato: " + randomNumber);
                Thread.sleep(1000);
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