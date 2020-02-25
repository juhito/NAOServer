package naoserver;

import java.net.*;
import java.util.Calendar;


public class NAOServer {
    private ServerSocket server;


    public NAOServer(String ip) throws Exception {
        this.server = new ServerSocket();
        this.server.bind(new InetSocketAddress(ip, 8888));
        new Thread(new BroadcastServer()).start();
    }

    private void listen(String[] args) {
        System.out.println("Server is listening on port: " + server.getLocalPort() + ", ip: " +
                server.getInetAddress());

        while(true) {
            try {
                Socket socket = server.accept();
                
                System.out.println("Starting a new thread for client: " + socket.getInetAddress().getHostName() + " at: " +
                        Calendar.getInstance().getTime());

                new Thread(new ClientHandler(socket, args)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
		NAOServer nss = new NAOServer("192.168.10.49");

		nss.listen(args);
	
	}
}
