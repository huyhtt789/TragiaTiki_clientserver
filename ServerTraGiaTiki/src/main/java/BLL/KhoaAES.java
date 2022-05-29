package BLL;

//import GUI.JFrameErrorMess;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KhoaAES {
    private SecretKey key;
    private int key_size = 256;

    public KhoaAES(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(key_size);
            this.key = keyGenerator.generateKey();
        }
        catch (NoSuchAlgorithmException e) {
            // Lỗi tạo key AES
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("KhoaAES: Tạo khóa AES thất bại");
        }
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    //secretkey to string
    public String convertSecretKeyToString(SecretKey secretKey){
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    //string to secretkey
    public SecretKey convertStringToSecretKeyto(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    public String getStringKey(){
        return convertSecretKeyToString(getKey());
    }

    public void setStringKey(String key){
        setKey(convertStringToSecretKeyto(key));
    }

    public String encrypt(String mess){
        try{
            byte[] messInBytes = mess.getBytes();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(messInBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (NoSuchAlgorithmException e){
            //Lỗi của getInstance
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("encrypt 1: "+e.getMessage());
        }
        catch (NoSuchPaddingException e){
            //Lỗi của getInstance
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("encrypt 2: "+e.getMessage());
        }
        catch (InvalidKeyException e){
            //Lỗi của init
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("encrypt 3: "+e.getMessage());
        }
        catch (IllegalBlockSizeException e){
            //Lỗi của doFinal
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("encrypt 4: "+e.getMessage());
        }
        catch (BadPaddingException e){
            //Lỗi của doFinal
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("encrypt 5: "+e.getMessage());
        }
        return "";
    }

    public String decrypt(String mess){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plaintext = cipher.doFinal(Base64.getDecoder().decode(mess));
            return new String(plaintext);
        }
        catch (NoSuchAlgorithmException e){
            //Lỗi của getInstance
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("decrypt 1: "+e.getMessage());
        }
        catch (NoSuchPaddingException e){
            //Lỗi của getInstance
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("decrypt 2: "+e.getMessage());
        }
        catch (InvalidKeyException e){
            //Lỗi của init
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("decrypt 3: "+e.getMessage());
        }
        catch (IllegalBlockSizeException e){
            //Lỗi của doFinal
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("decrypt 4: "+e.getMessage());
        }
        catch (BadPaddingException e){
            //Lỗi của doFinal
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("decrypt 5: "+e.getMessage());
        }
        return "";
    }

    /*
    Mã hóa object
    public SealedObject encryptObject(SealedObject object) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            SealedObject sealedObject = new SealedObject(object, cipher);
            return sealedObject;
        } catch (NoSuchPaddingException e) {
            //e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            //e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Serializable decryptObject(SealedObject sealedObject,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
        return unsealObject;
    }
     */

    /*
    public static void main(String[] args) {
        KhoaAES khoaAES = new KhoaAES();
        String mahoa = khoaAES.encrypt("điện thoại samsung galaxy d12");
        String key = khoaAES.getStringKey();
        KhoaAES khoaAES1 = new KhoaAES();
        khoaAES1.setStringKey(key);
        String giaima = khoaAES1.decrypt(mahoa);
        System.out.println("key: "+khoaAES.getKey()+"\nkey1: "+khoaAES.getStringKey()+"\nkey2: "+khoaAES1.getKey()+"\nmahoa ="+mahoa+"\ngiaima ="+giaima);
    }
     */

}
