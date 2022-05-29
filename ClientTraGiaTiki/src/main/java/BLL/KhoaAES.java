package BLL;

//import GUI.JFrameErrorMess;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
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
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            //Lỗi của getInstance
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("encrypt 1: "+e.getMessage());
            return "0;false";
        }
    }

    public String decrypt(String mess){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plaintext = cipher.doFinal(Base64.getDecoder().decode(mess));
            return new String(plaintext);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            //Lỗi của getInstance
            //JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            //jFrameErrorMess.messerror("decrypt 1: "+e.getMessage());
            return "0;false";
        }
    }
/*
    public static void main(String[] args) {
        KhoaAES khoaAES = new KhoaAES();
        String mahoa = khoaAES.encrypt("điện thoại samsung galaxy d12");
        String key = khoaAES.getStringKey();
        KhoaAES khoaAES1 = new KhoaAES();
        khoaAES1.setStringKey("AfF9u1ZAl9KxK1IlHULqlllUG0JcrmC3A1DtwBSJvz0=");
        String giaima = khoaAES1.decrypt("FFg2Q0Aemk8znovC+hpxcYK+6RfR9a+JQTO/oO+73YTUl+d44er9UfsK+WPQw5Xd");
        System.out.println("key: "+khoaAES.getKey()+"\nkey1: "+key+"\nkey2: "+khoaAES1.getKey()+"\nmahoa ="+mahoa+"\ngiaima ="+giaima);
    }
 */

}
