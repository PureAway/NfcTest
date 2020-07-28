package com.zcy.beamtest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author zcy
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        startService(new Intent(this, CardService.class));
    }


    public void sendBeam(View view) {
        Intent intent = new Intent(this, SenderBeamActivity.class);
        startActivity(intent);
    }

    public void sendApdu(View view) {
        Intent intent = new Intent(this, SenderApduActivity.class);
        startActivity(intent);
    }

    public void receiveApdu(View view) {
        Intent intent = new Intent(this, ReceiveApduActivity.class);
        startActivity(intent);
    }


    private boolean iosNFCPresent(Context context) {
        if (context == null) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && pm
                .hasSystemFeature(PackageManager.FEATURE_NFC));
    }
}
