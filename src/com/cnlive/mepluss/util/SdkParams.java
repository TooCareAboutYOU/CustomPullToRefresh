package com.cnlive.mepluss.util;

import java.util.List;

import cn.cmvideo.sdk.core.CmVideoCoreSdk;
import cn.cmvideo.sdk.pay.bean.SubscribeInfo;
import cn.cmvideo.sdk.pay.bean.sales.GoodsService;
import cn.cmvideo.sdk.pay.bean.sales.Payment;
import cn.cmvideo.sdk.user.auth.Oauth2AccessToken;

public class SdkParams {

	public static CmVideoCoreSdk sdk = null;
	public static SubscribeInfo subscribeInfo;
	public static Oauth2AccessToken mAccessToken;
	public static List<GoodsService> services;
	public static GoodsService currentService;
	public static  Payment currentPayment;
	
	/**������������*/
	public static final String APP_KEY 		= "148306786663202";//��Ҫ�滻���Լ��Ĳ���
	public static final String APP_SERRET 		= "c8b1a16e546f4fa791bb8e9db480dcaa";//��Ҫ�滻���Լ��Ĳ���
	public static final String GOODS_ID 		= "1001761";// ��Ʒ��id����Ҫ�滻���Լ��Ĳ���
	public static final String GOODS_TYPE 		= "MIGU_PACKAGE_PROGRAM";// ��Ʒ��ö�٣���Ҫ�滻���Լ��Ĳ���
	public static final String RESOURCE_ID 	= "623341266";// ��Ŀid����Ҫ�滻���Լ��Ĳ���
	
}
