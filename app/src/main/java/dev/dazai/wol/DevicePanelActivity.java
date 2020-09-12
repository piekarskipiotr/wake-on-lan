package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import dev.dazai.wol.databinding.ActionChooseDialogBinding;
import dev.dazai.wol.databinding.ActionInputDialogBinding;
import dev.dazai.wol.databinding.ActivityDevicePanelBinding;
import dev.dazai.wol.databinding.ChooseDialogItemBinding;

public class DevicePanelActivity extends AppCompatActivity {
    ActivityDevicePanelBinding activityBinding;
    String deviceName, deviceIpAddress, deviceMacAddress;
    BottomSheetDialog bottomSheetDialog;
    ActionInputDialogBinding actionInputDialogBinding;
    ActionChooseDialogBinding actionChooseDialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityDevicePanelBinding.inflate(getLayoutInflater());
        actionInputDialogBinding = ActionInputDialogBinding.inflate(getLayoutInflater());
        actionChooseDialogBinding = ActionChooseDialogBinding.inflate(getLayoutInflater());
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
        activityBinding.ipText.setText(deviceIpAddress);
        activityBinding.macText.setText(deviceMacAddress);

        activityBinding.ipContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionInputDialogBinding.getRoot());
                bottomSheetDialog.show();

            }
        });

        activityBinding.macContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionInputDialogBinding.getRoot());
                bottomSheetDialog.show();

            }
        });

        activityBinding.portContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionChooseDialogBinding.getRoot());
                bottomSheetDialog.show();
//                ChooseDialogItemBinding chooseDialogItemBinding = ChooseDialogItemBinding.inflate(getLayoutInflater());
//                actionChooseDialogBinding.gridLayout.addView(chooseDialogItemBinding.itemContainer);

            }
        });

        activityBinding.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }
        });

        activityBinding.secureOnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionInputDialogBinding.getRoot());
                bottomSheetDialog.show();
            }
        });

        activityBinding.turnOnDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}