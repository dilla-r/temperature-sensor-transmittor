package project3task1client;

import java.util.Date;

/**
 * October, 2016
 *
 * This is a JAX-WS Client that makes calls to a web service sending temperature
 * data from sensors.
 *
 * @author Rebecca Dilla
 */
public class Project3Task1Client {

    /**
     * The main method calls the high temperature method for sensor 1, the low
     * temperature method for sensor 2, the high temperature method for sensor
     * 1, then the high temperature method for sensor 1 again, but with an
     * incorrect signature. It then requests all recorded temperature from the
     * server, and the last recorded temperature for sensor 1.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SignatureWriter sw = new SignatureWriter(); //intantiates the Signiture Writing class

        //Generates TimeStamp and calls highTemperature method for sensor 1
        //The SignatureWriter is also called to write a signature for the temperature being recorded
        String d = new Date().toString();//Creates timestamp from system clock
        System.out.println(highTemperature("1", d, "Celsius", "112.04", sw.writeSignature("1", d, "Celsius", "112.04")));

        //Generates TimeStamps and calls lowTemperature for sensor 2
        //The SignatureWriter is also called to write a signature for the temperature being recorded
        d = new Date().toString();//creates a time stamp from the system clock
        System.out.println(lowTemperature("2", d, "Fahrenheit", "-14.54", sw.writeSignature("2", d, "Fahrenheit", "-14.54")));

        //Generates TimeStamp and calls highTemperature method for sensor 1
        //The SignatureWriter is also called to write a signature for the temperature being recorded
        d = new Date().toString();//Creates timestamp from system clock
        System.out.println(highTemperature("1", d, "Celsius", "108.04", sw.writeSignature("1", d, "Celsius", "108.04")));

        //Generates TimeStamp and calls highTemperature method for sensor 1
        //The SignatureWriter is also called to write a signature for the temperature being recorded
        //However, the signature created will be invalid, because the temperature in the signature generated 
        //does not match the temperature information passed to the server.
        //Because of the incorrect signature, the temperature will not be recorded.
        d = new Date().toString();//creates a timestamp from the system clock
        System.out.println(highTemperature("1", d, "Celsius", "111.11", sw.writeSignature("1", d, "Celsius", "111.12")));

        //This call asks the server for all of the recorded temperatures
        System.out.println(getTemperatures());

        //This call requests the last temperature recorded from sensor 1
        System.out.println(getLastTemperature("1"));
    }

    /**
     * This method calls the highTemperature method from the web service
     *
     * @param sensorID: sensor id (either 1 or 2)
     * @param timeStamp: time of the recorded sensor reading
     * @param type: either Celsius or Fahrenheit
     * @param temperature: Recorded temperature written as a double, encoded as
     * a String
     * @param signature: Signature generated by hashing the bytes of the
     * information and the sensor's private keys
     * @return Message from WebService
     */
    private static String highTemperature(java.lang.String sensorID, java.lang.String timeStamp, java.lang.String type, java.lang.String temperature, java.lang.String signature) {
        cmu.edu.andrew.rid.Project3Task1Service_Service service = new cmu.edu.andrew.rid.Project3Task1Service_Service();
        cmu.edu.andrew.rid.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.highTemperature(sensorID, timeStamp, type, temperature, signature);
    }

    /**
     * This method retrieves the recorded temperature from the web service
     *
     * @return recorded temperatures
     */
    private static String getTemperatures() {
        cmu.edu.andrew.rid.Project3Task1Service_Service service = new cmu.edu.andrew.rid.Project3Task1Service_Service();
        cmu.edu.andrew.rid.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.getTemperatures();
    }

    /**
     * This method returns the last recorded temperature from the specified
     * sensor
     *
     * @param sensorID sensor to retrieve last temperature from (either 1 or 2)
     * @return last recorded temperature from the specified sensor
     */
    private static String getLastTemperature(java.lang.String sensorID) {
        cmu.edu.andrew.rid.Project3Task1Service_Service service = new cmu.edu.andrew.rid.Project3Task1Service_Service();
        cmu.edu.andrew.rid.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.getLastTemperature(sensorID);
    }

    /**
     * This method calls the lowTemperature method from the web service
     *
     * @param sensorID: sensor id (either 1 or 2)
     * @param timeStamp: time of the recorded sensor reading
     * @param type: either Celsius or Fahrenheit
     * @param temperature: Recorded temperature written as a double, encoded as
     * a String
     * @param signature: Signature generated by hashing the bytes of the
     * information and the sensor's private keys
     * @return Message from WebService
     */
    private static String lowTemperature(java.lang.String sensorID, java.lang.String timeStamp, java.lang.String type, java.lang.String temperature, java.lang.String signature) {
        cmu.edu.andrew.rid.Project3Task1Service_Service service = new cmu.edu.andrew.rid.Project3Task1Service_Service();
        cmu.edu.andrew.rid.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.lowTemperature(sensorID, timeStamp, type, temperature, signature);
    }

}
