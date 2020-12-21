package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import dev.dazai.wol.databinding.ActionChooseIconDialogBinding;
import dev.dazai.wol.databinding.ActionGroupChooseDialogBinding;
import dev.dazai.wol.databinding.ActionGroupDialogBinding;
import dev.dazai.wol.databinding.ActionPortDialogBinding;
import dev.dazai.wol.databinding.ActionRouterIpDialogBinding;
import dev.dazai.wol.databinding.ActivityDevicePanelBinding;
import dev.dazai.wol.databinding.ChooseGroupItemBinding;
import dev.dazai.wol.databinding.DeviceQuestionDialogBinding;
import dev.dazai.wol.databinding.NoInternetConnectionDailogBinding;

public class DevicePanelActivity extends AppCompatActivity implements GroupChooseAdapter.OnGroupListener{
    ActivityDevicePanelBinding activityBinding;
    String deviceName, deviceIpAddress, deviceMacAddress, devicePort, deviceIcon, deviceGroupName, deviceSecureOn;
    int deviceGroup, deviceId;
    boolean deviceReachable = false, isThisNewDevice = false;
    BottomSheetDialog bottomSheetDialog;
    DeviceDatabase deviceDatabase;
    ActionPortDialogBinding actionPortDialogBinding;
    ActionChooseIconDialogBinding actionChooseIconDialogBinding;
    ActionRouterIpDialogBinding actionRouterIpDialogBinding;
    ActionGroupDialogBinding actionGroupDialogBinding;
    DeviceQuestionDialogBinding deviceUpdateDialogBinding;
    InputMethodManager inputMethodManager;
    IpAddressValidator validator;
    MagicPacket magicPacket;
    Device currentDevice;
    DevicePanelViewModel devicePanelViewModel;
    GroupChooseAdapter groupAdapter;
    ActionGroupChooseDialogBinding actionGroupChooseDialogBinding;
    private NetworkConnectionChecker networkConnectionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityDevicePanelBinding.inflate(getLayoutInflater());
        actionPortDialogBinding = ActionPortDialogBinding.inflate(getLayoutInflater());
        actionChooseIconDialogBinding = ActionChooseIconDialogBinding.inflate(getLayoutInflater());
        actionRouterIpDialogBinding = ActionRouterIpDialogBinding.inflate(getLayoutInflater());
        actionGroupDialogBinding = ActionGroupDialogBinding.inflate(getLayoutInflater());
        deviceUpdateDialogBinding = DeviceQuestionDialogBinding.inflate(getLayoutInflater());
        actionGroupChooseDialogBinding = ActionGroupChooseDialogBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        deviceDatabase = DeviceDatabase.getInstance(this);
        validator = new IpAddressValidator();
        magicPacket = new MagicPacket();
        deviceDatabase = DeviceDatabase.getInstance(getApplicationContext());
        bottomSheetDialog = new BottomSheetDialog(DevicePanelActivity.this, R.style.BottomSheetDialogTheme);

