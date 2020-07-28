package com.zcy.beamtest;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zcy.beamtest.nfc.LoyaltyCardReader;

/**
 * @author zcy
 */
public class ReceiveApduActivity extends AppCompatActivity implements LoyaltyCardReader.DataCallback {

    private static final String TAG = "ReceiveApduActivity";
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader mLoyaltyCardReader;
    private TextView tvData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_apdu);
        tvData = findViewById(R.id.tv_in_message);
        mLoyaltyCardReader = new LoyaltyCardReader(this);
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, mLoyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onDataReceived(String data) {
        runOnUiThread(() -> tvData.setText(data));
    }
}
