package dev.ng5m.shop;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PurchaseGson {
    private final String secret = ":w#tK9--kJ.$kLUT?EJWvcSX;)%D8e%(B,RwZr4rz3WJ{M@=;a$*ZhAE$-59/%gbY:+}4!.BbEwn#=%7mZHA7i5uK?d3_agS_.qfy{T:=TrL3WE_uk,c@H@v-BVt[_.NT*jQN9%T[SX2.u,Z7X06Uv/8=[i?wD14{Mm?C;WmDXW}h}?Qr,K.SgVQ{Am&HbKXN2iQz#Z8&zr&t_-@wDQ#Y8fiDSM+n.jm}aJyx340Q;SBtX,7rZbq3XBMZ&DD+g$m";

    public int productID;
    public String username;
    public String nonce;
    public int[] signature;

    private byte[] generateHMAC(String nonce) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            return hmac.doFinal(nonce.getBytes(StandardCharsets.UTF_8));
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    private byte[] iToB() {
        byte[] bytes = new byte[signature.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) signature[i];
        }

        return bytes;
    }

    public boolean verify() {
        byte[] received = iToB();
        byte[] expected = generateHMAC(nonce);

        return Arrays.equals(received, expected);
    }

}
