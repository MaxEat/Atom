package com.example.max.testjson;

/**
 * Created by max on 2018/4/11.
 */

public interface ScanResultReceiver {

    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}
