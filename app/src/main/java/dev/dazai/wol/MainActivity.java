package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkScannerResults {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkScanner networkScanner = new NetworkScanner(getApplicationContext());
        networkScanner.delegate = this;
        //first ip, last ip, timeout
        networkScanner.execute(0, 50, 200);


    }

    @Override
    public void processFinish(List<String> networkResults) {
        Log.i("Reachable devices", networkResults.toString());
    }


}


