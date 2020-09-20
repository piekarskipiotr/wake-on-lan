package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import dev.dazai.wol.databinding.ActionChooseIconDialogBinding;
import dev.dazai.wol.databinding.ActionGroupDialogBinding;
import dev.dazai.wol.databinding.ActionPortDialogBinding;
import dev.dazai.wol.databinding.ActionRouterIpDialogBinding;
import dev.dazai.wol.databinding.ActivityDevicePanelBinding;

public class DevicePanelActivity extends AppCompatActivity {
    ActivityDevicePanelBinding activityBinding;
    String deviceName, deviceIpAddress, deviceMacAddress;
    BottomSheetDialog bottomSheetDialog;
    ActionPortDialogBinding actionPortDialogBinding;
    ActionChooseIconDialogBinding actionChooseIconDialogBinding;
    ActionRouterIpDialogBinding actionRouterIpDialogBinding;
    ActionGroupDialogBinding actionGroupDialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityDevicePanelBinding.inflate(getLayoutInflater());
        actionPortDialogBinding = ActionPortDialogBinding.inflate(getLayoutInflater());
        actionChooseIconDialogBinding = ActionChooseIconDialogBinding.inflate(getLayoutInflater());
        actionRouterIpDialogBinding = ActionRouterIpDialogBinding.inflate(getLayoutInflater());
        actionGroupDialogBinding = ActionGroupDialogBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());

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

        activityBinding.routerIpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.setContentView(actionRouterIpDialogBinding.getRoot());
                bottomSheetDialog.show();
            }
        });


        activityBinding.turnOnDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceValid();
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

    public String getTextContent(View v){
        CardView cardView = (CardView)v;
        TextView text = (TextView)cardView.getChildAt(0);
        return text.getText().toString().trim();

    }

}