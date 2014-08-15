package com.vee.healthplus.ui.heahth_news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_beans.FeedComment;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.EmoticonsEditText;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.NewsComment;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class Health_ValueBook_commentList_activity extends BaseFragmentActivity
		implements android.view.View.OnClickListener {
	private PullToRefreshListView comment_listView;
	private Health_ValueBook_Comment_Adapter myAdapter;
	private ImageLoader imageLoader;
	private List<NewsComment> commentlist = new ArrayList<NewsComment>();
	private EditText editText;
	private String content;
	private Button submitButton;
	private String normal = "1", pull = "2", down = "3";
	private List<NewsComment> newsComment_new, newsComment_old;
	private List<NewsComment> newcomment_top = new ArrayList<NewsComment>();
	private List<NewsComment> newscomment = new ArrayList<NewsComment>();

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.health_valuebook_comment, null);

		setContainer(view);
		getHeaderView().setHeaderTitle("评论");
		setRightBtnVisible(View.GONE);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);

		init();
		new getCommentsAsync().execute(normal);
	}

	void init() {
		imageLoader = ImageLoader.getInstance(this);
		comment_listView = (PullToRefreshListView) findViewById(R.id.allcomment_listview);
		comment_listView.getRefreshableView().setDivider(null);
		comment_listView.getRefreshableView().setSelector(
				android.R.color.transparent);
		comment_listView.setPullToRefreshOverScrollEnabled(false);
		comment_listView.setMode(Mode.BOTH);
		comment_listView.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_load));
		comment_listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				getString(R.string.loading));
		comment_listView.getLoadingLayoutProxy(false, true).setReleaseLabel(
				getString(R.string.release_to_load));

		editText = (EditText) findViewById(R.id.setting_feedback_content);
		submitButton = (Button) findViewById(R.id.dispatch_comment);
		submitButton.setOnClickListener(this);
		myAdapter = new Health_ValueBook_Comment_Adapter(this, imageLoader);
		comment_listView.setAdapter(myAdapter);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				/* content = s; */
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		comment_listView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// Do work to refresh the list here.
						System.out.println("评论个数" + newscomment.size());
						/*if ((newscomment != null && newscomment.size() >0)
								|| (newcomment_top.size() > 0 && newcomment_top != null)){*/
							
						

							if (comment_listView.isHeaderShown()) {
								// 下
								String max_idString = null;
								if (newcomment_top.size() > 0
										&& newcomment_top != null) {
									max_idString = newcomment_top.get(0)
											.getCommentid() + "";
								} else if(commentlist.size()>0){
									max_idString = commentlist.get(0)
											.getCommentid() + "";
								}else{
									max_idString = 0+"";
								}

								new getCommentsAsync().execute(down,
										max_idString);
							} else if (comment_listView.isFooterShown()) {
								if(commentlist.size()>10){
									
								
								new getCommentsAsync().execute(pull,
										commentlist.get(commentlist.size() - 1)
												.getCommentid() + "");
								}else {
									comment_listView.onRefreshComplete();
								}
								// 上
							}
						/*}else {
							comment_listView.onRefreshComplete();
						}*/
					}
				});
	}

	private class getCommentsAsync extends
			AsyncTask<String, Void, List<NewsComment>> {
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<NewsComment> doInBackground(String... params) {
			try {
				String url = getIntent().getStringExtra("imgurl");
				System.out.println("评论url" + url);
				switch (Integer.parseInt(params[0])) {
				case 1:
					newscomment = SpringAndroidService.getInstance(
							getApplication())
							.getNewsCommentsByScope(url, 0, 10);
					System.out.println("newcomment" + newscomment.size());
					return newscomment;
				case 3:
					newsComment_old = SpringAndroidService.getInstance(
							getApplication())
							.getNewsCommentsByMaxCommentidScope(url,
									Integer.parseInt(params[1]));
					// newscomment.addAll(newsComment_old);
					return newsComment_old;
				case 2:
					newsComment_new = SpringAndroidService.getInstance(
							getApplication())
							.getNewsCommentsByMinCommentidScope(url,
									Integer.parseInt(params[1]), 10);
					// newscomment.addAll(newsComment_new);
					return newsComment_new;
				default:
					break;
				}

			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<NewsComment> data) {
			if (exception != null) {
				String message;

				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
			}
			if (data != null && data.size() > 0) {
				commentlist = data;
				myAdapter.listaddAdapter(commentlist);
				myAdapter.notifyDataSetChanged();
				comment_listView.onRefreshComplete();
			} else {
				// comment_listView.onRefreshComplete();
				Toast.makeText(getApplication(), "没有更多评论", Toast.LENGTH_SHORT)
						.show();
				comment_listView.onRefreshComplete();
			}
			// TODO 网络数据 adapter
			// myAdapter.notifyDataSetChanged();
		}
	}

	private class submitCommentsAsync extends
			AsyncTask<String, Void, GeneralResponse> {
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected GeneralResponse doInBackground(String... params) {
			try {
				String url = getIntent().getStringExtra("imgurl");
				System.out.println("评论对应的url");
				GeneralResponse grt = SpringAndroidService.getInstance(
						getApplication()).insertNewsComment(url, params[0]);
				return grt;
			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse data) {
			if (exception != null) {
				String message;

				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
				System.out.println("====" + message + "exception"
						+ exception.getMessage());
			}
			if (data != null) {
				if (data.getReturncode() == 200) {
					Toast.makeText(getApplicationContext(), "评论成功",
							Toast.LENGTH_SHORT).show();

					new freshCommentsAsync().execute();

				} else {
					String s = data.getReturncode() + data.getDescription();
					System.out.println("评论问题" + s);
					Toast.makeText(getApplicationContext(), "评论失败",
							Toast.LENGTH_SHORT).show();
				}
			}
			// TODO 网络数据 adapter
			// myAdapter.notifyDataSetChanged();
		}
	}

	// 最新评论
	private class freshCommentsAsync extends
			AsyncTask<String, Void, List<NewsComment>> {
		private Exception exception;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<NewsComment> doInBackground(String... params) {
			try {
				String url = getIntent().getStringExtra("imgurl");
				System.out.println("网址" + url);
				List<NewsComment> grt = SpringAndroidService.getInstance(
						getApplication()).getNewsCommentsByMaxCommentidScope(
						url, 1);
				return grt;
			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<NewsComment> data) {
			if (exception != null) {
				String message;

				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
			}
			if (data != null && data.size() > 0) {
				newcomment_top = data;
				myAdapter.listaddAllAdapter(data);
				myAdapter.notifyDataSetChanged();
			}
			// TODO 网络数据 adapter
			// myAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.dispatch_comment:
			content = editText.getText().toString().trim();
			// String strUTF8 = URLDecoder.decode(content, "UTF-8");
			if (TextUtils.isEmpty(content)) {
				showToast("请输入评论内容");
			} else {
				String reply = null;
				content = TextUtils.isEmpty(reply) ? content : reply + content;
				NewsComment nc = new NewsComment();
				// System.out.println("输入的评论内容是" + content);
				new submitCommentsAsync().execute(content.toString().trim());
			}
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(
					Health_ValueBook_commentList_activity.this
							.getCurrentFocus().getWindowToken(),

					InputMethodManager.HIDE_NOT_ALWAYS);
			editText.setText(null);
			break;
		default:
			break;
		}
	}

	public void showToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
}
