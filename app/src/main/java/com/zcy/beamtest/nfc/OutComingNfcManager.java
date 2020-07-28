package com.zcy.beamtest.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.util.Log;

import java.nio.charset.Charset;

/**
 * @author zcy
 */
public class OutComingNfcManager implements NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {

    private static final String TAG = OutComingNfcManager.class.getSimpleName();
    private NfcCallback callback;

    public OutComingNfcManager(NfcCallback callback) {
        this.callback = callback;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String outString = callback.getOutComingMessage();
        Log.d(TAG, "待发送的文本：" + outString);
        byte[] outBytes = outString.getBytes();

        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "application/vnd.com.haier.uhome.beam".getBytes(Charset.forName("US-ASCII")),
                new byte[0], outBytes);
        return new NdefMessage(mimeRecord);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        callback.onNdefPushComplete();
    }


    public interface NfcCallback {

        String getOutComingMessage();

        void onNdefPushComplete();
    }
}
