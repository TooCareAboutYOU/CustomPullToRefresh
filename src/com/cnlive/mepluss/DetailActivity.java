package com.cnlive.mepluss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.cmvideo.sdk.common.constants.ResourceType;
import cn.cmvideo.sdk.common.constants.ResultCode;
import cn.cmvideo.sdk.core.CmVideoCoreSdk;
import cn.cmvideo.sdk.core.handler.CmVideoCoreHandler;
import cn.cmvideo.sdk.core.service.CmVideoCoreService;
import com.cnlive.mepluss.util.RSA;
import com.cnlive.mepluss.util.SdkParams;

import cn.cmvideo.sdk.pay.bean.Goods;
import cn.cmvideo.sdk.pay.bean.Order;
import cn.cmvideo.sdk.pay.bean.SubscribeInfo;
import cn.cmvideo.sdk.pay.bean.sales.GoodsService;
import cn.cmvideo.sdk.pay.bean.sales.Payment;
import cn.cmvideo.sdk.pay.bean.sales.QueryPriceInfo;
import cn.cmvideo.sdk.pay.handler.QueryPriceHandler;
import cn.cmvideo.sdk.pay.handler.SubscribeHandler;
import cn.cmvideo.sdk.pay.handler.ValidateHandler;
import cn.cmvideo.sdk.program.bean.MediaPlaybillSearchInfo;
import cn.cmvideo.sdk.program.bean.OpenSearchInfo;
import cn.cmvideo.sdk.program.handler.MediaPlayBillSearchHandler;
import cn.cmvideo.sdk.program.handler.MediaProgramSearchHandler;
import cn.cmvideo.sdk.program.handler.OpenSearchHandler;
import cn.cmvideo.sdk.service.auth.bean.AuthInfo;
import cn.cmvideo.sdk.service.auth.bean.Codec;
import cn.cmvideo.sdk.service.auth.bean.PlayExtResponse;
import cn.cmvideo.sdk.service.auth.handler.ServiceAuthHandler;
import cn.cmvideo.sdk.user.auth.AuthHandler;
import cn.cmvideo.sdk.user.auth.Oauth2AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class DetailActivity extends Activity {

	private static final String PREFERENCES_DEMO = "meplus";

	private static final String TAG = "DetailActivity";
	
	private RadioGroup payTypeGroup;
	private TextView name;
	private TextView price;
	//验证码
	//private EditText captchaEdit;
	//private LinearLayout validateLayout;
	private VideoView videoView;
	private Button subscribeBtn;
	private Button mediaProgramSearch;
	private Button mediaPlaybillSearchBtn;
	private Button jniBtn;

	
	private int index 					= 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
	}
	
	
	private void initView() {
		// TODO Auto-generated method stub
		Log.i (TAG, "onCreate");

		videoView 		= (VideoView) findViewById (R.id.videoView);
		payTypeGroup 	= (RadioGroup) findViewById (R.id.payTypeGroup);

		name 				= (TextView) findViewById (R.id.nameValTxt);
		price 				= (TextView) findViewById (R.id.priceValTxt);
		//captchaEdit 		= (EditText) findViewById (R.id.captchaEdit);
		//validateLayout	 	= (LinearLayout) findViewById (R.id.validateLayout);

		subscribeBtn 			= (Button) findViewById (R.id.subscribeBtn);
		mediaProgramSearch 		= (Button) findViewById (R.id.mediaProgramSearchBtn);
		mediaPlaybillSearchBtn 	= (Button) findViewById (R.id.mediaPlaybillSearchBtn);
		jniBtn 	= (Button) findViewById (R.id.openSearchBtn);
		
		subscribeBtn.setOnClickListener (programOnClick);
		mediaProgramSearch.setOnClickListener (programOnClick);
		mediaPlaybillSearchBtn.setOnClickListener (programOnClick);
		jniBtn.setOnClickListener(programOnClick);

		if (SdkParams.sdk == null) 
		{
			CmVideoCoreService.init (this, coreHandler);
		}
	}
	
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult (requestCode, resultCode, data);

		if (SdkParams.sdk != null) 
		{
			SdkParams.sdk.onActivityCallBack (requestCode, resultCode, data);
		}
	}
	@Override
	protected void onStop () 
	{
		Log.i(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy () 
	{
		Log.i (TAG, "onDestroy");
		SdkParams.sdk.release(this);
		super.onDestroy();
	}
	
	protected void onStart () 
	{
		Log.i (TAG, "onStart");
		super.onStart();
	};

	protected void onRestart () 
	{
		Log.i (TAG, "onRestart");
		super.onRestart ();
	};

	
	
	OnClickListener programOnClick 	= new OnClickListener ()
	{
		public void onClick (View v) 
		{
			switch (v.getId ())
			{
				case R.id.subscribeBtn:
				{
					subscribe ();
				}break;
				
				case R.id.mediaPlaybillSearchBtn:
				{
					mediaPlaybillSearch ();
				}break;
				
				case R.id.mediaProgramSearchBtn:
				{
					mediaProgramSearch ();
				}break;
				case R.id.openSearchBtn:
				{
					openSrearch ();
				}break;
			}
		}
	};


	

	/**
	 * ����
	 * @param info
	 */
	private void queryPrice (QueryPriceInfo info) 
	{
		SdkParams.sdk.querySalesPrice (DetailActivity.this, info, new QueryPriceHandler () 
		{

			@Override
			public void onResult (String resultCode, String resultDesc, List<GoodsService> services) 
			{
				String str 	= String.format ("resultCode=%s, resultDesc=%s", resultCode, resultDesc);
				Log.d (TAG, str);

				if (ResultCode.ACCEPTED.name ().equals (resultCode)) 
				{
					SdkParams.services 	= services;
					if (services != null && services.size () > 0) 
					{
						int idx 	= 0;
						for (GoodsService service : services) 
						{
							for (Payment payment : service.getPayments ()) 
							{
								RadioButton radio 	= new RadioButton (DetailActivity.this);
								if (idx == 0) 
								{
									radio.setChecked (true);
									SdkParams.currentService 	= service;
									SdkParams.currentPayment 	= payment;
								}
								radio.setId (idx);
								radio.setText (payment.getName ());
								radio.setOnClickListener (radioBtnClick);
								payTypeGroup.addView (radio);
								idx ++;
							}
						}

						name.setText (services.get (0).getServiceInfo ().getName ());
						price.setText(String.format ( "%.2f", Double.parseDouble (services.get (0).getPayments ().get (0).getAmount ()+ "") / 100));
					}
				} 
				else 
				{
					Toast toast 	= Toast.makeText (DetailActivity.this, str, Toast.LENGTH_LONG);
					toast.show ();
				}

			}
		});
	}

	/**
	 * ����
	 */
	private void subscribe () 
	{
		if (!isLogin ())
		{
			return;
		}
			

		if (SdkParams.services == null || SdkParams.services.size () < 1) 
		{
			Toast toast 	= Toast.makeText (DetailActivity.this, "����ʧ�ܣ��޷�������", Toast.LENGTH_LONG);
			toast.show ();
			return;
		}
		if (SdkParams.currentService == null || SdkParams.currentPayment == null)
		{
			Toast toast 	= Toast.makeText (DetailActivity.this, "currentService��currentPayment����Ϊ��", Toast.LENGTH_LONG);
			toast.show ();
			return;
		}

		Goods goods = new Goods();
		goods.setId(SdkParams.GOODS_ID);
		goods.setType(SdkParams.GOODS_TYPE);
		goods.setResourceId(SdkParams.RESOURCE_ID);
		SdkParams.sdk.subscribe(DetailActivity.this, SdkParams.currentService, SdkParams.currentPayment, SdkParams.mAccessToken, goods, new SubscribeHandler() {

			@Override
			public void onResult(String resultCode, String resultDesc,
					String externalOrderId, Order order, String validId,
					String imageHexStream) {

				String str = String.format(
						"resultCode=%s, resultDesc=%s, validId=%s", resultCode,
						resultDesc, validId);
				Log.d(TAG, str);
				if (ResultCode.ACCEPTED.name().equals(resultCode)
						&& order != null) {
					Toast toast = Toast.makeText(DetailActivity.this, "����״̬="
							+ order.getStatus(), Toast.LENGTH_LONG);
					toast.show();

					// ��Ȩ
					serviceAuth();
				}else {
					Toast toast = Toast.makeText(DetailActivity.this, str,
							Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
	}
	
	
	boolean isSuccess 	= false;
	/**
	 * ��Ȩ
	 * @return
	 */
	private boolean serviceAuth () 
	{
		SdkParams.sdk.serviceAuth(DetailActivity.this, 
				SdkParams.mAccessToken.getUserId (), 
				SdkParams.mAccessToken.getToken (), 
				SdkParams.RESOURCE_ID, 
				ResourceType.POMS_PROGRAM_ID, 
				Codec.normal,
				new ServiceAuthHandler () 
		{
			@Override
			public void onResult (String resultCode, String resultDesc, AuthInfo authInfo, PlayExtResponse playExtResponse) 
			{
				isSuccess 		= ResultCode.SUCCESS.name ().equals(resultCode);
				String info 	= "";
				String url 		= "";
				if (isSuccess) 
				{
					url 	= playExtResponse.getPlayUrl ();
					info 	= String.format ("��Ȩ�ɹ������ŵ�ַΪ: %s ���ص�ַ��%s", url, playExtResponse.getDownloadUrl ());
				} 
				else 
				{
					if (playExtResponse != null) 
					{
						url 	= playExtResponse.getPlayUrl4Trial ();
					}
					info 	= String.format ("��Ȩʧ�ܣ��Բ���ַΪ: %s", url);
				}

				Toast toast  	= Toast.makeText(DetailActivity.this, info, Toast.LENGTH_LONG);
				toast.show ();

				if (!TextUtils.isEmpty (url)) 
				{
					Uri uri 	= Uri.parse(url);
					videoView.setVideoURI (uri);
					videoView.start ();
				}
			}
		});
		return isSuccess;
	}
	
	/**
	 * ��Ŀ����ѯ�ӿ�
	 */
	private void mediaPlaybillSearch ()
	{
		MediaPlaybillSearchInfo mMediaPlaybillSearchInfo = new MediaPlaybillSearchInfo ();
		mMediaPlaybillSearchInfo.setKey ("621399374");
//		mMediaPlaybillSearchInfo.setDate ("20151212");//����ѯ��Ŀ������ʱ���ʽ��YYYYMMDD ����ʱĬ��ȫ����Ŀ���б�
		SdkParams.sdk.mediaPlaybillSearch (DetailActivity.this, mMediaPlaybillSearchInfo, mediaPlayBillSearchHandler);
	}
	
	/**
	 * ý����Ϣ��ѯ�ӿ�
	 */
	private void mediaProgramSearch ()
	{
		String key = "621399374";
		SdkParams.sdk.mediaProgramSearch (DetailActivity.this, key, mediaProgramSearchHandler);
	}
	
	/**
	 * ��Ŀ�����ӿ�
	 */
	private void openSrearch ()
	{
		List<String> packId = new ArrayList<String>();
		packId.add("1002581");
		packId.add("1002821");
		
		OpenSearchInfo mOpenSearchInfo = new OpenSearchInfo ();
		mOpenSearchInfo.setKeyword("�ഺ");
		mOpenSearchInfo.setPackId(packId);
		
		SdkParams.sdk.openSearch(DetailActivity.this, mOpenSearchInfo, openSearchHandler);
	}
	
	
	
	CmVideoCoreHandler coreHandler 	= new CmVideoCoreHandler () 
	{

		public String encryptByRSA	(String source) 
		{
			String result 	= "";
			try 
			{
				result 	= RSA.encrypt (source);
			} 
			catch (Exception e) 
			{
				e.printStackTrace ();
			}
			return result;
		}

		public void initCallback (String resultCode, String resultDesc, CmVideoCoreSdk sdk) {
			SdkParams.sdk 	= sdk;
			if (ResultCode.SUCCESS.name ().equals (resultCode)) 
			{

				// Demoʾ�����룬�����߿ɲ����û���¼��Ϣ
				SharedPreferences pref 	= DetailActivity.this.getSharedPreferences (PREFERENCES_DEMO, Context.MODE_PRIVATE);
				String userid 			= pref.getString ("userid", "");
				String usernum 			= pref.getString ("usernum", "");
				String accesstoken 		= pref.getString ("accesstoken", "");
				SdkParams.mAccessToken 			= new Oauth2AccessToken ();
				SdkParams.mAccessToken.setUserId (userid);
				SdkParams.mAccessToken.setToken (accesstoken);
				SdkParams.mAccessToken.setUsernum (usernum);

				QueryPriceInfo info 	= new QueryPriceInfo ();
				info.setGoodsId (SdkParams.GOODS_ID);
				info.setGoodsType (SdkParams.GOODS_TYPE);

				Map<String, String> goodsProperties 	= new HashMap<String, String> ();
				goodsProperties.put ("resourceId", SdkParams.RESOURCE_ID);
				// ���ζ���goodsProperties���봫��resourceId
				info.setGoodsProperties (goodsProperties);

				// ����
				queryPrice (info);

				// ��Ȩ
				serviceAuth ();
			} 
			else 
			{
				Toast toast = Toast.makeText (DetailActivity.this, resultDesc, Toast.LENGTH_LONG);
				toast.show ();
			}
		}
	};
	
	class DemoAuthHandler implements AuthHandler 
	{
		@Override
		public void onComplete (Bundle values) 
		{
			// �� Bundle �н��� Token
			SdkParams.mAccessToken 	= Oauth2AccessToken.parseAccessToken (values);
			if (SdkParams.mAccessToken.isSessionValid ()) 
			{
				Log.i (TAG, "accessToken=" + SdkParams.mAccessToken.getToken ());

				// ���鿪���߱����û���¼��Ϣ����tokenʧЧ��ǰ�������¼
				SharedPreferences pref 	= DetailActivity.this.getSharedPreferences (PREFERENCES_DEMO, Context.MODE_PRIVATE);
				Editor editor 			= pref.edit ();
				editor.putString ("userid", SdkParams.mAccessToken.getUserId ());
				editor.putString ("usernum", SdkParams.mAccessToken.getUsernum ());
				editor.putString ("accesstoken", SdkParams.mAccessToken.getToken ());

				editor.commit ();

				boolean isSuccess 	= serviceAuth ();
				if (!isSuccess)
				{
					subscribe ();
				}
			} 
			else 
			{
				// ����ע���Ӧ�ó���ǩ������ȷʱ���ͻ��յ� Code����ȷ��ǩ����ȷ
				String code 	= values.getString ("code");
				Log.i (TAG, "code=" + code);
			}
		}

	}
	
	ValidateHandler validateHandler 	= new ValidateHandler () 
	{

		@Override
		public void onResult (String resultCode, String resultDesc, String externalOrderId, Order order) 
		{

			if (ResultCode.ACCEPTED.name ().equals (resultCode) && order != null) 
			{
				serviceAuth ();
			} 
			else 
			{
				String str 		= String.format ("resultCode=%s, resultDesc=%s", resultCode, resultDesc);
				Toast toast 	= Toast.makeText (DetailActivity.this, str, Toast.LENGTH_LONG);
				toast.show ();
			}

			//captchaEdit.setText ("");
			//validateLayout.setVisibility (LinearLayout.INVISIBLE);
		}

	};
	
	
	MediaPlayBillSearchHandler mediaPlayBillSearchHandler 	= new MediaPlayBillSearchHandler ()
	{

		@Override
		public void onResult (String resultCode, String resultDesc, String resultData) 
		{
			// TODO Auto-generated method stub
			if (ResultCode.SUCCESS.name ().equals (resultCode))
			{
				Toast.makeText (DetailActivity.this, resultData, Toast.LENGTH_LONG).show();
			}
			else 
			{
				String str 	= String.format ("resultCode=%s, resultDesc=%s", resultCode, resultDesc);
				Toast.makeText (DetailActivity.this, str, Toast.LENGTH_LONG).show();
			}
			
		}
		
	};
	
	MediaProgramSearchHandler mediaProgramSearchHandler 	= new MediaProgramSearchHandler ()
	{

		@Override
		public void onResult(String resultCode, String resultDesc, String resultData) 
		{
			// TODO Auto-generated method stub
			if (ResultCode.SUCCESS.name ().equals (resultCode))
			{
				Toast.makeText(DetailActivity.this, resultData, Toast.LENGTH_LONG).show();
			}
			else 
			{
				String str 	= String.format ("resultCode=%s, resultDesc=%s", resultCode, resultDesc);
				Toast.makeText (DetailActivity.this, str, Toast.LENGTH_LONG).show();
			}
		}
		
	};
	
	OpenSearchHandler openSearchHandler 	= new OpenSearchHandler ()
	{

		@Override
		public void onResult(String resultCode, String resultDesc, String resultData) {
			// TODO Auto-generated method stub
			if (ResultCode.SUCCESS.name ().equals (resultCode))
			{
				Toast.makeText(DetailActivity.this, resultData, Toast.LENGTH_LONG).show();
			}
			else 
			{
				Toast.makeText(DetailActivity.this, "openSearch error="+resultDesc, Toast.LENGTH_LONG).show();
			}
		}
		
	};

	
	private boolean isLogin () 
	{
		if (SdkParams.mAccessToken == null || !SdkParams.mAccessToken.isSessionValid ()) 
		{
			SdkParams.sdk.authorize (DetailActivity.this, SdkParams.APP_KEY, SdkParams.APP_SERRET, new DemoAuthHandler ());
			return false;
		}
		return true;
	}
	

	
	OnClickListener radioBtnClick 	= new OnClickListener () 
	{
		public void onClick (View v) 
		{
			onRadioButtonClicked (v);
		}
	};
	
	public void onRadioButtonClicked (View view) 
	{
		boolean isSet 		= false;
		boolean checked 	= ((RadioButton) view).isChecked ();
		if (checked) 
		{
			index 	= view.getId ();
			if (SdkParams.services != null && SdkParams.services.size () > 0) 
			{
				int idx 	= 0;
				for (GoodsService service : SdkParams.services) 
				{
					for (Payment payment : service.getPayments ()) 
					{
						if (idx == index) 
						{
							SdkParams.currentService 	= service;
							SdkParams.currentPayment 	= payment;

							name.setText (service.getServiceInfo ().getName ());
							price.setText (String.format ( "%.2f", Double.parseDouble (payment.getAmount () + "") / 100));
							isSet 	= true;
							break;
						} 
						else 
						{
							idx++;
						}
					}

					if (isSet) 
					{
						break;
					}
				}
			}
		}
	}
}
