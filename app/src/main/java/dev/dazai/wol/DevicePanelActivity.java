package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import dev.dazai.wol.databinding.ActionChooseIconDialogBinding;
import dev.dazai.wol.databinding.ActionGroupDialogBinding;
import dev.dazai.wol.databinding.ActionPortDialogBinding;
import dev.dazai.wol.databinding.ActionRouterIpDialogBinding;
import dev.dazai.wol.databinding.ActivityDevicePanelBinding;

public class DevicePanelActivity extends AppCompatActivity {
    ActivityDevicePanelBinding activityBinding;
    String deviceName, deviceIpAddress, deviceMacAddress, devicePort, deviceIcon, deviceGroup, deviceSecureOn;
    Boolean deviceReachable = false;
    BottomSheetDialog bottomSheetDialog;
    DeviceDatabase deviceDatabase;
    ActionPortDialogBinding actionPortDialogBinding;
    ActionChooseIconDialogBinding actionChooseIconDialogBinding;
    ActionRouterIpDialogBinding actionRouterIpDialogBinding;
    ActionGroupDialogBinding actionGroupDialogBinding;
    InputMethodManager inputMethodManager;
    IpAddressValidator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityDevicePanelBinding.inflate(getLayoutInflater());
        actionPortDialogBinding = ActionPortDialogBinding.inflate(getLayoutInflater());
        actionChooseIconDialogBinding = ActionChooseIconDialogBinding.inflate(getLayoutInflater());
        actionRouterIpDialogBinding = ActionRouterIpDialogBinding.inflate(getLayoutInflater());
        actionGroupDialogBinding = ActionGroupDialogBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        deviceDatabase = DeviceDatabase.getInstance(this);
        validator = new IpAddressValidator();

