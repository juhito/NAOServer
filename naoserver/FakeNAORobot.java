package naoserver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FakeNAORobot {


    public FakeNAORobot(String url, String[] args) {
        System.out.println("Connection to fake NAO was successful");
    }


    public synchronized List<String> getBehaviors() throws InterruptedException {
        
        List<String> behaviorList = new ArrayList<>();

        behaviorList.add("Behavior1");
        behaviorList.add("Behavior2");
        behaviorList.add("taichi-dance-free");
        behaviorList.add("football");

        return(behaviorList);
    }

    public synchronized List<Double> getTempData() throws InterruptedException {

        Double randomCPUTemp = 1.0 + Math.random() * (80.0 - 1.0);
        Double randomBATTemp = 1.0 + Math.random() * (80.0 - 1.0);

        List<Double> dataList = new ArrayList<Double>();
        dataList.add(randomCPUTemp);
        dataList.add(randomBATTemp);

        return(dataList);
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