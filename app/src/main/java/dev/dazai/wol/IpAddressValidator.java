package dev.dazai.wol;

import java.util.regex.Pattern;

public class IpAddressValidator {
    private static final String zeroTo255
            = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

    private static final String IP_REGEXP
            = zeroTo255 + "\\." + zeroTo255 + "\\."
            + zeroTo255 + "\\." + zeroTo255;

    private static final Pattern IP_PATTERN
            = Pattern.compile(IP_REGEXP);

    // Return true when *address* is IP Address
    public boolean isValid(String address) {
        return IP_PATTERN.matcher(address).matches();
    }
}

//source code: https://farenda.com/java/java-regex-matching-ip-address/