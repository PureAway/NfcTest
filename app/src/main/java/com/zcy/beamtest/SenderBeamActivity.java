package com.zcy.beamtest;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zcy.beamtest.nfc.OutComingNfcManager;

/**
 * @author zcy
 */
public class SenderBeamActivity extends BasicActivity implements OutComingNfcManager.NfcCallback {

    private TextView tvOutComingMessage;
    private EditText etOutComingMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_beam);

        if (!isNfcSupported()) {
            Toast.makeText(this, "该设备不支持NFC", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            finish();
        }

        initViews();

        OutComingNfcManager outComingNfcCallback = new OutComingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outComingNfcCallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outComingNfcCallback, this);
    }

    private void initViews() {
        this.tvOutComingMessage = findViewById(R.id.tv_out_message);
        this.etOutComingMessage = findViewById(R.id.et_message);
        Button btnSetOutComingMessage = findViewById(R.id.btn_set_out_message);
        btnSetOutComingMessage.setOnClickListener((v) -> setOutGoingMessage());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }


    private void setOutGoingMessage() {
        String outMessage = this.etOutComingMessage.getText().toString();
        this.tvOutComingMessage.setText(outMessage);
    }

    @Override
    public String getOutComingMessage() {
        return this.tvOutComingMessage.getText().toString();
    }

    @Override
    public void onNdefPushComplete() {
        runOnUiThread(() ->
                Toast.makeText(SenderBeamActivity.this, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show());
    }
}
