package com.iqos.argon2hashdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import de.wuthoehle.argon2jni.Argon2;
import de.wuthoehle.argon2jni.EncodedArgon2Result;
import de.wuthoehle.argon2jni.SecurityParameters;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private String calc() throws UnsupportedEncodingException {
        int version = Argon2.VersionIdentifiers.VERSION_13;
        SecurityParameters securityParameters = new SecurityParameters(1, 256 * 1024, 8);
        Argon2 argon = new Argon2(securityParameters, 32, 2, version);
        EncodedArgon2Result result = argon.argon2_hash("password".getBytes("US-ASCII"), "saltsaltsalt".getBytes("US-ASCII"));
        return bytesToHex(result.getResult());
    }

    private String bytesToHex(byte[] value) {
        StringBuilder wholeString = new StringBuilder();
        for (byte element : value) {
            wholeString.append(byteToHex(element));
        }
        return wholeString.toString();
    }

    private String byteToHex(byte value) {
        // String representation of all possible values of a nibble
        // alphabet[0] = 0, ..., alphabet[15] = f
        String alphabet = "0123456789abcdef";
        return String.valueOf(alphabet.charAt((value >> 4) & 0xF)) + alphabet.charAt(value & 0xF);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void onClick(View view) {
        try {
            String result = calc();
            ((TextView) findViewById(R.id.tv_result)).setText(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
