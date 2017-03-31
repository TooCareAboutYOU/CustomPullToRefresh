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
	
	/**生产环境配置*/
	public static final String APP_KEY 		= "148306786663202";//需要替换成自己的参数
	public static final String APP_SERRET 		= "c8b1a16e546f4fa791bb8e9db480dcaa";//需要替换成自己的参数
	public static final String GOODS_ID 		= "1001761";// 产品包id，需要替换成自己的参数
	public static final String GOODS_TYPE 		= "MIGU_PACKAGE_PROGRAM";// 产品包枚举，需要替换成自己的参数
	public static final String RESOURCE_ID 	= "623341266";// 节目id，需要替换成自己的参数
	
}
