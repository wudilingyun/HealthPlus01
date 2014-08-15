package com.vee.healthplus.ui.heahth_news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.vee.healthplus.R;
import com.vee.healthplus.TaskCallBack.TaskCallback;
import com.vee.healthplus.TaskCallBack.TaskResult;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_http.ImageLoaderFromHttp;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.http.StatisticsUtils;
import com.vee.healthplus.util.user.HP_User;
import com.yunfox.s4aservicetest.response.Answer;
import com.yunfox.s4aservicetest.response.YysNewsResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Health_ValuableBookActivity extends FragmentActivity implements
		TaskCallback, OnClickListener {

	private PullToRefreshListView listView_news;
	private List<YysNewsResponse> all_news;
	private Health_ValuableBook_NewsAdapter adapter;
	private ViewPager viewPager;
	private MyNewsPagerAdapter Myadapter;
	private ImageLoader imageLoader;
	private ImageLoaderFromHttp iFromHttp;
	// 切换当前显示的图片
	private String hasNet = "1", JsonCach = "2", pull = "3", down = "4";
	private JsonCache jsonCache;
	private String url;
	private boolean flag = true;
	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private List<YysNewsResponse> yysNewsResponseList, yysNewsResponseList_new,
			yysNewsResponseList_old;
	private String name;
	private int id;
	private int userId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_news_main, null);
		setContentView(view);
		userId = HP_User.getOnLineUserId(this);
		url = Contact.HealthNES_URL;
		jsonCache = JsonCache.getInstance();
		getData();
		gettitle();

		init(view);

		if (CheckNetWorkStatus.Status(this)) {
			flag = true;
			new GetNewsListTask().execute(hasNet);

		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			new GetNewsListTask().execute(JsonCach);
		}
	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText(name);
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	void getData() {
		name = getIntent().getStringExtra("name");
		id = getIntent().getIntExtra("id", 1);
	}

	void init(View view) {

		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (LinearLayout) view.findViewById(R.id.loading_frame);
		loadImageView = (ImageView) view.findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		all_news = new ArrayList<YysNewsResponse>();
		imageLoader = ImageLoader.getInstance(this);
		adapter = new Health_ValuableBook_NewsAdapter(this, imageLoader);
		listView_news = (PullToRefreshListView) view
				.findViewById(R.id.listView_news);
		listView_news.setAdapter(adapter);

		// listView_news.getRefreshableView().setDivider(null);
		listView_news.getRefreshableView().setSelector(
				android.R.color.transparent);
		listView_news.setMode(Mode.BOTH);
		listView_news.setPullToRefreshOverScrollEnabled(false);
		listView_news.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_load));
		listView_news.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				getString(R.string.loading));
		listView_news.getLoadingLayoutProxy(false, true).setReleaseLabel(
				getString(R.string.release_to_load));

		listView_news.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
				if (listView_news.isHeaderShown()) {
					System.out.println("最大" + all_news.get(0).getNewsid());

					new GetNewsListTask().execute(down, all_news.get(0)
							.getNewsid() + "");
					// 下拉刷新接口
				} else if (listView_news.isFooterShown()) {
					System.out.println("最小"
							+ all_news.get(all_news.size() - 1).getNewsid());
					new GetNewsListTask().execute(pull,
							all_news.get(all_news.size() - 1).getNewsid() + "");
					// 上拉刷新接口
				}
			}
		});

		
		/*  listView_news.setOnScrollListener(new OnScrollListener() {
		  
		  @Override 
		  public void onScrollStateChanged(AbsListView arg0, intscrollState) {
			  // TODO Auto-generated method stub switch
		  (scrollState) { case OnScrollListener.SCROLL_STATE_FLING:
		  imageLoader.unlock(); break; case OnScrollListener.SCROLL_STATE_IDLE:
		  imageLoader.unlock(); break; case
		  OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: imageLoader.unlock();
		  break; default: break; } }
		  
		  @Override public void onScroll(AbsListView arg0, int arg1, int arg2,
		  int arg3) { // TODO Auto-generated method stub
		  
		  } });*/
		
		listView_news.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					imageLoader.lock();
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					imageLoader.unlock();
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					imageLoader.lock();
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		listView_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				if (CheckNetWorkStatus.Status(getApplicationContext())) {
					ImageView img = (ImageView) view
							.findViewById(R.id.imageView_healthnews);
					TextView tvView = (TextView) view
							.findViewById(R.id.newstitle);
					TextView timeView = (TextView) view.findViewById(R.id.data);
					System.out.println("传过来web" + tvView.getTag().toString());
					String imgurlString = (String) img.getTag();
					// 跳转后显示内容
					Intent intent = new Intent(
							Health_ValuableBookActivity.this,
							Health_news_detailsActivity.class);
					intent.putExtra("title", tvView.getText().toString());
					intent.putExtra("weburl", tvView.getTag().toString());
					intent.putExtra("imgurl", imgurlString);
					intent.putExtra("name", name);
					intent.putExtra("brief", timeView.getTag().toString());
					Bundle bundle = new Bundle();
					intent.putExtra("bundle", bundle);
					startActivity(intent);

				} else {
					Toast.makeText(getApplicationContext(), "请检查网络连接",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	void getMinNewsId() {

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private class GetNewsListTask extends
			AsyncTask<String, Void, List<YysNewsResponse>> {
		// private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (flag) {
				loadImageView.startAnimation(news_loadAaAnimation);
			}
		}

		@Override
		protected List<YysNewsResponse> doInBackground(String... params) {
			try {
				switch (Integer.parseInt(params[0])) {
				case 1:
					System.out.println("访问网络");
					yysNewsResponseList = SpringAndroidService.getInstance(
							getApplication()).firstGetNewsList(id, 0, 40);
					System.out.println("id" + id);
					if (yysNewsResponseList.size() > 0
							&& yysNewsResponseList != null) {
						jsonCache.saveObject(yysNewsResponseList, "cluddoctor"
								+ id);
						System.out.println("缓存成功");
					}
					return yysNewsResponseList;

				case 2:
					yysNewsResponseList = jsonCache
							.getObject("cluddoctor" + id);

					return yysNewsResponseList;

				case 3:
					yysNewsResponseList_old = SpringAndroidService.getInstance(
							getApplication()).getMoreNewsList(id,
							Integer.parseInt(params[1]), 20);
					return yysNewsResponseList_old;
				case 4:

					yysNewsResponseList_new = SpringAndroidService.getInstance(
							getApplication()).getNewestNewsList(id,
							Integer.parseInt(params[1]));
					return yysNewsResponseList_new;

				}

			} catch (Exception e) {
				this.exception = e;
				System.out.println("message" + exception.getMessage()
						+ "toString" + exception.toString() + "getCause"
						+ exception.getCause() + "getLocalizedMessage"
						+ exception.getLocalizedMessage());

			}
			return null;

		}

		@Override
		protected void onPostExecute(List<YysNewsResponse> data) {
			if (exception != null) {
				String message;

				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "unauthorized,signout and signin again";

					SpringAndroidService.getInstance(getApplication())
							.signOut();

				}
				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "网络连接异常。请重试";
				}
				Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT)
						.show();
				loadImageView.clearAnimation();
				listView_news.onRefreshComplete();
			} else {
				if (data != null && data.size() > 0) {
					loFrameLayout.setVisibility(View.GONE);
					loadImageView.clearAnimation();
					all_news = data;
					adapter.listaddAdapter(all_news);
					adapter.notifyDataSetChanged();

					listView_news.onRefreshComplete();
				} else {

					Toast.makeText(getApplication(), "没有最新数据",
							Toast.LENGTH_SHORT).show();
					listView_news.onRefreshComplete();
				}
			}
		}
	}

	@Override
	public void taskCallback(TaskResult taskResult) {
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			onBackPressed();
			this.finish();
			break;
		case R.id.header_rbtn_img:

			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		switch (id) {
		case 1:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_JSJF_ID, StatisticsUtils.NEW_JSJF);
			break;
		case 2:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_JBYF_ID, StatisticsUtils.NEW_JBYF);
			break;
		case 3:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_YST_ID, StatisticsUtils.NEW_YST);
			break;
		case 4:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_QSYK_ID, StatisticsUtils.NEW_QSYK);
			break;
		case 5:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_YEBK_ID, StatisticsUtils.NEW_YEBK);
			break;
		case 6:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_JJJH_ID, StatisticsUtils.NEW_JJJH);
			break;
		case 7:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_LXHT_ID, StatisticsUtils.NEW_LXHT);
			break;
		case 8:
			StatisticsUtils.endFunction(this, userId + "",
					StatisticsUtils.NEW_ZXDB_ID, StatisticsUtils.NEW_ZXDB);
			break;
		}

	}

}
