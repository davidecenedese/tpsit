package tombola;

/**
 *
 * @authors Cenedese, Stella
 */
public class MainClient {

    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
        client.receiveCard();
        client.receiveNumbers();
    }

}
