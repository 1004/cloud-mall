package com.xky.mall.common.utils;

import com.xky.mall.common.common.Constants;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/20 7:40 下午
 */
public class Md5Util {
    public static String md5(String target) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return Base64.encodeBase64String(messageDigest.digest((target + Constants.SALT).getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
