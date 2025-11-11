package com.hospital.boot.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	
	public static int seqNum = 1;
	public static String fileRename(String originalFileName) {
		// 1.png -> 20250930091014_00001.png, 20250930091014_00002.png, 20250930091014_00003.png
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 20250930091014
		String date = sdf.format(new Date(System.currentTimeMillis()));
		String num = "_" + String.format("%05d", seqNum++);
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		return date+num+ext;
	}
}