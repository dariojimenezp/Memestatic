package Server;

import javax.crypto.*;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    /** fields **/

    /* key used for encryption */
    SecretKey key;

    /* cipher for encryption */
    Cipher cipher;


    /** Constructor
     *
     * @param algorithm algorithm used to encrypt and decrypt data
     *
     **/
    Encryption(String algorithm){

        /* generate key and cipher */
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(256);
            key = keyGenerator.generateKey();
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            System.out.println(e);
        }
    }

    /** copy constructor
     *
     * @param key key used for encryption
     *
     */
    public Encryption(SecretKey key){
        this.key = key;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /** Encrypts a given string
     *
     * @param dataString string wanting to be encrypted
     *
     * @return encrypted object as a SealedObject
     */
    public SealedObject Encrypt(String dataString){

        /* convert string to serializable object */
        Serializable dataObject = new String(dataString);

        /* create encrypted object */
        try {
            return new SealedObject(dataObject, cipher);
        }
        catch (IllegalBlockSizeException e) { System.out.println(e); }
        catch (IOException e) {System.out.println(e); }

        return null;
    }

    /** Decrypts a given encrypted object
     *
     * @param encryptedData encrypted object
     *
     * @return string contained within encrypted object
     **/
    public String Decrypt(SealedObject encryptedData) {

        try { return (String) encryptedData.getObject(key); }

        catch (IOException | NoSuchAlgorithmException | InvalidKeyException | ClassNotFoundException e) { System.out.println(e); }

        return null;
    }

    /** getters */
    public SecretKey getKey() {
        return key;
    }

    public Cipher getCipher() {
        return cipher;
    }
}
