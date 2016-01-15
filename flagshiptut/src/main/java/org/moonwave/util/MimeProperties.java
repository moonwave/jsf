package org.moonwave.util;

public class MimeProperties extends BaseProperties {

    private static MimeProperties instance;

    public static MimeProperties getInstance() {
        if (instance == null) {
            instance = new MimeProperties();
        }
        return instance;
    }
}
