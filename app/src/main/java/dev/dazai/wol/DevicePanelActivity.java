package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import dev.dazai.wol.databinding.ActionChooseIconDialogBinding;
import dev.dazai.wol.databinding.ActionInputDialogBinding;
import dev.dazai.wol.databinding.ActivityDevicePanelBinding;

public class DevicePanelActivity extends AppCompatActivity {
    ActivityDevicePanelBinding activityBinding;
    String deviceName, deviceIpAddress, deviceMacAddress;
    BottomSheetDialog bottomSheetDialog;
    ActionInputDialogBinding actionInputDialogBinding;
    ActionChooseIconDialogBinding actionChooseIconDialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityDevicePanelBinding.inflate(getLayoutInflater());
        actionInputDialogBinding = ActionInputDialogBinding.inflate(getLayoutInflater());
        actionChooseIconDialogBinding = ActionChooseIconDialogBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());

        bottomSheetDialog = new BottomSheetDialog(DevicePanelActivity.this, R.style.BottomSheetDialogTheme);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            activityBinding.deleteDeviceButton.setVisibility(View.GONE);
            activityBinding.saveDeviceButton.setVisibility(View.VISIBLE);
            deviceName = extras.getString("DEVICE_NAME");
            deviceIpAddress = extras.getString("DEVICE_IP_ADDRESS");
            deviceMacAddress = extras.getString("DEVICE_MAC_ADDRESS");

        }

        //init device info
        activityBinding.deviceNameText.setText(deviceName);
        activityBinding.ipTextInput.setText(deviceIpAddress);
        activityBinding.macTextInput.setText(deviceMacAddress);


        activityBinding.portContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        activityBinding.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionChooseIconDialogBinding.getRoot());
                bottomSheetDialog.show();

            }
        });


        activityBinding.groupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activityBinding.routerIpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionInputDialogBinding.getRoot());
                bottomSheetDialog.show();
                actionInputDialogBinding.titleText.setText("IP router'a");
            }
        });


        activityBinding.turnOnDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipAddressValid();
                macAddressValid();
            }
        });

        activityBinding.saveDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activityBinding.deleteDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activityBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private boolean ipAddressValid(){
        String ip = activityBinding.ipTextInput.getText().toString().trim();
        if(ip.isEmpty()){
            activityBinding.ipTextInput.setError("Pole nie może być puste!");
            return false;
        }else{
            activityBinding.ipTextInput.setError(null);
            return true;
        }
    }

    private boolean macAddressValid(){
        String ip = activityBinding.macTextInput.getText().toString().trim();
        if(ip.isEmpty()){
            activityBinding.macTextInput.setError("Pole nie może być puste!");
            return false;
        }else{
            activityBinding.macTextInput.setError(null);
            return true;
        }
    }
}