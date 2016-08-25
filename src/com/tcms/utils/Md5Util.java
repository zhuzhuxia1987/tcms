package com.tcms.utils;

import java.security.MessageDigest;
/**
  * @ClassName: Md5Util
  * @Description: 调用md5算法进行加密
  * @author 朱恒章
  * @date 2016-5-17 上午11:26:06
  * @package_name 路径：com.tcms.utils
  */
public class Md5Util {

	public static String md5Diagest(String str) {

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(str.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < result.length; i++) {
				String res = Integer.toHexString(result[i] & 0xFF);
				if (res.length() == 1) {
					sb.append("0" + res); // 0~F
				} else {
					sb.append(res);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String md5Diagest(String str, int bit) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(str.getBytes("UTF-8"));
			for (int i = 0; i < result.length; i++) {
				String res = Integer.toHexString(result[i] & 0xFF);
				if (res.length() == 1) {
					sb.append("0" + res); // 0~F
				} else {
					sb.append(res);
				}
			}
			if (bit == 16) {
				return sb.toString().substring(8, 24);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
