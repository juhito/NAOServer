package naoserver;

import java.util.ArrayList;
import java.util.List;
import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Tuple4;
import com.aldebaran.qi.helper.proxies.ALBattery;
import com.aldebaran.qi.helper.proxies.ALBehaviorManager;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.PackageManager;
import com.aldebaran.qi.helper.proxies.ALSystem;


public class NAORobot {
    private Application app;

    // submodules
    private ALTextToSpeech tts;
    private ALMemory memory;
    private ALBattery batteryManager;
    private ALBehaviorManager behaviorManager;
    private PackageManager packageManager;
    private ALSystem systemManager;

    public NAORobot(String url, String[] args) {

        try {
            // create a new connection to NAO
            this.app = new Application(args, url);
            this.app.start();

            System.out.println("Connection to NAO was successful");

            // Start submodules
            this.tts = new ALTextToSpeech(this.app.session());
            this.memory = new ALMemory(this.app.session());
            this.behaviorManager = new ALBehaviorManager(this.app.session());
            this.packageManager = new PackageManager(this.app.session());
            this.systemManager = new ALSystem(this.app.session());
            this.batteryManager = new ALBattery(this.app.session());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String> getBehaviors() throws InterruptedException, CallError {
        return(behaviorManager.async().getInstalledBehaviors().get());
    }

    public synchronized List<Object> getTempData() throws InterruptedException, CallError {
        Object cpuTemp = memory.async().getData("Device/SubDeviceList/Head/Temperature/Sensor/Value").get();
        Object batteryTemp = memory.async().getData("Device/SubDeviceList/Battery/Temperature/Sensor/Value").get();
        
        return(new ArrayList<Object>() {{
            add(cpuTemp); 
            add(batteryTemp);
        }});
    }

    public synchronized Integer getBatteryData() throws InterruptedException, CallError {
        return(batteryManager.async().getBatteryCharge().get());
    }

    public synchronized void installBehavior(String behavior) throws InterruptedException, CallError {
        if(!this.behaviorManager.async().isBehaviorInstalled(behavior).get()) {
            this.packageManager.async().install(behavior);
        }
        else {
            System.out.println("Package / Behavior already installed...");
        }
    }

    public synchronized void getSystemData() throws InterruptedException, CallError {
        List<Tuple4<String, String, Long, Long>> data = this.systemManager.async().diskFree(true).get();

        for(int i = 0; i < data.size(); i++) {
            System.out.println("var0: " + data.get(i).var0 + ", var1: " + data.get(i).var1 + ", var2: " + data.get(i).var2
                + ", var3: " + data.get(i).var3);
        }
    }

    public synchronized void stopBehavior(String behavior) throws InterruptedException, CallError {
        if(this.behaviorManager.async().isBehaviorRunning(behavior).get()) {
            this.behaviorManager.async().stopBehavior(behavior);
        }
        else {
            System.out.println("Couldn't find a running behavior by that name...");
        }
    }

    public synchronized void startBehavior(String behavior) throws InterruptedException, CallError {
        if(this.behaviorManager.async().isBehaviorInstalled(behavior).get()) {
            if(!this.behaviorManager.async().isBehaviorRunning(behavior).get()) {
                this.behaviorManager.async().runBehavior(behavior);
            }
            else {
                System.out.println("Behavior is already running");
            }
        }
        else {
            System.out.println("Behavior not found..");
        }
    }

    public synchronized void naoSpeak(String message) throws InterruptedException, CallError {
        this.tts.async().say(message);
    }
}
