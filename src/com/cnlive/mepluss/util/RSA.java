package com.cnlive.mepluss.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;
import cn.cmvideo.sdk.common.util.Base64;

public class RSA 
{

	private static final String ALGORITHM 	= "RSA";
	
	/**������������*/
//	private static String RSA_PRIVATE_KEY_PKCS8 	= "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMsG5/f0JoWERQ3f"
//			+ "\r" + "gI27N6OSdxq3xD9e3kZuFemA1lsuhOLp7S4afak9+ZR9XrQGpXt/fFx47gkouJg0" + "\r"
//		+ "xM/8dpwvqXP90eXYsTVC64rcQ81s9ZDbPMIYp9punnVd+etafMWiM19KR03nwVFa" + "\r"
//		+ "tAWzeuXlFXDFuNAxZPCComaQDHnlAgMBAAECgYB4fVrBjsYlyv/oYdI7SvaUPuIs" + "\r"
//		+ "mw7+xOCJT9/1Oh788jivKkE2cUZOYJd0Botjr+YZyukVQDeUe/RBcF+1R5FjxPmy" + "\r"
//		+ "GtGkk77V31WQb1DsLubp+lMVzvSz7wWO8lt/blC2D6y+IJCSx2E1LgMysE9y7Vg1" + "\r"
//		+ "4o5ENopojjPDS53jwQJBAO/lSNofEY2efky6FhsJtV34NY3flfGm1OwzpW/f7xW1" + "\r"
//		+ "dLwYXdJjCW+QMba3GxSnhaRLmQ40cRh3w+pRPsUS/G0CQQDYqARNY/shi2rzpKEZ" + "\r"
//		+ "NMCwunOU49oxcI3Zw3wrD50UxMJ9AxnrHIV/+M3sd2O7eDhtQxaHxTL0BhLABHqB" + "\r"
//		+ "mphZAkAuCpFa24+g4IRY8XYApnJaoKKWogzDHkPLOXZM7GuUfQj0eLC5Cizrbn88" + "\r"
//		+ "FBRseGJbz+6GHWZ/Ta0aMZGcfTJZAkA71ForzPpizw9f7QyX21uC8lpMuYdds8lY" + "\r"
//		+ "Y4V8lrk4LQXQY/TaWmv7ZQfozyd5c7+RXL9yCuEBRlQTNqll9P0xAkEAv8XRZAPi" + "\r"
//		+ "LQBaw6f7c10FvwXQln3HGDFKy/5A/B5OYVdMpOLeKO29hzkclHw+LuthdBqip8fB" + "\r"
//		+ "kUhvKzfmZiyYRg==";

	private static String RSA_PRIVATE_KEY_PKCS8 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAML4ZmVPLnwD8xVV"
			+ "\r" + "7cb7Q94daYEOUZzz1gl59qxvpiFV2vMFceITXIOerP1i5SW2i6zKkoSEDrHL1Vs0" + "\r"
			+ "RlXIE7E62eZncDKBm4qAERBWl691d7Qc+MAJDZ27lS3eyN1y1c9ujeHfStVDGKDK" + "\r"
			+ "8S0UtJfrVejXa0yGC+HtbP8RA+s3AgMBAAECgYBJHyYwCrFRW53YO6Dwxp2eeQNv" + "\r"
			+ "nnInXh/A8Bf+deUljXCPQlSCyGYjCuiwI13zdfIOfO7Uej6UmfDLwuA6Hc6cOlMd" + "\r"
			+ "wTyTkoMZAVnNpSbm9W+IqXhX/0V9dMTbpwLjfyQucWVQxdQb2uMPel6Xpgn9YW9D" + "\r"
			+ "sPWzyoVT0D5C1+BvsQJBAPOvOYSWP3wTDeMd9eR2lzqgD/mO0xjtmVYieBhVYWHU" + "\r"
			+ "NE2eiM60STfPEncJkj5i+YvNS8jg2C/eqywGAEGe//kCQQDM0uoVVNJbpqz06N1x" + "\r"
			+ "fP0QH6mgkKp7Ol73QqYnSa6vWC7dchnc2HrT1i5LF7FJRFMAwdrurRFArZHaXgQx" + "\r"
			+ "hnCvAkEA4upBUkCi30PTA7GrftIbouDX31hrJGRkC/xO/sHKy9FR3xGIbYsePxTZ" + "\r"
			+ "KfXEs8lwfQlvz5/oUDM2C+mOpYyM6QJAfCM4jEUkWTfji2u3CpleJu+Jty72pEj0" + "\r"
			+ "VZenbBhULyGbBcupsRfjGZCt7gUmBenLBKJ5Nj7ePfz0cQyaLgn1MQJAbMYEHlPP" + "\r"
			+ "rhu550GZJveSVKvRLcGEKOShqWF5R+Iy76foFYvmdiPeYILktlysEJ3sdh0OAP+p" + "\r"
			+ "oejvhs9IxdqnUQ==";
	
	public static String encrypt (String source) throws Exception 
	{
		byte[] data 		= source.getBytes ();
		byte[] keyBytes 	= Base64.decode (RSA_PRIVATE_KEY_PKCS8);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec 	= new PKCS8EncodedKeySpec (keyBytes);
		KeyFactory keyFactory 				= KeyFactory.getInstance (ALGORITHM);
		Key privateKey 						= keyFactory.generatePrivate (pkcs8KeySpec);

		/*
		 * 用私钥进行签�? RSA Cipher负责完成加密或解密工作，基于RSA
		 */
		Cipher cipher 	= Cipher.getInstance ("RSA/ECB/PKCS1Padding");
		// ENCRYPT_MODE表示为加密模�?
		cipher.init (Cipher.ENCRYPT_MODE, privateKey);
		// 加密
		byte[] rsaBytes 	= cipher.doFinal (data);
		// Base64编码
		return Base64.encode (rsaBytes);
	}
}
