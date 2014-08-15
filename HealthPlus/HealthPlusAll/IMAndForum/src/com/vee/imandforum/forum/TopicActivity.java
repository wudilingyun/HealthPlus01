package com.vee.imandforum.forum;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vee.imandforum.R;
import com.yunfox.s4aservicetest.response.ForumThread;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class TopicActivity extends Activity {
	private PullToRefreshListView threadlistview;
	private Button postbutton;
	private int fid;
	private Timestamp maxtimestamp;
	private Timestamp mintimestamp;
	private int rows;
	private ForumThreadAdapter forumThreadAdapter;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case 100: // 发帖成功
			/*
			 * threadlistview.getRefreshableView().setSelection(0); new
			 * GetForumThreadTask().execute();
			 */
			threadlistview.setRefreshing();
			new GetNewestForumThreadTask().execute();
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		Intent intent = getIntent();
		fid = intent.getIntExtra("fid", 0);
		maxtimestamp = new Timestamp(0);
		String timeStr = "2037-06-01 13:18:33.112233";
		mintimestamp = Timestamp.valueOf(timeStr);
		rows = 20;

		threadlistview = (PullToRefreshListView) findViewById(R.id.ThreadList);
		threadlistview.getRefreshableView().setDivider(null);
		threadlistview.getRefreshableView().setSelector(
				android.R.color.transparent);
		threadlistview.setMode(Mode.BOTH);
		threadlistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_load));
		threadlistview.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				getString(R.string.loading));
		threadlistview.getLoadingLayoutProxy(false, true).setReleaseLabel(
				getString(R.string.release_to_load));
		forumThreadAdapter = new ForumThreadAdapter(TopicActivity.this);
		threadlistview.setAdapter(forumThreadAdapter);

		threadlistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
				if (threadlistview.isHeaderShown()) {
					System.out.println("maxtimestamp:" + maxtimestamp.getTime());
					new GetNewestForumThreadTask().execute();
				} else if (threadlistview.isFooterShown()) {
					System.out.println("mintimestamp:" + mintimestamp.getTime());
					new LoadMoreForumThreadTask().execute();
				}
			}
		});

		threadlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				view.setSelected(true);
				ViewHolder v = (ViewHolder) view.getTag();
				Intent intent = new Intent(TopicActivity.this,
						TopicDetailActivity.class);
				intent.putExtra("pid", v.getPid());
				intent.putExtra("tid", v.getTid());
				intent.putExtra("fid", fid);
				startActivity(intent);
			}
		});
		postbutton = (Button) findViewById(R.id.Post);
		postbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TopicActivity.this,
						PostActivity.class);
				intent.putExtra("fid", fid);
				startActivityForResult(intent, 100);
			}
		});

		new FirstGetForumThreadTask().execute();
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class FirstGetForumThreadTask extends
			AsyncTask<Void, Void, List<ForumThread>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected List<ForumThread> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<ForumThread> forumthreadlist = SpringAndroidService
						.getInstance(getApplication()).getForumThreads(fid, 0,
								20);

				return forumthreadlist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<ForumThread> forumthreadlist) {
			if (exception != null) {

			}

			if (forumthreadlist != null) {
				// g_forumthreadlist = forumthreadlist;
				forumThreadAdapter.addbeforeforumthreadlist(forumthreadlist);
				forumThreadAdapter.notifyDataSetChanged();
			}
			threadlistview.setEmptyView(findViewById(R.id.empty));
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetNewestForumThreadTask extends
			AsyncTask<Void, Void, List<ForumThread>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected List<ForumThread> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<ForumThread> forumthreadlist = SpringAndroidService
						.getInstance(getApplication())
						.getForumThreadsByMaxLastpost(fid,
								maxtimestamp.getTime());

				return forumthreadlist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<ForumThread> forumthreadlist) {
			if (exception != null) {

			}

			if (forumthreadlist != null) {
				// g_forumthreadlist = forumthreadlist;
				forumThreadAdapter.addbeforeforumthreadlist(forumthreadlist);
				forumThreadAdapter.notifyDataSetChanged();
			}
			threadlistview.onRefreshComplete();
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class LoadMoreForumThreadTask extends
			AsyncTask<Void, Void, List<ForumThread>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected List<ForumThread> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<ForumThread> forumthreadlist = SpringAndroidService
						.getInstance(getApplication())
						.getForumThreadsByMinLastpost(fid,
								mintimestamp.getTime(), rows);

				return forumthreadlist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<ForumThread> forumthreadlist) {
			if (exception != null) {

			}

			else if (forumthreadlist == null || forumthreadlist.size() == 0) {
				Toast.makeText(TopicActivity.this, R.string.nomoreloaded,
						Toast.LENGTH_SHORT).show();
			}

			if (forumthreadlist != null) {
				// g_forumthreadlist = forumthreadlist;
				boolean hasnew = forumThreadAdapter
						.addafterforumthreadlist(forumthreadlist);
				forumThreadAdapter.notifyDataSetChanged();
				if (hasnew == false) {
					Toast.makeText(TopicActivity.this, R.string.nomoreloaded,
							Toast.LENGTH_SHORT).show();
				}
			}
			threadlistview.onRefreshComplete();
		}
	}

	private class ForumThreadAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		List<ForumThread> forumthreadlist;
		private Object lock = new Object();

		public boolean addafterforumthreadlist(
				List<ForumThread> newforumthreadlist) {
			synchronized (lock) {
				boolean hasnew = false;
				for (ForumThread forumThread : newforumthreadlist) {
					if (forumThread.getLastpost().getTime() > maxtimestamp
							.getTime()) {
						maxtimestamp = new Timestamp(forumThread.getLastpost()
								.getTime());
					}
					if (forumThread.getLastpost().getTime() < mintimestamp
							.getTime()) {
						mintimestamp = new Timestamp(forumThread.getLastpost()
								.getTime());
					}
					boolean bContains = false;
					for (ForumThread forumthreadold : forumthreadlist) {
						if (forumThread.getTid() == forumthreadold.getTid()) {
							bContains = true;
							break;
						}
					}
					if (!bContains) {
						forumthreadlist.add(forumThread);
						hasnew = true;
					}
				}
				return hasnew;
			}
		}

		public void addbeforeforumthreadlist(
				List<ForumThread> newforumthreadlist) {
			synchronized (lock) {
				for (ForumThread forumThread : newforumthreadlist) {
					if (forumThread.getLastpost().getTime() > maxtimestamp
							.getTime()) {
						maxtimestamp = new Timestamp(forumThread.getLastpost()
								.getTime());
					}
					if (forumThread.getLastpost().getTime() < mintimestamp
							.getTime()) {
						mintimestamp = new Timestamp(forumThread.getLastpost()
								.getTime());
					}
				}
				for (ForumThread forumThread : forumthreadlist) {
					boolean bContains = false;
					for (ForumThread forumthreadold : newforumthreadlist) {
						if (forumThread.getTid() == forumthreadold.getTid()) {
							bContains = true;
							break;
						}
					}
					if (!bContains) {
						newforumthreadlist.add(forumThread);
					}
				}
				forumthreadlist.clear();
				forumthreadlist.addAll(newforumthreadlist);
			}
		}

		public ForumThreadAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
			inflater = LayoutInflater.from(context);
			forumthreadlist = new ArrayList<ForumThread>();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return forumthreadlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			ViewHolder v = null;
			if (convertView != null) {
				view = convertView;
				v = (ViewHolder) view.getTag();
			} else {
				view = (View) inflater.inflate(R.layout.forumtopic_list_item,
						parent, false);
				v = new ViewHolder();
				view.setTag(v);
			}
			TextView subjectTextView = (TextView) view
					.findViewById(R.id.topic_title);
			subjectTextView.setText(forumthreadlist.get(position).getSubject());
			TextView authorTextView = (TextView) view
					.findViewById(R.id.topic_author);
			authorTextView.setText(forumthreadlist.get(position).getAuthor());
			TextView repliesTextView = (TextView) view
					.findViewById(R.id.topic_visits_replies);
			repliesTextView.setText("回复:"
					+ forumthreadlist.get(position).getReplies());
			TextView pubtimeTextView = (TextView) view
					.findViewById(R.id.topic_pubtime);
			SimpleDateFormat formattime = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date(forumthreadlist.get(position).getCreatetime()
					.getTime());
			String formatdate = formattime.format(date);
			pubtimeTextView.setText(formatdate);

			v.setPid(forumthreadlist.get(position).getPosttableid());
			v.setTid(forumthreadlist.get(position).getTid());

			return view;
		}
	}

	private class ViewHolder {
		private int pid;
		private int tid;

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public int getTid() {
			return tid;
		}

		public void setTid(int tid) {
			this.tid = tid;
		}
	}
}
