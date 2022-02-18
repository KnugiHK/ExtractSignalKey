package com.icf.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import org.thoughtcrime.securesms.crypto.DatabaseSecret;
import org.thoughtcrime.securesms.crypto.KeyStoreHelper;

import java.security.SecureRandom;

public class FirstFragment extends Fragment {
    private static final String ENCRYPTED_SECRET = "";
    private static DatabaseSecret secret;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
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

    private static @NonNull DatabaseSecret createAndStoreDatabaseSecret() {
        SecureRandom random = new SecureRandom();
        byte[]       secret = new byte[32];
        random.nextBytes(secret);

        DatabaseSecret databaseSecret = new DatabaseSecret(secret);
        KeyStoreHelper.SealedData encryptedSecret = KeyStoreHelper.seal(databaseSecret.asBytes());

        return databaseSecret;
    }
}