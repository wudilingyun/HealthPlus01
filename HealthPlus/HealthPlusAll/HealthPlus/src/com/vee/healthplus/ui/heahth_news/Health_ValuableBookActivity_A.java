/*package com.vee.healthplus.ui.heahth_news;
//这里面是老版本的估计没用了
 * 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.google.gson.Gson;
import com.vee.healthplus.R;
import com.vee.healthplus.TaskCallBack.TaskCallback;
import com.vee.healthplus.TaskCallBack.TaskResult;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_http.ImageLoaderFromHttp;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.heahth_news_utils.XiaoMuZhuang;
import com.vee.healthplus.heahth_news_utils.XiaoMuZhuang.OnUpdatingListener;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Health_ValuableBookActivity_A extends FragmentActivity implements
		TaskCallback, OnClickListener {

	private ListView listView_news;
	private List<Doc> all_news;
	private Health_ValuableBook_NewsAdapter adapter;

	private List<View> dots;
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private List<String> tiList = new ArrayList<String>();
	private List<String> urlList = new ArrayList<String>();
	private TextView tv_title;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	private ViewPager viewPager;
	private List<ImageView> imageViews;
	private MyNewsPagerAdapter Myadapter;
	private ImageLoader imageLoader;
	private ScrollView scrollAll;
	private LinearLayout layoutIner;
	private XiaoMuZhuang xiaoMuZuang;
	private Handler refesHandler = new Handler();
	private ImageLoaderFromHttp iFromHttp;
	// 切换当前显示的图片
	private Handler handler;
	private String hasNet = "1", JsonCach = "2";
	private JsonCache jsonCache;
	private String url;
	private int page = 1;
	private boolean flag = true;
	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;

	public static Health_ValuableBookActivity_A newInstance() {
		return new Health_ValuableBookActivity_A();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_news_main, null);
		setContentView(view);
		url = Contact.HealthNES_URL;
		jsonCache = JsonCache.getInstance();
		getdata(view);
		gettitle();
		init(view);
		getViewPagerData(view);

		if (CheckNetWorkStatus.Status(this)) {
			flag = true;
			new GetNewsListTask().execute(url, hasNet);
			System.out.println("当前地址" + url);

		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			new GetNewsListTask().execute(url, JsonCach);
		}
		iFromHttp = new ImageLoaderFromHttp(this, Myadapter, viewPager);
		iFromHttp.GetNews(this);
	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	void init(View view) {

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
			};
		};
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (LinearLayout) view.findViewById(R.id.loading_frame);
		loadImageView = (ImageView) view.findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		all_news = new ArrayList<Doc>();
		imageLoader = ImageLoader.getInstance(this);
		adapter = new Health_ValuableBook_NewsAdapter(this, imageLoader);
		listView_news = (ListView) view.findViewById(R.id.listView_news);
		listView_news.setAdapter(adapter);
		listView_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				ImageView img = (ImageView) view
						.findViewById(R.id.imageView_healthnews);
				img.setDrawingCacheEnabled(true);

				if (img.getTag() != null) {
					String imgurlString = (String) img.getTag();
					// 跳转后显示内容
					Intent intent = new Intent(
							Health_ValuableBookActivity_A.this,
							Health_news_detailsActivity.class);
					intent.putExtra("imgurl", imgurlString);
					Bundle bundle = new Bundle();
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}

			}
		});
	}

	public static void scrollToBottom(final View scroll, final View inner) {
		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}

				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}
				scroll.scrollTo(0, 0);

			}
		});
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度

		listView.setLayoutParams(params);
	}

	void getdata(View view) {
		scrollAll = (ScrollView) view.findViewById(R.id.scroll_all);
		scrollAll.requestDisallowInterceptTouchEvent(false);
		layoutIner = (LinearLayout) view.findViewById(R.id.layout_inner);
		scrollAll.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();
				if (action == MotionEvent.ACTION_UP) {
					return false;
				} else {
					return true;
				}
			}
		});
		scrollToBottom(scrollAll, layoutIner);
		xiaoMuZuang = new XiaoMuZhuang(scrollAll, layoutIner, this);
		xiaoMuZuang.setOnHeaderUpdatingListener(new OnUpdatingListener() {

			@Override
			public void onHeaderUpdating(XiaoMuZhuang puller) {
				// 线程
				refesHandler.postDelayed(new Runnable() {
					public void run() {
						// TODO 刷新时在主线程需要做的事情
						flag = false;
						new GetNewsListTask().execute(url, hasNet);
						// adapter.notifyDataSetChanged();
						xiaoMuZuang.setHeaderUpdatingComplete();
						// listView.onRefreshComplete();
					}
				}, 2000);

			}

			@Override
			public void onFooterUpdating(XiaoMuZhuang puller) {
				// 线程
				refesHandler.postDelayed(new Runnable() {
					public void run() {
						// TODO 刷新时在主线程需要做的事情
						// data.clear();
						page++;
						String rows = page * 20 + "";

						if (CheckNetWorkStatus.Status(getApplication())) {
							new GetNewsListTask().execute(
									Contact.HealthNES_URL_a + rows
											+ Contact.HealthNES_URL_b, hasNet);
							System.out.println("d======="
									+ Contact.HealthNES_URL_a + rows
									+ Contact.HealthNES_URL_b);
						} else {
							Toast.makeText(getApplication(), "错误",
									Toast.LENGTH_SHORT).show();
						}
						// adapter.notifyDataSetChanged();
						xiaoMuZuang.setFooterUpdatingComplete();
						// listView.onRefreshComplete();
					}
				}, 2000);

			}
		});

	}

	void getViewPagerData(View view) {

		// 点
		dots = new ArrayList<View>();
		dots.add(view.findViewById(R.id.v_dot0));
		dots.add(view.findViewById(R.id.v_dot1));
		dots.add(view.findViewById(R.id.v_dot2));

		// 文字

		tv_title = (TextView) view.findViewById(R.id.tv_title);
		// tv_title.setText(tiList.get(0));//

		viewPager = (ViewPager) view.findViewById(R.id.vp);
		Myadapter = new MyNewsPagerAdapter(this, imageLoader, tv_title);

		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	public void onResume() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 3,
				TimeUnit.SECONDS);
		super.onResume();
	}

	@Override
	public void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	*//**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 *//*
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % 3;
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	*//**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 *//*
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		*//**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 *//*
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(tiList.get(position));
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	private class GetNewsListTask extends AsyncTask<String, Void, String> {
		// private MultiValueMap<String, String> formData;
		private Exception exception;
		private HttpClient httpClient = new HttpClient();
		private StringBuffer stringBuffer = new StringBuffer();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (flag) {
				
				 * progressDiaog.showProgressDialog(getResources().getString(
				 * R.string.healthexam_download));
				 
				loadImageView.startAnimation(news_loadAaAnimation);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				switch (Integer.parseInt(params[1])) {
				case 1:

					Response response = httpClient.get(params[0]);
					InputStream isInputStream = response.asStream();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(isInputStream));

					String line = null;

					while ((line = reader.readLine()) != null) {
						stringBuffer.append(line);
					}
					jsonCache.saveJson(stringBuffer.toString(), params[0]);
					return stringBuffer.toString();
				case 2:
					String jsonCacheData = jsonCache.getJson(params[0]);
					return jsonCacheData;
				default:
					return null;

				}

			} catch (Exception e) {
				this.exception = e;
			}
			return null;

		}

		@Override
		protected void onPostExecute(String data) {
			if (exception != null) {
				String message;

				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "unauthorized,signout and signin again";

					SpringAndroidService.getInstance(getApplication())
							.signOut();

					// this.finish();
				}
				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "error";
				}
				
				 * progressDiaog.dismissProgressDialog();
				 * Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT)
				 * .show();
				 
			} else {

				if (data != null && data.length() > 0) {
					Gson gson = new Gson();
					Root root = gson.fromJson(data, Root.class);
					all_news = root.getResponse().getDocs();
					adapter.listaddAdapter(all_news);
					setListViewHeightBasedOnChildren(listView_news);
					adapter.notifyDataSetChanged();
					// progressDiaog.dismissProgressDialog();
					// tiList = iFromHttp.getTitle();
					loFrameLayout.setVisibility(View.GONE);
					loadImageView.clearAnimation();

				}
			}
		}
	}

	//
	@Override
	public void taskCallback(TaskResult taskResult) {
		tiList = taskResult.titles;
		tv_title.setText(tiList.get(0));
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:
			
			break;

		default:
			break;
		}
	}

}
*/