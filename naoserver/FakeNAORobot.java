package naoserver;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FakeNAORobot {


    public FakeNAORobot(String url, String[] args) {
        System.out.println("Connection to fake NAO was successful");
    }


    public synchronized Object[] getBehaviors() throws InterruptedException {
        return(new Object[]{
            "Behavior1",
            "Behavior2",
            "taichi-dance-free",
            "football"
        });
    }

    public synchronized Object[] getTempData() throws InterruptedException {

        Double randomCPUTemp = 1.0 + Math.random() * (80.0 - 1.0);
        Double randomBATTemp = 1.0 + Math.random() * (80.0 - 1.0);

        return(new Object[] {
            randomCPUTemp,
            randomBATTemp
        });
    }

    public synchronized Object[] getBatteryData() throws InterruptedException {
        Double randomBATCharge = 1.0 + Math.random() * (100.0 - 1.0);

        return(new Object[] {
            randomBATCharge
        });
    }

    public synchronized Object[] getCommands() throws InterruptedException {
        ArrayList<String> methods = new ArrayList<String>();
        
        for(Method m : this.getClass().getDeclaredMethods()) {
            methods.add(m.getName());    
        }

        return(methods.toArray());
    }

    
}
/* Implement this later maybe
class FakeBehaviorManager {

    private FakeBehavior runningBehavior;
    private ArrayList<FakeBehavior> installedBehaviors;


    public FakeBehaviorManager() {
        installedBehaviors = new ArrayList<FakeBehavior>();
    }

    public synchronized Boolean isBehaviorRunning() {
        return(this.runningBehavior.isRunning());
    }

    public synchronized void runBehavior(String behavior) {
        // DO LATER Double randomTime = 10.0 + Math.random() * (50.0 - 10.0);
    }

    public FakeBehavior findBehavior(String behavior) {
        for(FakeBehavior f : installedBehaviors) {
            if(f.getName().equals(behavior)) {
                return(f);
            }
        }
        return(null);
    }

    public synchronized Boolean isBehaviorInstalled(String behavior) {
        Boolean found = false;

        for(FakeBehavior f : installedBehaviors) {
            if(f.getName().equals(behavior)) {
                found = true;
                break;
            }
        }

        return(found);
    }

    public synchronized void installBehavior(String behavior) {
        if(isBehaviorInstalled(behavior)) {
            System.out.println("Behavior already installed!");
        }
        else {
            this.installedBehaviors.add(new FakeBehavior(behavior));
        }
    }
}

class FakeBehavior implements Runnable {
    private String behaviorName;
    private Boolean isRunning;

    public FakeBehavior(String name) {
        this.behaviorName = name;
        isRunning = false;
    }

    public Boolean isRunning() {
        return(this.isRunning);
    }

    public String getName() {
        return(this.behaviorName);
    }

    @Override
    public void run() {

    }
}
*/