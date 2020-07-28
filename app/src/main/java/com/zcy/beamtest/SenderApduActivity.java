package com.zcy.beamtest;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zcy.beamtest.nfc.AccountStorage;

/**
 * @author zcy
 */
public class SenderApduActivity extends BasicActivity {

    private EditText etOutComingMessage;
    private TextView tvOutComingMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_apdu);

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
    }

    private void initViews() {
        tvOutComingMessage = findViewById(R.id.tv_out_message);
        etOutComingMessage = findViewById(R.id.et_message);
        Button btnSetOutComingMessage = findViewById(R.id.btn_set_out_message);
        btnSetOutComingMessage.setOnClickListener((v) -> setOutGoingMessage());
    }

    private void setOutGoingMessage() {
        String outMessage = etOutComingMessage.getText().toString();
        tvOutComingMessage.setText(outMessage);
        AccountStorage.SetAccount(this, outMessage);
    }

}
