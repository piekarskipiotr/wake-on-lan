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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import dev.dazai.wol.databinding.ActionChooseIconDialogBinding;
import dev.dazai.wol.databinding.ActionGroupDialogBinding;
import dev.dazai.wol.databinding.ActionPortDialogBinding;
import dev.dazai.wol.databinding.ActionRouterIpDialogBinding;
import dev.dazai.wol.databinding.ActivityDevicePanelBinding;

public class DevicePanelActivity extends AppCompatActivity {
    ActivityDevicePanelBinding activityBinding;
    String deviceName, deviceIpAddress, deviceMacAddress;
    BottomSheetDialog bottomSheetDialog;
    DeviceDatabase deviceDatabase;
    ActionPortDialogBinding actionPortDialogBinding;
    ActionChooseIconDialogBinding actionChooseIconDialogBinding;
    ActionRouterIpDialogBinding actionRouterIpDialogBinding;
    ActionGroupDialogBinding actionGroupDialogBinding;
    InputMethodManager inputMethodManager;

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

        bottomSheetDialog = new BottomSheetDialog(DevicePanelActivity.this, R.style.BottomSheetDialogTheme);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            activityBinding.deleteDeviceButton.setVisibility(View.GONE);
            activityBinding.saveDeviceButton.setVisibility(View.VISIBLE);
            activityBinding.turnOnDeviceButton.setVisibility(View.VISIBLE);

            deviceName = extras.getString("DEVICE_NAME");
            deviceIpAddress = extras.getString("DEVICE_IP_ADDRESS");
            deviceMacAddress = extras.getString("DEVICE_MAC_ADDRESS");
            activityBinding.ipTextInputLayout.setHint(null);
            activityBinding.macTextInputLayout.setHint(null);

            //init device info
            activityBinding.deviceNameTextInput.setText(deviceName);
            activityBinding.ipTextInput.setText(deviceIpAddress);
            activityBinding.macTextInput.setText(deviceMacAddress);
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
                addDeviceToDatabase();

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
        String ip = activityBinding.deviceNameTextInput.getText().toString().trim();
        if(ip.isEmpty()){
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
        Device device = new Device();
        device.setDeviceName(activityBinding.deviceNameTextInput.getText().toString().trim());
        device.setDeviceIpAddress(activityBinding.ipTextInput.getText().toString().trim());
        device.setDeviceMacAddress(activityBinding.macTextInput.getText().toString().trim());
        device.setDeviceLanPort(activityBinding.portText.getText().toString().trim());
        device.setDeviceIcon(activityBinding.iconShowField.getText().toString().trim());
        device.setDeviceGroup(activityBinding.groupText.getText().toString().trim());
        device.setDeviceSecureOn(activityBinding.secureOnTextInput.getText().toString().trim());

        deviceDatabase.deviceDao().insert(device);

    }


}