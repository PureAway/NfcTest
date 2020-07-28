package com.zcy.beamtest;

import java.io.IOException;
import java.nio.charset.Charset;

import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

/**
 * @author zcy
 */
public class WriteTag {

    private static final String TAG = WriteTag.class.getSimpleName();

    public void writeTagUltralight(Tag tag) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            // 从第五页开始写，因为从0-3前四页是存储系统数据的。
            ultralight.writePage(4, "中国".getBytes(Charset.forName("GB2312")));
            ultralight.writePage(5, "美国".getBytes(Charset.forName("GB2312")));
            ultralight.writePage(6, "英国".getBytes(Charset.forName("GB2312")));
            ultralight.writePage(7, "法国".getBytes(Charset.forName("GB2312")));

            System.out.println("成功写入MifareUltralight格式数据!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            try {
                ultralight.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }

    public void writeTagClassic(Tag tag) {

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();
            boolean auth = false;
            short sectorAddress = 1;
            auth = mfc.authenticateSectorWithKeyA(sectorAddress,
                    MifareClassic.KEY_NFC_FORUM);
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                mfc.writeBlock(4, "1313838438000000".getBytes());
                mfc.writeBlock(5, "1322676888000000".getBytes());
                mfc.close();
                System.out.println("成功写入");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public boolean writeNdefTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            //获取Ndef对象
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                //允许对标签进行IO操作
                ndef.connect();

                if (!ndef.isWritable()) {
                    Log.e(TAG, "NFC Tag是只读的！");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Log.e(TAG, "NFC Tag的空间不足！");
                    return false;
                }

                //向标签写入数据
                ndef.writeNdefMessage(message);
                Log.e(TAG, "已成功写入数据！");
                return true;

            } else {
                //获取可以格式化和向标签写入数据NdefFormatable对象
                NdefFormatable format = NdefFormatable.get(tag);
                //向非NDEF格式或未格式化的标签写入NDEF格式数据
                if (format != null) {
                    try {
                        //允许对标签进行IO操作
                        format.connect();
                        format.format(message);
                        Log.e(TAG, "已成功写入数据！");
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "写入NDEF格式数据失败！");
                        return false;
                    }
                } else {
                    Log.e(TAG, "NFC标签不支持NDEF格式！");
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
