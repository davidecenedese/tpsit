package tombola;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Cenedese, Stella
 */
public class Server extends Thread implements Runnable {

    ServerSocket server = null;
    Socket client = null;
    String recivedString = null;
    String modificatedString = null;
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    String serverString = null;
    BufferedReader keyboard;
    List<ClientData> cards;
    Boolean firstBox;
    int nBoxes;
    int[] numbers;
    int[] firstWin = new int[4];
    boolean bingo = false;

    public Server(Socket socket) {
        this.client = socket;
        this.cards = new ArrayList<ClientData>();
        firstBox = true;
        nBoxes = 0;
        numbers = new int[90];
    }

    @Override
    public void run() {
        try {
            comunicate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void comunicate() throws Exception {
        inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outToClient = new DataOutputStream(client.getOutputStream());
        try {
            String arrayToString;
            //controllo che non ci siano caselle generate in precedenza uguali a quella appena generata
            boolean equalBoxes = true;
            do {
                //Genero la casella
                int[] box = generateBox();

                // +++ DOPO AVER GENERATO LA CASELLA +++
                arrayToString = "";
                for (int i = 0; i < box.length; i++) {
                    arrayToString += box[i] + ".";
                }
                if (firstBox == true) {
                    equalBoxes = false;
                    firstBox = false;
                } else {
                    for (int i = 0; i < cards.size(); i++) {
                        equalBoxes = cards.get(i).equals(arrayToString);
                        if (equalBoxes == true) {
                            break;
                        }
                    }
                }
            } while (equalBoxes);
            //aggiungo casella alla lista
            cards.add(new ClientData(client, buildCard(arrayToString)));

            System.out.println(++nBoxes + ": " + arrayToString);
            //invio la casella al client
            outToClient.writeBytes(arrayToString + '\n');
            /*
            Boolean t = true;
            do{
                stringaRicevuta = inDalClient.readLine();
                if(!"quit".equals(stringaRicevuta)){
                    System.out.println(stringaRicevuta);
                    tastiera = new BufferedReader(new InputStreamReader(System.in));
                    stringaServer = tastiera.readLine();
                    outVersoClient.writeBytes("\tServer: " + stringaServer + '\n');
                }else{
                    t = false;
                    outVersoClient.close();
                    inDalClient.close();
                    System.out.println("9 Chiusura socket" + client);
                    client.close();
                }
            }while(t);
             */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore");
            System.exit(1);
        }
    }

    public int[] generateBox() {
        int[] box = new int[15];
        //String[] box = new String[15];
        Random rand = new Random();

        int i = 0;
        //Genero i numeri casuali
        while (i < 15) {
            box[i] = rand.nextInt(90) + 1;
            //verifico che non ce ne siano di uguali nella stessa casella
            i = controlEqualNumbers(box, box[i], i);
        }
        //Riordino in modo crescente i numeri della casella
        bubbleSort(box);
        return box;
    }

    public int controlEqualNumbers(int[] temp, int num, int i) {
        boolean different = true;
        int j = 0;
        while (j < i && different == true) {
            if (temp[j] != num) {
                different = true;
                j++;
            } else {
                different = false;
            }
        }
        if (different == true) {
            i++;
        }
        return i;
    }

    public void bubbleSort(int[] numbers) {
        int temp = 0;
        int j = numbers.length - 1;
        while (j > 0) {
            for (int i = 0; i < j; i++) {
                if (numbers[i] > numbers[i + 1]) {
                    temp = numbers[i];
                    numbers[i] = numbers[i + 1];
                    numbers[i + 1] = temp;
                }
            }
            j--;
        }
    }

    public int extractNumber(List<Socket> clients) {
        Socket winner = null;
        int number = 0;
        boolean bingo = false;
        Random rand = new Random();

        for (Socket client : clients) {
            try {
                inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String read = inFromClient.readLine();

                switch (read) {
                    case "TOMBOLA":
                        bingo = true;
                        winner = client;
                        break;
                    case "AMBO":
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).getSocket() == client) {
                                if (checkWin(cards.get(i).getCard()).equals("AMBO\n")) {
                                    System.out.println("Il client " + client + " ha fatto " + read);
                                }
                            }
                        }
                        break;
                    case "TERNA":
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).getSocket() == client) {
                                if (checkWin(cards.get(i).getCard()).equals("TERNA\n")) {
                                    System.out.println("Il client " + client + " ha fatto " + read);
                                }
                            }
                        }
                        break;
                    case "QUATERNA":
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).getSocket() == client) {
                                if (checkWin(cards.get(i).getCard()).equals("QUATERNA\n")) {
                                    System.out.println("Il client " + client + " ha fatto " + read);
                                }
                            }
                        }
                        break;
                    case "CINQUINA":
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).getSocket() == client) {
                                if (checkWin(cards.get(i).getCard()).equals("CINQUINA\n")) {
                                    System.out.println("Il client " + client + " ha fatto " + read);
                                }
                            }
                        }
                        break;
                }

            } catch (IOException ex) {
            }
        }

        if (!bingo) {
            while (numbers[(number = rand.nextInt(90) + 1) - 1] != 0) {
            }

            numbers[number - 1] = number;
            try {
                for (Socket client : clients) {
                    outToClient = new DataOutputStream(client.getOutputStream());
                    outToClient.writeBytes(String.valueOf(number) + "\n");
                }
            } catch (IOException ex) {
                System.out.println("Client non raggiungibile");
            }
        } else {
            try {
                for (Socket client : clients) {
                    outToClient = new DataOutputStream(client.getOutputStream());
                    outToClient.writeBytes("SHUTDOWN\n");
                    inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    System.out.println("Il client " + winner.toString() + " ha fatto TOMBOLA");

                    if (inFromClient.readLine().equals("OK")) {
                        outToClient.writeBytes("Il client " + winner.toString() + " ha fatto TOMBOLA\n");
                        Thread.sleep(100);
                        System.exit(0);
                    }
                }
            } catch (IOException ex) {}
            catch (InterruptedException ex) {}
        }

        return number;
    }

    public String checkWin(String[][] card) {
        int count = 0;
        int bingo = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 5; k++) {
                for (int j = 0; j < numbers.length; j++) {
                    if (card[i][k].equals(String.valueOf(numbers[j]))) {
                        count++;
                        bingo++;
                    }
                }
            }

            switch (count) {
                case 2:
                    firstWin[0]++;
                    if (firstWin[0] == 1) {
                        return "AMBO\n";
                    }
                    break;
                case 3:
                    firstWin[1]++;
                    if (firstWin[1] == 1) {
                        return "TERNA\n";
                    }
                    break;
                case 4:
                    firstWin[2]++;
                    if (firstWin[2] == 1) {
                        return "QUATERNA\n";
                    }
                    break;
                case 5:
                    firstWin[3]++;
                    if (firstWin[3] == 1) {
                        return "CINQUINA\n";
                    }
                    break;
            }

            count = 0;
        }

        if (bingo == 15) {
            this.bingo = true;
            return "TOMBOLA\n";
        } else {
            bingo = 0;
        }

        return "";
    }

    public String[][] buildCard(String stringToArray) {
        String[] numbers = stringToArray.split("\\.");

        String[][] card = new String[3][5];

        int val = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 5; k++) {
                card[i][k] = numbers[val];
                val++;
            }
        }
        return card;
    }
}
