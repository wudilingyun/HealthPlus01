package com.vee.healthplus.ui.setting;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.util.LoadingDialogUtil;

public class SettingShare extends Activity implements OnItemClickListener,
		PlatformActionListener {

	private Context mContext;
	private String content,brief;
	private final int SUCCESS = 0;
	private final int CANNEL = 1;
	private final int ERROR = 2;
	private String imgPath = "";
	private String url = "";
	private String id = "";
	private String type = "";
	private LoadingDialogUtil loading;
	private Bitmap bitmap;
	private Button share_edit_cancel_btn;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			loading.hide();
			
			switch (msg.what) {

			case SUCCESS:
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.setting_share_success),
						Toast.LENGTH_SHORT).show();

				break;
			case CANNEL:
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.setting_share_cannel),
						Toast.LENGTH_SHORT).show();
				break;
			case ERROR:
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.setting_share_failed),
						Toast.LENGTH_SHORT).show();
				break;

			}
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.mContext = this;
		setContentView(R.layout.share_activity);
		content = getIntent().getStringExtra("content");
		imgPath = getIntent().getStringExtra("imgPath");
		url = getIntent().getStringExtra("url");
		id = getIntent().getStringExtra("id");
		brief = getIntent().getStringExtra("brief");
		// type = getIntent().getStringExtra("type");
		loading = new LoadingDialogUtil(mContext);
		ListView shareTo_lv = (ListView) findViewById(R.id.shareTo_lv);
		ShareActivityAdapter adapter = new ShareActivityAdapter(mContext);
		getWindow().setWindowAnimations(R.anim.dialog_enter_down2up);
		shareTo_lv.setAdapter(adapter);
		shareTo_lv.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loading.hide();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		loading.show(R.string.setting_share_wait);
		shareTo(arg2);

	}

	private void shareTo(int shareTo) {
		String s = "我在使用【云医生】应用,这个新闻很不错啊,你也来看一下吧###[" + content + "]#### 地址是："
				+ url;
		int type = Platform.SHARE_WEBPAGE;
		//if(imgPath!=null)
	if (imgPath.length() > 0 && url.length() > 0) {
			type = Platform.SHARE_WEBPAGE;
		} else {
			type = Platform.SHARE_TEXT;
		}

		switch (shareTo) {
		case 0:
			SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();

			sp.text = s;
			if (imgPath.length() > 0) {

				sp.imageUrl = imgPath;
			}
			Platform p = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			
			p.share(sp);
			p.setPlatformActionListener(this);
			
			break;
		case 1:
			TencentWeibo.ShareParams sp1 = new TencentWeibo.ShareParams();
			sp1.text = s + content ;
		
			/*if (imgPath.length() > 0) {
				sp1.imagePath = imgPath;
			}*/
			Platform p1 = ShareSDK.getPlatform(this, TencentWeibo.NAME);
			//p1.authorize();
			p1.setPlatformActionListener(this);
			p1.share(sp1);
			break;
		case 2:
			Wechat.ShareParams sp2 = new Wechat.ShareParams();
			sp2.title = content;
		//	sp2.setTitleUrl(url);
			sp2.text = brief;
			sp2.shareType = type;
			sp2.imageUrl = imgPath;
			sp2.url = url;
			
			Platform p2 = ShareSDK.getPlatform(this, Wechat.NAME);
			if (!p2.isValid()) {
				loading.hide();
				Toast.makeText(this,
						R.string.setting_wechat_client_inavailable,
						Toast.LENGTH_SHORT).show();
			} else {
				p2.setPlatformActionListener(this);
				p2.share(sp2);
			}
			System.out.println("platform2 valid " + p2.isValid());
			break;
		case 3:
			WechatMoments.ShareParams sp3 = new WechatMoments.ShareParams();
			sp3.title = content;
			sp3.text = brief;
			sp3.shareType = type;
			sp3.setTitleUrl(url);
			sp3.url = url;
			sp3.imageUrl = imgPath;
			Platform p3 = ShareSDK.getPlatform(this, WechatMoments.NAME);
			if (!p3.isValid()) {
				loading.hide();
				Toast.makeText(this,
						R.string.setting_wechat_client_inavailable,
						Toast.LENGTH_SHORT).show();
			} else {
				p3.setPlatformActionListener(this);
				p3.share(sp3);
			}

			break;
		}

	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(CANNEL);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(SUCCESS);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		System.out.println("arg0"+arg0+"arg1"+arg1+"arg2"+arg2);
		System.out.println(arg2.getStackTrace());
		arg2.printStackTrace();
		handler.sendEmptyMessage(ERROR);
	}
}
