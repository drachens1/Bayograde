package dev.ng5m.shop;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.drachens.Manager.defaults.ContinentalManagers;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PurchaseGson {
    public String productID;
    public String username;
    public String randomBytes;
    public String signature;

    public PurchaseGson(String productID, String username, String randomBytes, String signature){
        this.productID=productID;
        this.username=username;
        this.randomBytes=randomBytes;
        this.signature=signature;
    }

    public static PurchaseGson create(String json){
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return new PurchaseGson(jsonObject.get("productID").getAsString(),
                jsonObject.get("username").getAsString(),
                jsonObject.get("nonce").getAsString(),
                jsonObject.get("signature").getAsString());
    }

    private byte[] generateHMAC(String randomBytes) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            final String secret = ContinentalManagers.configFileManager.getStoreSecretFile().getSecret();
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            return hmac.doFinal(randomBytes.getBytes(StandardCharsets.UTF_8));
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    public void updateId(){
        productID = ContinentalManagers.configFileManager.getStoreSecretFile().getIdMap().get(productID);
    }

    private byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    private byte[] iToB() {
        return hexStringToByteArray(signature);
    }

    public boolean verify() {
        byte[] received = iToB();
        byte[] expected = generateHMAC(randomBytes);
        return Arrays.equals(received, expected);
    }
}
