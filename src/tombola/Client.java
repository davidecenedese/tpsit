package tombola;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Cenedese, Stella
 */
public class Client {

    String serverName = "localhost";
    int serverPort = 6789;
    Socket socket;
    BufferedReader keyboard;
    String userString;
    String stringFromServer;
    DataOutputStream outToServer;
    BufferedReader inFromServer;

    int card[][];

    public Socket connect() {

        System.out.println("CLIENT partito in esecuzione ...");

        try {
            keyboard = new BufferedReader(new InputStreamReader(System.in));

            socket = new Socket(serverName, serverPort);

            outToServer = new DataOutputStream(socket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host Sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione col Server!");
            System.exit(1);
        }
        return socket;

    }

    public void receiveCard() {

        card = new int[3][5];

        try {

            String[] numbers = inFromServer.readLine().split("\\.");
            List<Integer> list = new ArrayList<Integer>();

            // riempimento cartella della tombola con i numeri ricevuti dal server
            int val = 0;
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 5; k++) {
                    card[i][k] = Integer.parseInt(numbers[val]);
                    list.add(card[i][k]);
                    val++;
                }
            }

            System.out.println("CARTELLA");
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 5; k++) {
                    System.out.print("[" + card[i][k] + "]");
                }
                System.out.println("");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Si è verificato un errore.");
            System.exit(1);
        }
    }

    public void receiveNumbers() {

        String inFromServerString;
        try {
            inFromServerString = inFromServer.readLine();
            while (!inFromServerString.equals("FINE")) {
                int randomNumber = Integer.parseInt(inFromServerString);
                System.out.println(randomNumber);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
