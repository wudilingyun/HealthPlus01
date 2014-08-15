package com.vee.imandforum.main;

import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import android.app.ProgressDialog;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.vee.imandforum.R;
import com.vee.imandforum.authorization.SignInActivity;
import com.vee.imandforum.forum.TopicActivity;
import com.yunfox.s4aservicetest.response.Forum;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ForumFragment extends SherlockFragment {
	private List<Forum> g_forumlist;
	private ListView forumsListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_forum, container, false);
		forumsListView = (ListView) view.findViewById(R.id.ForumsListView);
		forumsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				view.setSelected(true);
				ViewHolder v = (ViewHolder) view.getTag();
				Intent intent = new Intent(getActivity(), TopicActivity.class);
				intent.putExtra("fid", v.getForumid());
				startActivity(intent);
				Toast.makeText(getActivity(), "clicked " + v.getForumid()
						+ "  " + v.getTextView().getText(), Toast.LENGTH_SHORT).show();
			}
		});

		new GetForumTask().execute();
		return view;
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetForumTask extends AsyncTask<Void, Void, List<Forum>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(getActivity());
			dialog.show();
		}

		@Override
		protected List<Forum> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Forum> forumlist = SpringAndroidService.getInstance(
						getActivity().getApplication()).getAllForums();

				return forumlist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Forum> forumlist) {
			dialog.dismiss();
			if (exception != null) {
				String message;

				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					Toast.makeText(ForumFragment.this.getActivity(), "连接超时",
							Toast.LENGTH_SHORT).show();
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
					SpringAndroidService.getInstance(
							getActivity().getApplication()).signOut();
					startActivity(new Intent(ForumFragment.this.getActivity(),
							SignInActivity.class));
					getActivity().finish();
				} else if (exception instanceof ExpiredAuthorizationException) {
					message = "authorization expired";
					SpringAndroidService.getInstance(
							getActivity().getApplication()).signOut();
					startActivity(new Intent(ForumFragment.this.getActivity(),
							SignInActivity.class));
					getActivity().finish();
				} else {
					Toast.makeText(ForumFragment.this.getActivity(), "网络连接错误",
							Toast.LENGTH_SHORT).show();
				}
			}

			if (forumlist != null) {
				g_forumlist = forumlist;
				ForumAdapter forumAdapter = new ForumAdapter(getActivity());
				//forumgridview.setAdapter(forumAdapter);
				forumsListView.setAdapter(forumAdapter);
				forumAdapter.notifyDataSetChanged();
			}
		}
	}

	private class ForumAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;

		public ForumAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return g_forumlist.size();
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
						R.layout.forumcategory_list_item, parent, false);
				v = new ViewHolder();
				v.setTextView((TextView) view
						.findViewById(R.id.forumcategorylistitem));
				view.setTag(v);
			}
			v.getTextView().setText(g_forumlist.get(position).getFname());
			v.setForumid(g_forumlist.get(position).getFid());

			return view;
		}

	}

	private class ViewHolder {
		private TextView textView;
		private int forumid;

		public int getForumid() {
			return forumid;
		}

		public void setForumid(int forumid) {
			this.forumid = forumid;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
	}
}
