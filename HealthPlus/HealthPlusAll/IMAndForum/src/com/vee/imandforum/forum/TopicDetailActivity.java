package com.vee.imandforum.forum;

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
import com.yunfox.s4aservicetest.response.Post;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class TopicDetailActivity extends Activity {
	private int pid;
	private int tid;
	private int fid;
	private PullToRefreshListView topicdetaillistview;
	private Button replypostbutton; 
	private TextView topictitletextview;
	private int maxposition;
	private TopicDetailAdapter topicDetailAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detail);
		
		maxposition = 0;
		
		topicdetaillistview = (PullToRefreshListView)findViewById(R.id.TopicDetailList);
		topicdetaillistview.getRefreshableView().setDivider(null);
		topicdetaillistview.getRefreshableView().setSelector(
				android.R.color.transparent);
		topicdetaillistview.setMode(Mode.PULL_FROM_END);
		topicdetaillistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_load));
		topicdetaillistview.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				getString(R.string.loading));
		topicdetaillistview.getLoadingLayoutProxy(false, true).setReleaseLabel(
				getString(R.string.release_to_load));
		
		topictitletextview = (TextView)findViewById(R.id.topic_title);
		topicDetailAdapter = new TopicDetailAdapter(TopicDetailActivity.this);
		topicdetaillistview.setAdapter(topicDetailAdapter);
		
		topicdetaillistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
					new GetTopicDetailTask().execute();
			}
		});

		Intent intent = getIntent();
		pid = intent.getIntExtra("pid", 0);
		tid = intent.getIntExtra("tid", 0);
		fid = intent.getIntExtra("fid", 0);
		
		replypostbutton = (Button)findViewById(R.id.ReplyPost);
		replypostbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TopicDetailActivity.this,
						ReplyPostActivity.class);
				intent.putExtra("pid", pid);
				intent.putExtra("tid", tid);
				intent.putExtra("fid", fid);
				startActivityForResult(intent,200);
			}
		});
		
		new FirstGetTopicDetailTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode){
		case 200:
			topicdetaillistview.setRefreshing();
			new GetTopicDetailTask().execute();
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class FirstGetTopicDetailTask extends AsyncTask<Void, Void, List<Post>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected List<Post> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Post> topicdetaillist = SpringAndroidService.getInstance(
						getApplication()).getPostsByTid(tid, 0, 20);

				return topicdetaillist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Post> postlist) {
			if (exception != null) {

			}

			if (postlist != null) {
				topicDetailAdapter.addtopicdetaillist(postlist);
				topicDetailAdapter.notifyDataSetChanged();
				
				topictitletextview.setText(postlist.get(0).getSubject());
			}
			
			topicdetaillistview.setEmptyView(findViewById(R.id.empty));
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetTopicDetailTask extends AsyncTask<Void, Void, List<Post>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected List<Post> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Post> topicdetaillist = SpringAndroidService.getInstance(
						getApplication()).getPostsByMaxPosition(tid, maxposition, 20);

				return topicdetaillist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Post> postlist) {
			if (exception != null) {

			}

			if (postlist != null) {
				boolean hasnew = topicDetailAdapter.addtopicdetaillist(postlist);
				topicDetailAdapter.notifyDataSetChanged();
				
				if (hasnew == false) {
					Toast.makeText(TopicDetailActivity.this, R.string.nomoreloaded,
							Toast.LENGTH_SHORT).show();
				}
			}
			
			topicdetaillistview.onRefreshComplete();
		}
	}

	private class TopicDetailAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		private List<Post> g_topicdetaillist;

		public TopicDetailAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
			inflater = LayoutInflater.from(context);
			g_topicdetaillist = new ArrayList<Post>();
		}
		
		public boolean addtopicdetaillist(List<Post> newtopicdetaillist)
		{
			boolean hasnew = false;
			for(Post post : newtopicdetaillist)
			{
				if(post.getPosition() > maxposition)
				{
					maxposition = post.getPosition();
				}
			}
			g_topicdetaillist.addAll(newtopicdetaillist);
			if(newtopicdetaillist.size()>0)
			{
				hasnew = true;
			}
			
			return hasnew;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return g_topicdetaillist.size();
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
				view = (View) inflater.inflate(
						R.layout.forumtopicdetail_list_item, parent, false);
				v = new ViewHolder();
				view.setTag(v);
			}
			v.setPid(g_topicdetaillist.get(position).getPid());
			v.setTid(g_topicdetaillist.get(position).getTid());
			
			TextView contentTextView = (TextView) view.findViewById(R.id.topic_content);
			contentTextView.setText(g_topicdetaillist.get(position).getMessage());
			
			TextView authorTextView = (TextView) view.findViewById(R.id.topic_author);
			authorTextView.setText(g_topicdetaillist.get(position).getAuthor());
			
			TextView pubtimeTextView = (TextView) view.findViewById(R.id.topic_pubtime);
			SimpleDateFormat formattime=new SimpleDateFormat("yyyyMMdd");
			Date date = new Date(g_topicdetaillist.get(position).getCreatetime().getTime());
			String formatdate = formattime.format(date);
			pubtimeTextView.setText(formatdate);

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
