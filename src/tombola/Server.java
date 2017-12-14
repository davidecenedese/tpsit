/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombola;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server extends Thread {

    ServerSocket server = null;
    Socket client = null;
    String recivedString = null;
    String modificatedString = null;
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    String serverString = null;
    BufferedReader keyboard;
    List<String> list;
    
    public Server(Socket socket) {
        this.client = socket;
        this.list = new ArrayList<>();
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
        try{
            /*
            //controllo che non ci siano caselle generate in precedenza uguali a quella appena generata
            boolean equalBoxes = true;
            do{
                
            }while(equalBoxes);
            */
            
            //Genero la casella
            int[] box = generateBox();

            // +++ DOPO AVER GENERATO LA CASELLA +++
            String arrayToString = "";
            for(int i = 0; i < box.length; i++){
                arrayToString += box[i] + "|";
            }
            
            System.out.println(arrayToString);
            
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
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore");
            System.exit(1);
        }
    }
    
    public int[] generateBox(){
        int[] box = new int[15];
        //String[] box = new String[15];
        Random rand = new Random();
        
        int i = 0;
        //Genero i numeri casuali
        while(i < 15){
            box[i] = rand.nextInt(90) + 1;
            //verifico che non ce ne siano di uguali nella stessa casella
            i = controlEqualNumbers(box, box[i], i); 
        }
        //Riordino in modo crescente i numeri della casella
        bubbleSort(box);        
        return box;
    }
    
    public int controlEqualNumbers(int[] temp, int num, int i){
        boolean different = true;
        int j = 0;
        while(j <= i && different == true){
            if(temp[j] != num){
                different = true;
                j++;
            } else {
                different = false;
            }
        }
        if(different == true){
            i++;
        }    
        return i;
    }
    
    public void bubbleSort(int[] numbers) {
        int temp = 0;
        int j = numbers.length-1;
        while(j>0) {
            for(int i=0; i<j; i++) {
                if(numbers[i]>numbers[i+1]){
                    temp=numbers[i]; 
                    numbers[i]=numbers[i+1];
                    numbers[i+1]=temp;
                }
            }
            j--; 
        }
    }
}