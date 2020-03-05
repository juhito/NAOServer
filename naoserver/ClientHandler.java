package naoserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Object[] clientMessageObject;
    private String clientCommand;

    //private static NAORobot robot;
    private static FakeNAORobot robot;

    public ClientHandler(Socket socket, String[] args) {
        this.socket = socket;
        System.out.println("Client: " + socket.getInetAddress().getHostAddress() + " connected");

        if(robot == null)
            robot = new FakeNAORobot("tcp://192.168.1.118:9559", args);
            //robot = new FakeNAORobot("", args);
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
                clientCommand = (String) clientMessageObject[0];

                if(clientMessageObject.length > 1) {
                    System.out.println(String.format("Received from client: %s %s",
                        (String) clientMessageObject[0], 
                        Arrays.toString((Object[]) clientMessageObject[1])));
                }
                else {
                    System.out.println("Received from client: " + Arrays.toString(clientMessageObject));
                }
                
                ArrayList<Object> a = new ArrayList<Object>(Arrays.asList(clientMessageObject));
                a.remove(clientCommand);
                clientMessageObject = a.toArray();
                out.writeObject(this.findFunction(clientCommand, clientMessageObject));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object findFunction(String functionName, Object[] args) {
        try {
            Method method;
            Object messageToClient;

            if (args.length >= 1) {
                method = robot.getClass().getDeclaredMethod(functionName, getParameterTypes(robot.getClass(), functionName));
				messageToClient = method.invoke(robot, (Object[]) args[0]);
            }
            else {
                method = robot.getClass().getDeclaredMethod(functionName);
                messageToClient = method.invoke(robot);
            }

            if (messageToClient != null) {
                System.out.println("Sending to client: " + messageToClient);
            }

            return messageToClient;
        } catch (Exception e) {
            e.printStackTrace();
            return(new String("Sorry, that method isn't implement on the server!"));
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
