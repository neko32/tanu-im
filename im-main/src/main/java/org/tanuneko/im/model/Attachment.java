package org.tanuneko.im.model;

import java.io.Serializable;

/**
 * Created by neko32 on 2016/12/08.
 */
@SuppressWarnings("ALL")
public class Attachment implements Serializable {

    private final String fileName;
    private final byte[] data;

    public Attachment(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public String toString() {
        return String.format("%s:%d", fileName, data.length);
    }
}
