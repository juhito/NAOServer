package naoserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastServer implements Runnable {
    private DatagramSocket scannerSocket;

    public BroadcastServer() throws Exception {
        this.scannerSocket = new DatagramSocket(8889, InetAddress.getByName("0.0.0.0"));
        this.scannerSocket.setBroadcast(true);
    }

    @Override
    public void run() {
        try {
            while(true) {
                byte[] messageBuffer = new byte[15000];
                System.out.println("New BroadcastServer started");

                DatagramPacket messagePacket = new DatagramPacket(messageBuffer, messageBuffer.length);
                scannerSocket.receive(messagePacket);

                // Packet received
                System.out.println("Discovery packet received from: " + messagePacket.getAddress().getHostAddress());
                System.out.println("Packet received; data: " + new String(messagePacket.getData()));

                // See if the packet holds the right message
                String message = new String(messagePacket.getData()).trim();
                if(message.equals("IM_LOOKING_FOR_NAO")) {
                    byte[] responseData = "YOU_FOUND_ME".getBytes();

                    // Send a response
                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, 
                        messagePacket.getAddress(), messagePacket.getPort());
                    scannerSocket.send(responsePacket);

                    System.out.println("Sent packet to: " + responsePacket.getAddress().getHostAddress() +
                        "; data: " + new String(responsePacket.getData()).trim());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }    
}