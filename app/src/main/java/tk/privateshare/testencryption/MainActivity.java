package tk.privateshare.testencryption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import tk.privateshare.utils.Encryptor;


public class MainActivity extends AppCompatActivity {
    EditText textfield;
    EditText passwordField;
    Button encryptButton;
    Button decryptButton;
    String text;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textfield=(EditText)findViewById(R.id.editText);
        passwordField=(EditText)findViewById(R.id.editText2);
        encryptButton=(Button)findViewById(R.id.button);
        decryptButton=(Button)findViewById(R.id.button2);
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=textfield.getText().toString();
                key=passwordField.getText().toString();
                SecureRandom random = new SecureRandom();
                byte iv[] = new byte[16];//generate random 16 byte IV AES is always 16bytes
                random.nextBytes(iv);
                String ivstring = Base64.encodeToString(iv,Base64.DEFAULT);
                random.nextBytes(iv);
                String saltstring = Base64.encodeToString(iv,Base64.DEFAULT);
                System.out.println(ivstring);
                System.out.println(saltstring);
                MessageDigest md = null;
                byte[] keybytes=null;
                try {
                    md = MessageDigest.getInstance("SHA-1");
                    keybytes = (key+saltstring).getBytes("UTF-8");
                    keybytes = md.digest(keybytes);
                    keybytes = Arrays.copyOf(keybytes, 16); // use only first 128 bit
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String cipher = Encryptor.encrypt(Base64.encodeToString(keybytes,Base64.DEFAULT),ivstring,text);

                Toast.makeText(MainActivity.this,cipher,Toast.LENGTH_LONG).show();


            }
        });

    }
}
