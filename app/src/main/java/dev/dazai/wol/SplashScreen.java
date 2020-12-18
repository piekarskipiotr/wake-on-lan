package dev.dazai.wol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import dev.dazai.wol.databinding.InternetConnectionMessageOnStartBinding;
import dev.dazai.wol.databinding.NoInternetConnectionDailogBinding;

public class SplashScreen extends AppCompatActivity {
    private static int DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                Intent i;

                if (networkInfo.isConnected()) {
                    i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    InternetConnectionMessageOnStartBinding binding = InternetConnectionMessageOnStartBinding.inflate(getLayoutInflater());
                    builder.setView(binding.getRoot());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    binding.reButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recreate();
                        }
                    });

                    binding.exitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }

            }
        }, DELAY);

    }
}