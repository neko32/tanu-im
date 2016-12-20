package org.tanuneko.im.util;

/**
 * Created by neko32 on 2016/12/13.
 */
@SuppressWarnings("ALL")
public class ConfigValidator {

    private ConfigValidator() {
        throw new IllegalStateException("no use");
    }

    public static String validateField(String str, String key) {
        if(key.equals(Resource.RES_USER_NAME)) {
            return validateUserName(str);
        }
        else if(key.equals(Resource.RES_GROUP_NAME)) {
            return validateGroupName(str);
        }
        else {
            return "";
        }
    }

    public static String validateUserName(String str) {
        if(str.length() > 20) {
            return "User name should be less than 20";
        }

        return "";
    }

    public static String validateGroupName(String str) {
        if(str.length() > 20) {
            return "Group name should be less than 20";
        }

        return "";
    }
}
