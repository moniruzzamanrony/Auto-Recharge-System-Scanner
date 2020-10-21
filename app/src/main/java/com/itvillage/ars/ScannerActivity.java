package com.itvillage.ars;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.itvillage.AES;
import com.itvillage.ars.ars.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ScannerActivity extends AppCompatActivity {

    Button push;
    XammpConnector xammpConnector;
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;


    CodeScannerView codeScannerView;
    CodeScanner codeScanner;
    Dialog dialog;
    Button but;
    private String packageName, userName, shopName, mac, phoneNo, email, userId, shopAddress, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        xammpConnector = new XammpConnector();
        codeScannerView = findViewById(R.id.codeScannerView);
        dialog = new Dialog(ScannerActivity.this);
        openCamaraForScanner();

    }

    private void openCamaraForScanner() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            openQRCodeScneer();
        } else {
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera access is required to Scan The Barcode.",
                    Toast.LENGTH_LONG).show();
            // Request the permission
            ActivityCompat.requestPermissions(ScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            Toast.makeText(this,
                    "<b>Camera could not be opened.</b>\\nThis occurs when the camera is not available (for example it is already in use) or if the system has denied access (for example when camera access has been disabled).", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void openQRCodeScneer() {
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String[] results = result.getText().split(",");

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_dialog);
                        EditText sPrice = dialog.findViewById(R.id.price);

                        packageName = results[4];
                        userName = results[1];
                        shopName = results[6];
                        mac = results[2];
                        phoneNo = results[0];
                        email = results[5];
                        userId = results[3];
                        shopAddress = results[7];
                        price = sPrice.getText().toString();

                        TextView packageName = (TextView) dialog.findViewById(R.id.packageName);
                        packageName.setText(results[4]);
                        TextView name = (TextView) dialog.findViewById(R.id.name);
                        name.setText("User Name: " + results[1]);
                        TextView companyName = (TextView) dialog.findViewById(R.id.company_name);
                        companyName.setText("Shop Name: " + results[6]);
                        TextView macAddress = (TextView) dialog.findViewById(R.id.mac_address);
                        macAddress.setText("Mac Address: " + results[2]);
                        TextView phoneNo = (TextView) dialog.findViewById(R.id.phoneNo);
                        phoneNo.setText("Phone No: " + results[0]);
                        TextView email = (TextView) dialog.findViewById(R.id.email);
                        email.setText("Email: " + results[5]);

                        Button send = (Button) dialog.findViewById(R.id.send);
                        Button re_scan = (Button) dialog.findViewById(R.id.re_scan);
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Doregister gg = new Doregister();
                                gg.execute("");
                                dialog.dismiss();
                                sendMail();
                            }
                        });
                        re_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                codeScanner.startPreview();
                                dialog.dismiss();
                            }
                        });


                        dialog.show();

                    }
                });
            }
        });
//        codeScannerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                codeScanner.startPreview();
//                dialog.dismiss();
//            }
//        });
    }

    private void sendMail() {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "Activation Key@ Auto Recharge System"+ "&body=" + "Key: "+ getSerialKey() + "&to=" + email);
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send mail..."));
    }

    private String dateIncrement(Date date) {
        return Config.dateToSting(Config.addDays(date, getPackageDays(packageName)));
    }

    private int getPackageDays(String value) {
        int integer = Integer.valueOf(value.replaceAll("[^0-9]", ""));
        System.out.print(integer);
        return integer;
    }

    private String getSerialKey() {
        return AES.encrypt("elearners.live," + userId + ",\"elearners.live,\"", "itvillage428854");
    }

    public class Doregister extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            conn = xammpConnector.CONN();
            if (conn == null) {
                Toast.makeText(getApplicationContext(), "Server Not Found", Toast.LENGTH_LONG);
            }
            Log.e("354435",""+conn);
            String Sql = "INSERT INTO `user_info`(`user_id`, `phone_no`, `email`, `shop_name`, `mac_address`, `serial_key`, `active_date`, `expaied_date`, `package_name`, `price`, `client_name`, `initial_password`, `shop_address`, `package_validity`, `role`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                pst = conn.prepareStatement(Sql);
                pst.setString(1, userId);
                pst.setString(2, phoneNo);
                pst.setString(3, email);
                pst.setString(4, shopName);
                pst.setString(5, mac);
                pst.setString(6, getSerialKey());
                pst.setString(7, Config.getCurrentDate());
                pst.setString(8, dateIncrement(Config.stringToDateType(Config.getCurrentDate())));
                pst.setString(9, packageName);
                pst.setString(10, price);
                pst.setString(11, userName);
                pst.setString(12, phoneNo);
                pst.setString(13, shopAddress);
                pst.setString(14, String.valueOf(getPackageDays(packageName)));
                pst.setString(15, "user");
                pst.execute();


            } catch (Exception e) {
                Log.e("SQL",e.getMessage());
            }

            return "Successfully";
        }
    }
}
