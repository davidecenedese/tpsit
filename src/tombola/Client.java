/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombola;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Davide Cenedese
 */
public class Client {

    String serverName = "127.0.0.1";
    int serverPort = 6789;
    Socket socket;
    BufferedReader keyboard;
    String userString;
    String stringFromServer;
    DataOutputStream outToServer;
    BufferedReader inFromServer;

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

    public void comunicate() {
        try {

            int card[][] = new int[3][5];
            List<Integer> list = new ArrayList<Integer>();

            // riempimento cartella della tombola con i numeri ricevuti dal server
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 5; k++) {
                    //card[i][k] = inFromServer.read();
                    card[i][k] = new Random().nextInt((90 - 1) + 1) + 1;
                    list.add(card[i][k]);
                }
            }

            System.out.println("NON ORDINATO");
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 5; k++) {
                    System.out.print("[" + card[i][k] + "]");
                }
                System.out.println("");
            }

            // ordinamento della cartella
            Collections.sort(list);
            
            int val = 0;
            for(int i = 0; i < 3; i++){
                for(int k = 0; k < 5; k++){
                    card[i][k] = list.get(val);
                    val++;
                }
            }

            System.out.println("ORDINATO");
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
}
