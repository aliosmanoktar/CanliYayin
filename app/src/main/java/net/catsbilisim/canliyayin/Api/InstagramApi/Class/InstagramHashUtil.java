package net.catsbilisim.canliyayin.Api.InstagramApi.Class;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InstagramHashUtil {
    public static final String XLATE = "0123456789abcdef";
    public static String generateSignature(String payload) throws UnsupportedEncodingException {
        String parsedData = URLEncoder.encode(payload, "UTF-8");

        String signedBody = CalculateHash(InstagramConstants.API_KEY, payload);

        return "ig_sig_key_version=" + InstagramConstants.API_KEY_VERSION + "&signed_body=" + signedBody + '.'
                + parsedData;

    }
    public static String CalculateHash(String key, String message){
        //Reference http://en.wikipedia.org/wiki/Secure_Hash_Algorithm
        //SHA256 block size is 512 bits => 64 bytes.
        final int HashBlockSize = 64;

        byte[] keyBytes = new byte[0];
        try {
            keyBytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] opadKeySet = new byte[HashBlockSize];
        byte[] ipadKeySet = new byte[HashBlockSize];
        if (keyBytes.length > HashBlockSize) keyBytes = GetHash(keyBytes);
        if (keyBytes.length < HashBlockSize)
        {
            byte[] newKeyBytes = new byte[HashBlockSize];
            System.arraycopy(keyBytes,0,newKeyBytes,0,keyBytes.length);
            keyBytes = newKeyBytes;
        }
        for (int i = 0; i < keyBytes.length; i++)
        {
            opadKeySet[i] = (byte)(keyBytes[i] ^ 0x5C);
            ipadKeySet[i] = (byte)(keyBytes[i] ^ 0x36);
        }
        byte[] hash=null;
        try {
            hash= GetHash(ByteConcat(opadKeySet,
                    GetHash(ByteConcat(ipadKeySet, message.getBytes("UTF-8")))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return byteToString(hash);
    }
    private static byte[] GetHash(byte[] bytes) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest.digest(bytes);
    }
    private static byte[] ByteConcat(byte[] left, byte[] right){
        if (null == left) return right;

        if (null == right) return left;
        byte[] newBytes = new byte[left.length + right.length];
        System.arraycopy(left,0,newBytes,0,left.length);
        System.arraycopy(right,0,newBytes,left.length,right.length);
        return newBytes;
    }
    public static String CalculateMd5(String message){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes=md.digest(message.getBytes("UTF-8"));
            return byteToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String byteToString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
        {
            int intVal = bytes[i] & 0xff;
            if (intVal < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(intVal));
        }
        return sb.toString();
    }
    public static String generateDeviceId(String username, String password) {
        String seed = md5hex(username + password);
        String volatileSeed = "12345";

        return "android-" + md5hex(seed + volatileSeed).substring(0, 16);
    }
    public static String md5hex(String source) {
        return digest("MD5", source);
    }
    protected static String digest(String codec, String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance(codec);
            byte[] digestBytes = digest.digest(source.getBytes(Charset.forName("UTF-8")));
            return hexlate(digestBytes, digestBytes.length);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(codec + " codec not available");
        }
    }
    protected static String hexlate(byte[] bytes, int initialCount) {
        if (bytes == null) {
            return "";
        }

        int count = Math.min(initialCount, bytes.length);
        char[] chars = new char[count * 2];

        for (int i = 0; i < count; ++i) {
            int val = bytes[i];
            if (val < 0) {
                val += 256;
            }
            chars[(2 * i)] = XLATE.charAt(val / 16);
            chars[(2 * i + 1)] = XLATE.charAt(val % 16);
        }

        return new String(chars);
    }

}
