package dev.dazai.wol;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import dev.dazai.wol.databinding.HeaderBinding;

public class ReachableChecker extends AsyncTask<Void, Integer, Void> {
    private Device mDevice;
    private RunDeviceDialogViewModel runDeviceDialogViewModel;
    private WeakReference<RunDeviceDialog> weakReference;
    private boolean preDeviceReachableStatus = false;
    private boolean currentDeviceReachableStatus = false;

    public ReachableChecker(RunDeviceDialog activity, Device device, RunDeviceDialogViewModel runDeviceDialogViewModel){
        mDevice = device;
        weakReference = new WeakReference<>(activity);
        this.runDeviceDialogViewModel = runDeviceDialogViewModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakReference.get().binding.headerText.setText("Sprawdzanie połączenia");
    }


    @Override
    protected Void doInBackground(Void... voids) {


        if(mDevice.getReachable() != null){
            preDeviceReachableStatus = mDevice.getReachable();
        }
        for(int i = 1; i <= 10; i++){
            try {
                publishProgress(i);
                currentDeviceReachableStatus = InetAddress.getByName(mDevice.getDeviceIpAddress()).isReachable(1000);
                if(currentDeviceReachableStatus)
                    break;

            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... integers) {
        super.onProgressUpdate(integers);
        weakReference.get().binding.headerText.setText("Sprawdzanie połączenia: próba "+integers[0]+"/5");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(preDeviceReachableStatus != currentDeviceReachableStatus){
            mDevice.setReachable(currentDeviceReachableStatus);
            runDeviceDialogViewModel.update(mDevice);
            weakReference.get().adapter.setDevice(mDevice);
        }else{

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int sec = (int) ((millisUntilFinished / 1200));
                        weakReference.get().binding.headerText.setText("Automatyczne zamknięcie okna za "+ sec +"s");

                    }
                    public void onFinish() {
                        weakReference.get().alertDialog.dismiss();

                    }
                }.start();
            }
        },4000);


    }
}
