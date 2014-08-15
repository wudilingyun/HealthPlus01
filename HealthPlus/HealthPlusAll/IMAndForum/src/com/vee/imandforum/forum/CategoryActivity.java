package com.vee.imandforum.forum;

import java.util.List;

import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.imandforum.R;
import com.vee.imandforum.main.ForumFragment;
import com.yunfox.s4aservicetest.response.Category;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class CategoryActivity extends Activity {

	private ListView categorylistview;
	List<Category> g_categorylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		categorylistview = (ListView) findViewById(R.id.CategoryList);
		categorylistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setSelected(true);
				ViewHolder v = (ViewHolder) view.getTag();
				Intent intent = new Intent(CategoryActivity.this,
						ForumFragment.class);
				intent.putExtra("cid", v.getCategoryid());
				startActivity(intent);

				Toast.makeText(
						CategoryActivity.this,
						"clicked " + v.getCategoryid() + "  "
								+ v.getTextView().getText(), Toast.LENGTH_SHORT)
						.show();
			}
		});

		new GetCategoryTask().execute();
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetCategoryTask extends
			AsyncTask<Void, Void, List<Category>> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected List<Category> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				List<Category> forumcategorylist = SpringAndroidService
						.getInstance(getApplication()).getAllForumCategory();

				return forumcategorylist;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Category> forumcategorylist) {
			if (exception != null) {

			}

			if (forumcategorylist != null) {
				g_categorylist = forumcategorylist;
				CategoryAdapter myAdapter = new CategoryAdapter(CategoryActivity.this);
				categorylistview.setAdapter(myAdapter);
			}
		}

	}

	private class CategoryAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;

		public CategoryAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return g_categorylist.size();
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
			v.getTextView().setText(
					g_categorylist.get(position).getCname());
			v.setCategoryid(g_categorylist.get(position).getCid());

			return view;
		}

	}

	private class ViewHolder {
		private TextView textView;
		private int categoryid;

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}

		public int getCategoryid() {
			return categoryid;
		}

		public void setCategoryid(int categoryid) {
			this.categoryid = categoryid;
		}
	}
}
