package com.zcy.beamtest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author zcy
 */
public class ReceiverBeamActivity extends BasicActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    private static final String TAG = ReceiverBeamActivity.class.getSimpleName();

    private TextView tvIncomingMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_beam);

        if (!isNfcSupported()) {
            Toast.makeText(this, "设备不支持nfc", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            finish();
            return;
        }
        initViews();
    }

    private void initViews() {
        this.tvIncomingMessage = findViewById(R.id.tv_in_message);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        receiveMessageFromDevice(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatch(this, this.nfcAdapter);
        receiveMessageFromDevice(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch(this, this.nfcAdapter);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            String inMessage = new String(ndefRecord_0.getPayload());
            Log.d(TAG, inMessage);
            this.tvIncomingMessage.setText(inMessage);
        }
    }


    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter) {

        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ex) {
            throw new RuntimeException("Check your MIME type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
