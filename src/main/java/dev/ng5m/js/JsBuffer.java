package dev.ng5m.js;

public class JsBuffer {
    public String type;
    public int[] data;

    public byte[] toByteArray() {
        byte[] byteArray = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            byteArray[i] = (byte) data[i];
        }

        return byteArray;
    }
}
