package com.iaoongin.wxgzh.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {

	/**
	 * 微信接入校验
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSigature(String signature,String timestamp,String nonce){
		String token = WXProperties.getInstance().getProperty("token");
		if(signature!=null && !"".equals(signature)
				&& timestamp!=null && !"".equals(timestamp)
				&& nonce!=null && !"".equals(nonce)
				&& token!=null && !"".equals(token)){
			String [] arr=new String[]{token,timestamp,nonce};
			Arrays.sort(arr);
			StringBuilder content=new StringBuilder();
			for(int i=0;i<arr.length;i++){
				content.append(arr[i]);
			}
			
			MessageDigest md=null;
			String tmpStr="";
			try {
				md=MessageDigest.getInstance("SHA-1");
				byte[] digest =md.digest(content.toString().getBytes());
				tmpStr=byteToStr(digest);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			content=null;
			return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
		}
		return false;
	}


	private static String byteToStr(byte[] byteArray) {
		String strDigest="";
		for(int i=0;i<byteArray.length;i++){
			strDigest+=byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}


	private static String byteToHexStr(byte mByte) {
		char[] Diget={'0','1','2','3','4','5','6','7','8','9','A',
				'B','C','D','E','F'}; 
		char[] tempArr=new char[2];
		tempArr[0]=Diget[(mByte >>> 4) & 0X0F]; //取出
		tempArr[1]=Diget[mByte & 0X0F];
		String s=new String (tempArr);
		return s;
	}
		
}
