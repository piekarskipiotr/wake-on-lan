package dev.dazai.wol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import dev.dazai.wol.databinding.RunDeviceDialogBinding;

public class RunDeviceDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(RunDeviceDialogBinding.inflate(getLayoutInflater()).getRoot());
        return builder.create();
    }
}
