package com.galwaykart;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class TestData extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        String st_text="glaze@12";
        byte[] plaintext = st_text.getBytes();

        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            SecretKey key = keygen.generateKey();
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, key);

                    try {
                        byte[] ciphertext = cipher.doFinal(plaintext);

                        byte[] iv = cipher.getIV();

                            System.out.println(ciphertext.toString());
                            System.out.println(iv.toString());
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }


                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }
}
