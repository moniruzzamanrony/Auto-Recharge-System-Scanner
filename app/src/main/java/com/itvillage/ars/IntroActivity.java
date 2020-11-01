package com.itvillage.ars;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.itvillage.ars.ars.R;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

public class IntroActivity extends AppCompatActivity {

    XammpConnector xammpConnector;
    Connection conn = null;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        logo.setAnimation(animation);

        addSSLCertificate();

        xammpConnector = new XammpConnector();
        Doregister gg = new Doregister();
        gg.execute("");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }, 3000);

    }

    private void addSSLCertificate() {
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }


    public class Doregister extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            List<String> tableCreateSqlQueryList = new ArrayList<>();
            // For user_info table
            tableCreateSqlQueryList.add("CREATE TABLE IF NOT EXISTS user_info (user_id varchar(255),phone_no varchar(255),email varchar(255),shop_name varchar(255),mac_address varchar(255),serial_key varchar(255),active_date varchar(255),expaied_date varchar(255),package_name varchar(255),price varchar(255),client_name varchar(255),initial_password varchar(255),shop_address varchar(255),package_validity varchar(255),role varchar(255));");

            try {

                conn = xammpConnector.CONN();

                for (String sql : tableCreateSqlQueryList) {
                    System.out.print(sql);
                    Statement stmt = conn.createStatement();
                    stmt.execute(sql);
                    stmt.close();
                }
                conn.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            return "Successfully";
        }
    }
}
