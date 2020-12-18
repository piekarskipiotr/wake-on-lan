package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import dev.dazai.wol.databinding.ActivityHowToConfigDeviceActivtyBinding;

public class HowToConfigDeviceActivty extends AppCompatActivity {
    private ActivityHowToConfigDeviceActivtyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHowToConfigDeviceActivtyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
