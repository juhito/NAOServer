package naoserver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FakeNAORobot {


    public FakeNAORobot(String url, String[] args) {
        System.out.println("Connection to fake NAO was successful");
    }

    public synchronized List<String> getBehaviors() throws InterruptedException {
        return(new ArrayList<String>() {{
            add("Behavior1");
            add("Behavior2");
            add("taichi-dance-free");
            add("football");
        }});
    }

    public synchronized void naoMove(float x, float y) throws InterruptedException {
        System.out.println(String.format("nao moved by: %.2f, %.2f", x, y));
    }

    public synchronized List<Double> getTempData() throws InterruptedException {
        Double randomCPUTemp = 1.0 + Math.random() * (80.0 - 1.0);
        Double randomBATTemp = 1.0 + Math.random() * (80.0 - 1.0);

        return(new ArrayList<Double>() {{
            add(randomCPUTemp);
            add(randomBATTemp);
        }});
    }

    public synchronized Double getBatteryData() throws InterruptedException {
        Double randomBATCharge = 1.0 + Math.random() * (100.0 - 1.0);

        return(randomBATCharge);
    }

    public synchronized List<String> getCommands() throws InterruptedException {
        List<String> methods = new ArrayList<String>();
        
        for(Method m : this.getClass().getDeclaredMethods()) {
            methods.add(m.getName());    
        }

        return(methods);
    }    
}