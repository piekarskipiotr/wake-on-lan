package dev.dazai.wol;

import java.util.List;

public interface NetworkScannerResults {
    void processFinish(List<String> networkResults);
}
