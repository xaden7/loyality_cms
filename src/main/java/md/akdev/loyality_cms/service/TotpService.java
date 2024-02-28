package md.akdev.loyality_cms.service;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class TotpService {


    public int generateTotp(String secret) throws InvalidKeyException, NoSuchAlgorithmException {

        int nrOfSeconds = 30;

        Base32 base32 = new Base32();

        byte[] key = base32.decode(secret);

        byte[] timeCounter = new byte[8];

        // Calculate time
        long time = System.currentTimeMillis() / 1000 / nrOfSeconds;

        // Determine time counter from time
        for (int i = timeCounter.length - 1; time > 0; i--) {
            timeCounter[i] = (byte) (time & 0xFF);
            time >>= 8;
        }

        // Encrypt the data with the key and return the HMAC SHA1 of it in hex
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);

        //HMAC result as 20-byte String
        byte[] hmacString = mac.doFinal(timeCounter);

        int lastByte = hmacString[hmacString.length - 1];

        // Take lower-order 4 bits
        int offset = lastByte & 0xf;

        int offsetValue = 0;

        for (int i = offset; i <= offset + 3; i++) {
            // Get bytes of next offset index
            int nextByte = hmacString[i] & 0xff;
            // Shift bytes to the left
            offsetValue = offsetValue << 8;
            // Add bytes of next offset index
            offsetValue = offsetValue | nextByte;
        }

        offsetValue = offsetValue & 0x7fffffff;
        // Take the last 6 digits as verification code
        return offsetValue % 1000000;

    }
}
