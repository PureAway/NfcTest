package com.zcy.beamtest;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;



/**
 * @author zcy
 */
public class BasicActivity extends AppCompatActivity {

    protected NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        super.onCreate(savedInstanceState);
    }

    protected boolean isNfcSupported() {
        return this.nfcAdapter != null;
    }
}
