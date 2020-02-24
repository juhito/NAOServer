
import java.net.*;
import java.util.Calendar;


public class NAOServer {
    private ServerSocket server;
    private int clientID = 0;

    public NAOServer(String ip) throws Exception {
        this.server = new ServerSocket();
        this.server.bind(new InetSocketAddress(ip, 8888));
    }

    private void listen(String[] args) {
        System.out.println("Server is listening on port: " + server.getLocalPort() + ", ip: " +
                server.getInetAddress());

        while(true) {
            try {
                Socket socket = server.accept();

                clientID++;
                System.out.println("Starting a new thread for client: " + clientID + " at: " +
                        Calendar.getInstance().getTime());

                new Thread(new ClientHandler(socket, args)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
		NAOServer nss = new NAOServer("192.168.1.144");

		nss.listen(args);
	
	}
}
