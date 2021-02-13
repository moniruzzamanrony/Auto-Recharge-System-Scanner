package com.itvillage.ars;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.itvillage.ars.services.ApiServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ScannerActivity extends AppCompatActivity {

    Button push;
    ServerConnector serverConnector;
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
        serverConnector = new ServerConnector();
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
            Toast.makeText(this, "Scan Your Requested Customer QR Code",
                    Toast.LENGTH_LONG).show();
            // Request the permission
            ActivityCompat.requestPermissions(ScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            Toast.makeText(this,
                    "Camera could not be opened.).", Toast.LENGTH_SHORT).show();

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
                        final EditText sPriceEditText = dialog.findViewById(R.id.price);

                        packageName = results[4];
                        userName = results[1];
                        shopName = results[6];
                        mac = results[2];
                        phoneNo = results[0];
                        email = results[5];
                        userId = results[3];
                        shopAddress = results[7];


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
                                price = sPriceEditText.getText().toString();
                                if (price.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Price Fill Empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    save();
                                    dialog.dismiss();
                                    sendMail();
                                }
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

    protected String save() {

        ApiServices apiServices = new ApiServices(this);
        Observable<String> identityResponseObservable = apiServices.addNewUser(
                userId,
                phoneNo,
                email,
                shopName,
                mac,
                getSerialKey(),
                Config.getCurrentDate(),
                dateIncrement(Config.stringToDateType(Config.getCurrentDate())),
                packageName,
                price,
                userName,
                phoneNo,
                shopAddress,
                String.valueOf(getPackageDays(packageName)),
                "user");

        identityResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {
                        System.out.println(res);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                });

//        String url = "http://167.99.76.96/arm/php/UserInfoInsert.php?user_id=" + userId + "&phone_no=" + phoneNo + "&email=" + email + "&shop_name=" + shopName + "&mac_address=" + mac + "&serial_key=" + getSerialKey() + "&active_date=" + Config.getCurrentDate() + "&expaied_date=" + dateIncrement(Config.stringToDateType(Config.getCurrentDate())) + "&package_name=" + packageName + "&price=" + price + "&client_name=" + userName + "&initial_password=" + phoneNo + "&shop_address=" + shopAddress + "&package_validity=" + String.valueOf(getPackageDays(packageName)) + "&role=user";
//        serverConnector.requestSend(url, ScannerActivity.this);

        return "Successfully";
    }

}