        NoInternetConnectionDailogBinding networkBinding = NoInternetConnectionDailogBinding.inflate(getLayoutInflater());
        networkConnectionChecker = new NetworkConnectionChecker(DevicePanelActivity.this, networkBinding);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.size() == 1){
                deviceId = extras.getInt("ID");
                devicePanelViewModel = ViewModelProviders.of(this).get(DevicePanelViewModel.class);
//                devicePanelViewModel = new ViewModelProvider(this).get(DevicePanelViewModel.class);

                devicePanelViewModel.getDeviceById(deviceId).observe(this, new Observer<Device>() {
                    @Override
                    public void onChanged(Device device) {
                        if(device != null){
                            currentDevice = device;
                            deviceName = currentDevice.getDeviceName();
                            deviceIpAddress = currentDevice.getDeviceIpAddress();
                            deviceMacAddress = currentDevice.getDeviceMacAddress();
                            devicePort = currentDevice.getDeviceLanPort();
                            deviceIcon = currentDevice.getDeviceIcon();
                            deviceGroup = currentDevice.getGroupId();
                            deviceSecureOn = currentDevice.getDeviceSecureOn();
                            deviceReachable = currentDevice.getReachable();

                            if(deviceGroup == 0){
                                deviceGroupName = null;
                                activityBinding.groupText.setText(deviceGroupName);
                                fillFields();
                            }else{
                                devicePanelViewModel.getGroupById(deviceGroup).observe(DevicePanelActivity.this, new Observer<Group>() {
                                    @Override
                                    public void onChanged(Group group) {
                                        deviceGroupName = group.getGroupName();
                                        fillFields();
                                    }
                                });

                            }


                        }

                    }
                });

            }else{
                if(!extras.getBoolean("MANUAL", false)){
                    deviceName = extras.getString("DEVICE_NAME");
                    deviceIpAddress = extras.getString("DEVICE_IP_ADDRESS");
                    deviceMacAddress = extras.getString("DEVICE_MAC_ADDRESS");

                    deviceReachable = true;
                    fillFields();
                }

                activityBinding.saveDeviceButton.setLayoutParams(new LinearLayout.LayoutParams(
                        (int)getApplicationContext().getResources().getDimension(R.dimen.deleteTextWidth),  LinearLayout.LayoutParams.MATCH_PARENT));
                activityBinding.deleteDeviceButton.setVisibility(View.GONE);

                isThisNewDevice = true;
                devicePanelViewModel = ViewModelProviders.of(this).get(DevicePanelViewModel.class);

            }
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
                bottomSheetDialog.setContentView(actionGroupDialogBinding.getRoot());
                bottomSheetDialog.show();

                if(deviceGroup == 0){
                    actionGroupDialogBinding.groupName.setText("Ustaw grupę");
                    actionGroupChooseDialogBinding.deleteDeviceFromGroup.setVisibility(View.GONE);

                }else{
                    actionGroupDialogBinding.groupName.setText(deviceGroupName);

                    actionGroupChooseDialogBinding.deleteDeviceFromGroup.setVisibility(View.VISIBLE);
                    actionGroupChooseDialogBinding.deleteDeviceFromGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deviceGroup = 0;
                            activityBinding.groupText.setText(null);
                            bottomSheetDialog.dismiss();
                        }
                    });
                }




                actionGroupDialogBinding.groupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.setContentView(actionGroupChooseDialogBinding.getRoot());
                        devicePanelViewModel.getAllGroups().observe(DevicePanelActivity.this, new Observer<List<Group>>() {
                            @Override
                            public void onChanged(List<Group> groups) {
                                groupAdapter = new GroupChooseAdapter(DevicePanelActivity.this, DevicePanelActivity.this);
                                actionGroupChooseDialogBinding.groupRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                actionGroupChooseDialogBinding.groupRecyclerView.setHasFixedSize(true);
                                actionGroupChooseDialogBinding.groupRecyclerView.setAdapter(groupAdapter);
                                groupAdapter.setGroups(groups);

                            }
                        });

                    }

                });

            }
        });



        activityBinding.turnOnDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceValid() && !deviceReachable && !isThisNewDevice)
                    new RunDeviceDialog(currentDevice).show(getSupportFragmentManager(), "RunDeviceDialog");

                else if(isThisNewDevice || isThereAnyChanges())
                    Toast.makeText(getApplicationContext(), "Przed uruchumieniem zapisz urządzenie!", Toast.LENGTH_SHORT).show();


            }
        });

        activityBinding.saveDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isThisNewDevice){
                    if(deviceValid()){
                        addDeviceToDatabase();
                        DevicePanelActivity.this.finish();
                    }
                }else{
                    if(deviceValid() && isThereAnyChanges())
                        updateDevice(false);
                }
            }
        });

        activityBinding.deleteDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDevice();

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

    private boolean secureOnValid(){
        String secureOn = activityBinding.secureOnTextInput.getText().toString().trim();
        String[] hex = secureOn.split("(\\:|\\-)");
        if(hex.length != 6 && !secureOn.isEmpty()){
            activityBinding.secureOnTextInputLayout.setError("SecureOn jest niepoprawny!");
            return false;
        }else{
            activityBinding.secureOnTextInput.setError(null);
            return true;
        }
    }

    private boolean portValid(){
        if(activityBinding.portText.getText().toString().trim().isEmpty()){
            activityBinding.portText.setHintTextColor(getResources().getColor(R.color.colorRed));
            return false;
        }else{
            activityBinding.portText.setHintTextColor(getResources().getColor(R.color.colorPrimaryLight));
            return true;
        }


    }

    private boolean deviceValid(){
        return deviceNameValid() && ipAddressValid() && macAddressValid() && portValid() && secureOnValid();

    }

    private String getTextContent(View v){
        CardView cardView = (CardView)v;
        TextView text = (TextView)cardView.getChildAt(0);
        return text.getText().toString().trim();

    }

    private void addDeviceToDatabase(){
        currentDevice = new Device();
        currentDevice.setDeviceName(activityBinding.deviceNameTextInput.getText().toString().trim());
        currentDevice.setDeviceIpAddress(activityBinding.ipTextInput.getText().toString().trim());
        currentDevice.setDeviceMacAddress(activityBinding.macTextInput.getText().toString().trim());
        currentDevice.setDeviceLanPort(activityBinding.portText.getText().toString().trim());
        currentDevice.setDeviceIcon(activityBinding.iconShowField.getText().toString().trim());
        currentDevice.setGroupId(deviceGroup);
        currentDevice.setDeviceSecureOn(activityBinding.secureOnTextInput.getText().toString().trim());
        devicePanelViewModel.insert(currentDevice);

    }

    private void fillFields(){
        //clear hint
        activityBinding.ipTextInputLayout.setHint(null);
        activityBinding.macTextInputLayout.setHint(null);

        //init device info
        activityBinding.deviceNameTextInput.setText(deviceName);
        activityBinding.ipTextInput.setText(deviceIpAddress);
        activityBinding.macTextInput.setText(deviceMacAddress);
        activityBinding.portText.setText(devicePort);
        activityBinding.iconShowField.setText(deviceIcon);
        activityBinding.groupText.setText(deviceGroupName);
        activityBinding.secureOnTextInput.setText(deviceSecureOn);

    }

    private void deleteDevice(){
        bottomSheetDialog.setContentView(deviceUpdateDialogBinding.getRoot());
        bottomSheetDialog.show();
        deviceUpdateDialogBinding.headerText.setText("Czy na pewno chcesz usunąć urządzenie?");

        deviceUpdateDialogBinding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePanelViewModel.delete(currentDevice);
                bottomSheetDialog.dismiss();
                DevicePanelActivity.this.finish();

            }
        });

        deviceUpdateDialogBinding.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });
    }

    private boolean isThereAnyChanges(){
        boolean name = activityBinding.deviceNameTextInput.getText().toString().trim().equals(deviceName);
        boolean ipAddress = activityBinding.ipTextInput.getText().toString().trim().equals(deviceIpAddress);
        boolean macAddress = activityBinding.macTextInput.getText().toString().trim().equals(deviceMacAddress);
        boolean port = activityBinding.portText.getText().toString().trim().equals(devicePort);
        boolean icon = activityBinding.iconShowField.getText().toString().trim().equals(deviceIcon);
        boolean group = currentDevice.getGroupId() == deviceGroup;
        boolean secure = activityBinding.secureOnTextInput.getText().toString().trim().equals(deviceSecureOn);

        return !name || !ipAddress || !macAddress || !port || !icon || !group || !secure;

    }

    private void setNewData(){
        boolean name = activityBinding.deviceNameTextInput.getText().toString().trim().equals(deviceName);
        boolean ipAddress = activityBinding.ipTextInput.getText().toString().trim().equals(deviceIpAddress);
        boolean macAddress = activityBinding.macTextInput.getText().toString().trim().equals(deviceMacAddress);
        boolean port = activityBinding.portText.getText().toString().trim().equals(devicePort);
        boolean icon = activityBinding.iconShowField.getText().toString().trim().equals(deviceIcon);
        boolean group = currentDevice.getGroupId() == deviceGroup;
        boolean secure = activityBinding.secureOnTextInput.getText().toString().trim().equals(deviceSecureOn);

        if(!name){
            currentDevice.setDeviceName(activityBinding.deviceNameTextInput.getText().toString().trim());
        }
        if(!ipAddress){
            currentDevice.setDeviceIpAddress(activityBinding.ipTextInput.getText().toString().trim());
        }
        if(!macAddress){
            currentDevice.setDeviceMacAddress(activityBinding.macTextInput.getText().toString().trim());
        }
        if(!port){
            currentDevice.setDeviceLanPort(activityBinding.portText.getText().toString().trim());
        }
        if(!icon){
            currentDevice.setDeviceIcon(activityBinding.iconShowField.getText().toString().trim());
        }
        if(!group){
            currentDevice.setGroupId(deviceGroup);
        }
        if(!secure){
            currentDevice.setDeviceSecureOn(activityBinding.secureOnTextInput.getText().toString().trim());
        }
    }

    private void updateDevice(final boolean toExit){
        bottomSheetDialog.setContentView(deviceUpdateDialogBinding.getRoot());
        bottomSheetDialog.show();
        deviceUpdateDialogBinding.headerText.setText("Zapisać zmiany?");

        deviceUpdateDialogBinding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewData();
                devicePanelViewModel.update(currentDevice);
                bottomSheetDialog.dismiss();
                if(toExit)
                    DevicePanelActivity.this.finish();

            }
        });

        deviceUpdateDialogBinding.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if(toExit)
                    DevicePanelActivity.this.finish();
            }
        });

    }

    @Override
    public void onGroupClick(Group group) {
        deviceGroup = group.getGroupId();
        deviceGroupName = group.getGroupName();
        activityBinding.groupText.setText(deviceGroupName);
        bottomSheetDialog.dismiss();

    }

    @Override
    public void onBackPressed() {
        if(!isThisNewDevice){
            if(isThereAnyChanges())
                updateDevice(true);
            else
                super.onBackPressed();
        }else
            super.onBackPressed();


    }

    @Override
    protected void onDestroy() {
        networkConnectionChecker.unRegister();
        super.onDestroy();
    }

}