        bottomSheetDialog = new BottomSheetDialog(DevicePanelActivity.this, R.style.BottomSheetDialogTheme);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.size() == 1){
                activityBinding.deleteDeviceButton.setVisibility(View.VISIBLE);
                activityBinding.saveDeviceButton.setVisibility(View.GONE);
                activityBinding.turnOnDeviceButton.setVisibility(View.VISIBLE);

                Device device = deviceDatabase.deviceDao().getById(extras.getInt("ID"));
                deviceName = device.getDeviceName();
                deviceIpAddress = device.getDeviceIpAddress();
                deviceMacAddress = device.getDeviceMacAddress();
                devicePort = device.getDeviceLanPort();
                deviceIcon = device.getDeviceIcon();
                deviceGroup = device.getDeviceGroup();
                deviceSecureOn = device.getDeviceSecureOn();

            }else{
                deviceName = extras.getString("DEVICE_NAME");
                deviceIpAddress = extras.getString("DEVICE_IP_ADDRESS");
                deviceMacAddress = extras.getString("DEVICE_MAC_ADDRESS");
                deviceReachable = true;

            }

            activityBinding.ipTextInputLayout.setHint(null);
            activityBinding.macTextInputLayout.setHint(null);

            //init device info
            activityBinding.deviceNameTextInput.setText(deviceName);
            activityBinding.ipTextInput.setText(deviceIpAddress);
            activityBinding.macTextInput.setText(deviceMacAddress);
            activityBinding.portText.setText(devicePort);
            activityBinding.iconShowField.setText(deviceIcon);
            activityBinding.groupText.setText(deviceGroup);
            activityBinding.secureOnTextInput.setText(deviceSecureOn);
        }

        activityBinding.ipContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityBinding.ipTextInput.requestFocus();
                inputMethodManager.showSoftInput(activityBinding.ipTextInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        activityBinding.macContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityBinding.macTextInput.requestFocus();
                inputMethodManager.showSoftInput(activityBinding.macTextInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        activityBinding.secureOnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityBinding.secureOnTextInput.requestFocus();
                inputMethodManager.showSoftInput(activityBinding.secureOnTextInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        activityBinding.portContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionPortDialogBinding.getRoot());
                bottomSheetDialog.show();
                actionPortDialogBinding.portSeven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityBinding.portText.setText(getTextContent(v));
                        bottomSheetDialog.dismiss();
                    }
                });

                actionPortDialogBinding.portNine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityBinding.portText.setText(getTextContent(v));
                        bottomSheetDialog.dismiss();
                    }
                });


            }
        });

        activityBinding.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionChooseIconDialogBinding.getRoot());
                bottomSheetDialog.show();
                GridLayout gridLayout = (GridLayout)actionChooseIconDialogBinding.getRoot().getChildAt(1);

                for(int i = 0; i <= gridLayout.getChildCount()-1; i++){
                    gridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityBinding.iconShowField.setText(getTextContent(v));
                            bottomSheetDialog.dismiss();

                        }
                    });
                }

            }
        });


        activityBinding.groupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityBinding.groupText.getText().toString().trim().isEmpty()){

                }else{
                    bottomSheetDialog.setContentView(actionGroupDialogBinding.getRoot());
                    bottomSheetDialog.show();
                }
            }
        });



        activityBinding.turnOnDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceValid()){

                }

            }
        });

        activityBinding.saveDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceValid()){
                    addDeviceToDatabase();
                }


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
    private boolean deviceNameValid(){
        String name = activityBinding.deviceNameTextInput.getText().toString().trim();
        if(name.isEmpty()){
            activityBinding.deviceNameTextInput.setError("Pole nie może być puste!");
            return false;
        }else{
            activityBinding.deviceNameTextInput.setError(null);
            return true;
        }
    }

    private boolean ipAddressValid(){
        String ip = activityBinding.ipTextInput.getText().toString().trim();
        if(ip.isEmpty()){
            activityBinding.ipTextInput.setError("Pole nie może być puste!");
            return false;
        }else if(!validator.isValid(ip)){
            activityBinding.ipTextInput.setError("Adres Ip jest niepoprawny!");
            return false;
        }else{
            activityBinding.ipTextInput.setError(null);
            return true;
        }
    }

    private boolean macAddressValid(){
        String mac = activityBinding.macTextInput.getText().toString().trim();
        String[] hex = mac.split("(\\:|\\-)");
        if(mac.isEmpty()){
            activityBinding.macTextInput.setError("Pole nie może być puste!");
            return false;
        }else if(hex.length != 6){
            activityBinding.macTextInput.setError("Adres Mac jest niepoprawny!");
            return false;
        }else{
            activityBinding.macTextInput.setError(null);
            return true;
        }
    }

    private boolean portValid(){
        if(activityBinding.portText.getText().toString().trim().isEmpty()){
            activityBinding.portText.setHintTextColor(Color.RED);
            return false;
        }else{
            activityBinding.portText.setHintTextColor(getResources().getColor(R.color.colorPrimaryLight));
            return true;
        }


    }

    private boolean deviceValid(){
        if(deviceNameValid() | ipAddressValid() | macAddressValid() | portValid())
            return true;
        else
            return false;

    }

    private String getTextContent(View v){
        CardView cardView = (CardView)v;
        TextView text = (TextView)cardView.getChildAt(0);
        return text.getText().toString().trim();

    }

    private void addDeviceToDatabase(){
        Device mDevice = new Device();
        mDevice.setDeviceName(activityBinding.deviceNameTextInput.getText().toString().trim());
        mDevice.setDeviceIpAddress(activityBinding.ipTextInput.getText().toString().trim());
        mDevice.setDeviceMacAddress(activityBinding.macTextInput.getText().toString().trim());
        mDevice.setDeviceLanPort(activityBinding.portText.getText().toString().trim());
        mDevice.setDeviceIcon(activityBinding.iconShowField.getText().toString().trim());
        mDevice.setDeviceGroup(activityBinding.groupText.getText().toString().trim());
        mDevice.setDeviceSecureOn(activityBinding.secureOnTextInput.getText().toString().trim());
        mDevice.setReachable(deviceReachable);

        deviceDatabase.deviceDao().insert(mDevice);
        Toast.makeText(getApplicationContext(), "Urządzenie zostało dodane!", Toast.LENGTH_SHORT).show();

    }


}