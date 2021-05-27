package dev.dazai.wol.network;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.List;

import dev.dazai.wol.data.Device;
import dev.dazai.wol.utilities.RunDeviceDialog;
import dev.dazai.wol.viewmodels.RunDeviceDialogViewModel;

public class ReachableChecker extends AsyncTask<Void, Integer, Void> {
    private Device mDevice;
    private List<Device> mDevices;
    private static final int REACHABLE_INT = -1;
    private RunDeviceDialogViewModel runDeviceDialogViewModel;
    private WeakReference<RunDeviceDialog> weakReference;
    private boolean currentDeviceReachableStatus = false;

    public ReachableChecker(RunDeviceDialog activity, Device device, RunDeviceDialogViewModel runDeviceDialogViewModel) {
        mDevice = device;
        weakReference = new WeakReference<>(activity);
        this.runDeviceDialogViewModel = runDeviceDialogViewModel;
    }

    public ReachableChecker(RunDeviceDialog activity, List<Device> devices, RunDeviceDialogViewModel runDeviceDialogViewModel) {
        mDevices = devices;
        weakReference = new WeakReference<>(activity);
        this.runDeviceDialogViewModel = runDeviceDialogViewModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakReference.get().binding.doneButton.setClickable(true);
        weakReference.get().binding.headerText.setText("Sprawdzanie połączenia");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (mDevices != null) {
            int size = mDevices.size();
            for (int i = 0; i < size; i++) {
                for (int ii = 1; ii < 50; ii++) {
                    if (ii == 1)
                        publishProgress(ii);

                    if (ii % 10 == 0)
                        publishProgress(ii / 10 + 1);

                    try {
                        if (isCancelled()) {
                            break;
                        }

                        currentDeviceReachableStatus = InetAddress.getByName(mDevices.get(i).getDeviceIpAddress()).isReachable(1000);
                        if (currentDeviceReachableStatus) {
                            publishProgress(REACHABLE_INT);
                            if (size - 1 == i)
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            for (int ii = 1; ii < 50; ii++) {
                if (ii == 1)
                    publishProgress(ii);

                if (ii % 10 == 0)
                    publishProgress(ii / 10 + 1);

                try {
                    if (isCancelled()) {
                        break;
                    }

                    currentDeviceReachableStatus = InetAddress.getByName(mDevice.getDeviceIpAddress()).isReachable(1000);
                    if (currentDeviceReachableStatus) {
                        publishProgress(REACHABLE_INT);
                        break;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... integers) {
        super.onProgressUpdate(integers);
        if (integers[0] == -1) {
            mDevice.setReachable(currentDeviceReachableStatus);
            runDeviceDialogViewModel.update(mDevice);
            weakReference.get().adapter.setDevice(mDevice);
        } else
            weakReference.get().binding.headerText.setText("Sprawdzanie połączenia: próba " + integers[0] + "/5");

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int sec = (int) ((millisUntilFinished / 1200));
                        weakReference.get().binding.headerText.setText("Automatyczne zamknięcie okna za " + sec + "s");
                    }

                    public void onFinish() {
                        weakReference.get().binding.doneButton.setClickable(false);
                        weakReference.get().alertDialog.dismiss();

                    }
                }.start();
            }
        }, 4000);


    }
}
