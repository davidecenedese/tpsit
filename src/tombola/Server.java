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
import java.util.Random;

public class Server extends Thread {

    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    String stringaServer = null;
    BufferedReader tastiera;

    public Server(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        try {
            comunica();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void comunica() throws Exception {
        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outVersoClient = new DataOutputStream(client.getOutputStream());
        try{
            String[] casella = generaCasella();
            
            //Genero la casella
            
            // +++ DOPO AVER GENERATO LA CASELLA +++
            String arrayToString = "";
            for(int i = 0; i < casella.length; i++){
                arrayToString += casella[i] + "|";
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
    
    public String[] generaCasella(){
        String[] casella = new String[15];
        Random rand = new Random();
        int i = 0;
        while(i < 15){
            casella[i] = Integer.toString(rand.nextInt(90) + 1);
            for(int j = 0; j <= i; j++){
                if(!casella[j].equals(casella[i])){
                    i++;
                } 
            }
        }
        return casella;
    }
}