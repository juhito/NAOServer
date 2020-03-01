package naoserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Object[] clientMessageObject;
    private ArrayList<Object> clientArgumentsArray;
    private String clientMessage;

    //private static NAORobot robot;
    private static FakeNAORobot robot;

    public ClientHandler(Socket socket, String[] args) {
        this.socket = socket;
        System.out.println("Client: " + socket.getInetAddress().getHostAddress() + " connected");

        if(robot == null)
            //robot = new NAORobot("tcp://192.168.1.144:9559", args);
            robot = new FakeNAORobot("", args);
		else
			System.out.println("ROBOT ALREADY MADE YES");
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            while (socket.isConnected()) {
                clientMessageObject = (Object[]) in.readObject();
                clientMessage = (String) clientMessageObject[0];

				System.out.println("Received from client: " + Arrays.toString(clientMessageObject));

                clientArgumentsArray = new ArrayList<Object>(Arrays.asList(clientMessageObject));
                Collection<Object> c = clientArgumentsArray;
                c.remove(clientMessage);
                clientMessageObject = c.toArray();
                out.writeObject(this.findFunction(clientMessage, clientMessageObject));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object[] findFunction(String functionName, Object[] args) {
        try {

            Method method;
            Object[] messageToClient;

            if (args.length >= 1) {
                method = robot.getClass().getDeclaredMethod(functionName, getParameterTypes(robot.getClass(), functionName));
				messageToClient = (Object[]) method.invoke(robot, args);
            }
            else {
                method = robot.getClass().getDeclaredMethod(functionName);
                messageToClient = (Object[]) method.invoke(robot);
            }

            if (messageToClient != null) {
                System.out.println("Sending to client: " + Arrays.toString(messageToClient));
            }

            return messageToClient;
        } catch (Exception e) {
            System.out.println("Error: " + e.toString() + ", sending info to client!");
            return(new Object[]{"Sorry, that method isn't implement on the server!"});
        }
    }

    private Class<?>[] getParameterTypes(Class<?> cl, String method) {
        try {
            for (Method m : cl.getDeclaredMethods()) {
                if (m.getName().equals(method)) {
                    return m.getParameterTypes();
                }
            }
            // If nothing have been found return null value
            return(null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return(null);
        }
    }
}
