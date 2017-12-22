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

    int[] firstWin = new int[4];
    String card[][];
    boolean bingo = false;

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

        card = new String[3][5];

        try {
            String[] numbers = inFromServer.readLine().split("\\.");

            sortCard(numbers);
            printCard();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Si è verificato un errore.");
            System.exit(1);
        }
    }

    public void receiveNumbers() {

        try {
            String inFromServerString;
            while (!(inFromServerString = inFromServer.readLine()).equals("FINE")) {
                int randomNumber = Integer.parseInt(inFromServerString);
                System.out.println("Numero estratto: " + randomNumber);
                checkNumber(randomNumber);
                printCard();
                System.out.print(checkWin());
                if(bingo == true){
                    System.exit(0);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sortCard(String[] numbers) {
        List<String> list = new ArrayList<>();
        int val = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 5; k++) {
                card[i][k] = numbers[val];
                list.add(card[i][k]);
                val++;
            }
        }
    }

    public void checkNumber(int number) {
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 5; k++) {
                if (card[i][k].equals(String.valueOf(number))) {
                    card[i][k] = "X";
                }
            }
        }
    }

    public String checkWin() {
        int count = 0;
        int bingo = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 5; k++) {
                if (card[i][k].equals("X")) {
                    count++;
                    bingo++;
                }
            }

            switch (count) {
                case 2:
                    firstWin[0]++;
                    if (firstWin[0] == 1) {
                        return "AMBO\n\n";
                    }
                    break;
                case 3:
                    firstWin[1]++;
                    if (firstWin[1] == 1) {
                        return "TERNA\n\n";
                    }
                    break;
                case 4:
                    firstWin[2]++;
                    if (firstWin[2] == 1) {
                        return "QUATERNA\n\n";
                    }
                    break;
                case 5:
                    firstWin[3]++;
                    if (firstWin[3] == 1) {
                        return "CINQUINA\n\n";
                    }
                    break;
            }

            count = 0;
        }

        if (bingo == 15) {
            this.bingo = true;
            return "*** TOMBOLA ***\n\n";
        } else {
            bingo = 0;
        }
        
        return "";
    }

    public void printCard() {
        System.out.println("CARTELLA");
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 5; k++) {
                System.out.print("[" + (String.format("%2s", card[i][k])) + "]");
            }
            System.out.println("");
        }
        System.out.println("");
    }

}
