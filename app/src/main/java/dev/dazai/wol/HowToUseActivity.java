package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import dev.dazai.wol.databinding.ActivityHowToUseBinding;

public class HowToUseActivity extends AppCompatActivity {
    ActivityHowToUseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHowToUseBinding.inflate(getLayoutInflater());
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