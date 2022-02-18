package com.icf.test;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.thoughtcrime.securesms.crypto.KeyStoreHelper;
import org.thoughtcrime.securesms.crypto.DatabaseSecret;

import java.security.SecureRandom;

import static java.util.Base64.getEncoder;

public class MainActivity extends AppCompatActivity {

    private static final String ENCRYPTED_SECRET = "";
    private static DatabaseSecret secret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyStoreHelper.SealedData encryptedSecret = KeyStoreHelper.SealedData.fromString(ENCRYPTED_SECRET);
                String msg = "Creating KeyStore, replace the app KeyStore with Signal one";
                boolean success = true;
                try{
                    secret = new DatabaseSecret(KeyStoreHelper.unseal(encryptedSecret));
                }catch (NullPointerException e) {
                    success = false;
                    createAndStoreDatabaseSecret();
                }catch (AssertionError e){
                    success = false;
                    msg = "Replace the app KeyStore with Signal one first";
                }
                if (success){
                    msg = "Key decrypted: " + secret.encoded;
                    Log.d("key", msg);
                }
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static @NonNull DatabaseSecret createAndStoreDatabaseSecret() {
        SecureRandom random = new SecureRandom();
        byte[]       secret = new byte[32];
        random.nextBytes(secret);

        DatabaseSecret databaseSecret = new DatabaseSecret(secret);
        KeyStoreHelper.SealedData encryptedSecret = KeyStoreHelper.seal(databaseSecret.asBytes());

        return databaseSecret;
    }

}
