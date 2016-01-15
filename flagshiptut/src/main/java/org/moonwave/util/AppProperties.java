package org.moonwave.util;

public class AppProperties extends BaseProperties {

    public static String KEY_web_server_home    = "web_server_home";
    public static String KEY_outgoing_folder    = "outgoing_folder";
    public static String KEY_upload_folder      = "upload_folder";
    public static String KEY_normal_attachments = "normal_attachments";
    public static String KEY_default_timezone   = "default_timezone";
    public static String KEY_all_day_start      = "all_day_start";
    public static String KEY_all_day_end        = "all_day_end";

    private static AppProperties instance;

    public static AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties();
        }
        return instance;
    }

}