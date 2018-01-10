/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tombola;

import java.net.Socket;

/**
 *
 * @author Davide Cenedese
 */
public class ClientData {
    
    private Socket client;
    private String[][] card;
    
    public ClientData(Socket client, String[][] card){
        this.client = client;
        this.card = card;
    }
    
    public Socket getSocket(){ return this.client; }
    public String[][] getCard(){ return this.card; }
}
