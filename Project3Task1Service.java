package cmu.edu.andrew.rid;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.LinkedList;

/**
 * October 2016
 *
 * This is a webs service that can record data transmitted from sensors. It
 * validates the signature of the transmitted data, and, if the signature is
 * valid, stores the data in a LinkedList. It also has methods to return the
 * list of recorded temperatures, and return the last recorded temperature of a
 * specified sensor
 *
 * @author RebeccaDilla
 */
@WebService(serviceName = "Project3Task1Service")
public class Project3Task1Service {

    LinkedList tempList = new LinkedList(); //Linked List for storing valid temperature recordings

    //Sensor 1's private keys
    BigInteger e1 = new BigInteger("65537");
    BigInteger n1 = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

    //Sensor 2's private keys
    BigInteger e2 = new BigInteger("65537");
    BigInteger n2 = new BigInteger("3377327302978002291107433340277921174658072226617639935915850494211665206881371542569295544217959391533224838918040006450951267452102275224765075567534720584260948941230043473303755275736138134129921285428767162606432396231528764021925639519");

    /**
     * This operation validated the signature, and stores valid temperature
     * recordings It takes in sensorID(1 or 2), timeStamp, type(Fahrenheit or
     * Celsius), temperature, and signature
     *
     * Web service operation
     */
    @WebMethod(operationName = "highTemperature")
    public String highTemperature(@WebParam(name = "sensorID") String sensorID, @WebParam(name = "timeStamp") String timeStamp, @WebParam(name = "type") String type, @WebParam(name = "temperature") String temperature, @WebParam(name = "signature") String signature) {

        //Validates the signature 
        boolean validSig = validateInput(sensorID, timeStamp, type, temperature, signature);

        //If the signature is valid, the recorded temp is added to the LinkedList
        if (validSig) {

            String[] tempInfo = {sensorID, timeStamp, type, temperature}; //Put record information into String Array
            tempList.add(tempInfo); //Add String Array to LinkedList

            return "Added " + temperature + " " + type; //Sends return message to the client
        }
        return "Invalid Signature."; //Sends message to client if signature is invalid
    }

    /**
     * This operation validated the signature, and stores valid temperature
     * recordings It takes in sensorID(1 or 2), timeStamp, type(Fahrenheit or
     * Celsius), temperature, and signature
     *
     * Web service operation
     */
    @WebMethod(operationName = "lowTemperature")
    public String lowTemperature(@WebParam(name = "sensorID") String sensorID, @WebParam(name = "timeStamp") String timeStamp, @WebParam(name = "type") String type, @WebParam(name = "temperature") String temperature, @WebParam(name = "signature") String signature) {

        //Checks to see if the signature is valid
        boolean validSig = validateInput(sensorID, timeStamp, type, temperature, signature);

        //If signature is valid, it adds the recorded temperature to the LinkedList
        if (validSig) {

            String[] tempInfo = {sensorID, timeStamp, type, temperature};//Put recorded info into String Array
            tempList.add(tempInfo); //Add record to LinkedList

            return "Added " + temperature + " " + type; //Send message to client that temperature was added
        }
        return "Invalid Signature."; //Send invalid signature message to client
    }

    /**
     * Retrieve all of the recorded temperatures from the LinkedList and sends
     * them to the client in a String
     *
     * Web service operation
     */
    @WebMethod(operationName = "getTemperatures")
    public String getTemperatures() {
        String temps = ""; //Empty String for temperatures to be added to 

        //Retrieve each temperatue and type from the LinkedList
        for (int i = 0; i < tempList.size(); i++) {

            String[] thisTemp = (String[]) tempList.get(i);
            temps = temps + thisTemp[3] + " " + thisTemp[2] + ", "; //Add the temperature and type to the String

        }
        return temps; //Return the list of temperatures and types to the client
    }

    /**
     * This method takes in a sensorID as a parameter (either 1 or 2) and return
     * the last recorded temperature for that sensor.
     *
     * Web service operation
     */
    @WebMethod(operationName = "getLastTemperature")
    public String getLastTemperature(@WebParam(name = "sensorID") String sensorID) {
        String lastTemp = "";//Creates an empty String for the 

        //Goes through the LinkedList to find the last added temperature for the given sensor
        for (int i = tempList.size() - 1; i >= 0; i--) {
            String[] thisTemp = (String[]) tempList.get(i);

            //If there is a temperature found for that sensor, then it is returned to the client
            if (thisTemp[0].equals(sensorID)) {
                lastTemp = lastTemp + thisTemp[3] + " " + thisTemp[2] + ", ";
                return "The last last temperature recorded for sensor " + sensorID + " was "
                        + lastTemp;
            }

        }
        //If no temperature is found for that sensor, the server returns an alternate message
        return "No temperatures found for this sensor.";

    }

    /**
     * This method validates the signature send by the client
     *
     * @param sensorID sensor ID, either 1 or 2
     * @param timeStamp time the report was sent
     * @param type type of temperature recorded
     * @param temperature measure of the temperature, a double encoded as a
     * String
     * @param signature signature sent from the client
     * @return true if the signature is valid, false if not
     */
    public boolean validateInput(String sensorID, String timeStamp, String type, String temperature, String signature) {
        //Concatonates the Strings of the record elements
        String toSign = sensorID + timeStamp + type + temperature;

        //Convert String to byte array
        byte[] sig;
        sig = toSign.getBytes();

        try {
            //Create a MessageDigest to handle hashing
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sig);

            //Convert the hashed value into byte array
            byte[] hashValue = md.digest();

            //Create a new array one index larger than the original
            byte[] sig2 = new byte[hashValue.length + 1];
            //Set the byte in the first index to 0
            sig2[0] = 0;
            //Move the elements from the old array into the new array, 
            //one index to the right of their original position
            for (int i = 1; i < sig2.length; i++) {
                sig2[i] = hashValue[i - 1];
            }

            //Convert the new array into a BigInteger
            BigInteger hashedBytes = new BigInteger(sig2);

            //Create a new BigInteger to hold the decrypted signature from the client
            BigInteger decryptedSig = null;
            //Convert the encrypted signature from the client to a BigIteger
            BigInteger encryptedSig = new BigInteger(signature);

            //RSA decrypt the signature using that sensor's public keys
            if (sensorID.equals("1")) {
                decryptedSig = encryptedSig.modPow(e1, n1);
            } else if (sensorID.equals("2")) {
                decryptedSig = encryptedSig.modPow(e2, n2);
            }

            //If the decrypted signature is equal to our new hash, the signature
            //is valid. Return true. 
            if (decryptedSig.equals(hashedBytes)) {
                return true;
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Exception caught: " + e);
        }
        //If the decrypted signature is not equal to the new hash, the signature
        //not valid. Return false.
        return false;
    }

}
