package naoserver;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FakeNAORobot {

    public FakeNAORobot(String url, String[] args) {
        System.out.println("Connection to fake NAO was successful");
    }

    public synchronized List<Object> takeImage() throws InterruptedException, IOException {
        BufferedImage original = ImageIO.read(new File("//home//juhi//Pictures//tor_duck.png"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(original, "png", baos);

        baos.flush();

        byte[] imageByte = baos.toByteArray();
        baos.close();

        ByteBuffer byteBuffer = ByteBuffer.wrap(imageByte);

        byte[] byteData;
      
        if(byteBuffer.hasArray()) {
            byteData = byteBuffer.array();
        }
        else {
            byteData = new byte[byteBuffer.capacity()];
            ((ByteBuffer) byteBuffer.duplicate().clear()).get(byteData);
        }


        List<Object> data = new ArrayList<Object>();

        data.add(640);
        data.add(480);
        data.add(2);
        data.add("fakeColorSpace");
        data.add(19000);
        data.add(1900);
        data.add(byteData);

        return(data);
    }


    public synchronized List<String> getBehaviors() throws InterruptedException {
        return(null);
    }

    public synchronized void naoMove(float x, float y) throws InterruptedException {
        System.out.println(String.format("nao moved by: %.2f, %.2f", x, y));
    }

    public synchronized List<Double> getTempData() throws InterruptedException {
        Double randomCPUTemp = 1.0 + Math.random() * (80.0 - 1.0);
        Double randomBATTemp = 1.0 + Math.random() * (80.0 - 1.0);

        List<Double> data = new ArrayList<>();
        data.add(randomCPUTemp);
        data.add(randomBATTemp);

        return(data);
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