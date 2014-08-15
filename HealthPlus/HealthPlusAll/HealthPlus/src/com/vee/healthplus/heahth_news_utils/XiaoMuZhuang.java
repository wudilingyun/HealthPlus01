package com.vee.healthplus.heahth_news_utils;

import java.util.Calendar;

import com.vee.healthplus.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class XiaoMuZhuang {
	ScrollView scrollAll;
	LinearLayout layoutInner;// 用于加载布局的一个加载器
	LayoutInflater inflater;
	boolean isUpEdge = false;// 记录是否在上边缘
	boolean isDownEdge = false;// 记录是否在下边缘
	boolean isFooterViewEnable = true;// 记录底部的view是否可用
	int lastY = 0;// 记录上一次按下去的Y位置
	int heightInner = 0;// 记录layoutInner的高度
	int heightScroll = 0;// 记录当前Scrolview所在一屏的高度，他的值和手机有关，不过他不会变
	OnUpdatingListener listener = null;// 头部view和底部view刷新的监听器
	/**
	 * 下面几个参数的表达当前的几个状态，大致如下： NULL_STATE 空闲状态，即一般的状态 HEADER_UPDATING 头部正在刷新的状态
	 * FOOTER_UPDATING 底部正在刷新的状态
	 * 
	 * HEADER_PULL_UPDATE 头部下拉刷新的状态 FOOTER_PULL_UPDATE 底部上拉刷新的状态
	 * 
	 * HEADER_RELEASE_UPDATE 头部松手刷新的状态 FOOTER_RELEASE_UPDATE 底部松手刷新的状态
	 */
	final int NULL_STATE = 0, HEADER_UPDATING = 1, FOOTER_UPDATING = 2,
			HEADER_PULL_UPDATE = 3, FOOTER_PULL_UPDATE = 4,
			HEADER_RELEASE_UPDATE = 5, FOOTER_RELEASE_UPDATE = 6;
	int currentState = NULL_STATE;// 默认为空闲状态
	private View viewHeader = null;// header view 头部的view
	private View viewFooter = null;// footer view 底部的view
	private int heightHeaderView;// 记录头部view的高度
	private int heightFooterView;// 记录底部view的高度
	private ImageView imgHeaderArrow;// 头部的指示箭头
	private ImageView imgFooterArrow;// 底部的指示箭头
	private TextView txtTimeHeader;// 头部的刷新时间
	private TextView txtTimeFooter;// 底部的刷新时间
	/**
	 * header show update 头部显示正在刷新或者松手刷，以及下拉刷新等字样
	 */
	private TextView txtUpdatingHeader;
	/**
	 * footer show update 底部显示正在刷新或者松手刷，以及下拉刷新等字样
	 */
	private TextView txtUpdatingFooter;
	private ProgressBar progressBarHeader;// 头部的进度条
	private ProgressBar progressBarFooter;// 底部的进度条
	/**
	 * 使箭头旋转的两个动画类 第一个动画是从0度到-180度 第二个动画是从-180到0度
	 */
	private RotateAnimation animationRotate01, animationRotate02;

	public XiaoMuZhuang(ScrollView scrollAll, LinearLayout layoutIner,
			Context context) {
		this.scrollAll = scrollAll;
		this.layoutInner = layoutIner;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initAnimation();
		initHeaderView();
		initFooterView();
		setPullDownUpForUpdate();
	}

	private void initAnimation() {
		animationRotate01 = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animationRotate01.setInterpolator(new LinearInterpolator());
		animationRotate01.setDuration(250);
		animationRotate01.setFillAfter(true);
		animationRotate02 = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animationRotate02.setInterpolator(new LinearInterpolator());
		animationRotate02.setDuration(250);
		animationRotate02.setFillAfter(true);
	}

	private void initFooterView() {
		viewFooter = inflater.inflate(R.layout.footer, null);
		imgFooterArrow = (ImageView) viewFooter
				.findViewById(R.id.footer_arrowImageView);
		progressBarFooter = (ProgressBar) viewFooter
				.findViewById(R.id.footer_progressBar);
		txtUpdatingFooter = (TextView) viewFooter
				.findViewById(R.id.footer_tipsTextView);
		txtTimeFooter = (TextView) viewFooter
				.findViewById(R.id.footer_lastUpdatedTextView);
		measureView(viewFooter);
		heightFooterView = viewFooter.getMeasuredHeight();
		Log.e("heightHeaderView", "heightFooterView=" + heightFooterView);
		setFooterPaddingBottom(-1 * heightFooterView);
		layoutInner.addView(viewFooter);
	}

	private void initHeaderView() {
		viewHeader = inflater.inflate(R.layout.head, null);
		imgHeaderArrow = (ImageView) viewHeader
				.findViewById(R.id.head_arrowImageView);
		progressBarHeader = (ProgressBar) viewHeader
				.findViewById(R.id.head_progressBar);
		txtUpdatingHeader = (TextView) viewHeader
				.findViewById(R.id.head_tipsTextView);
		txtTimeHeader = (TextView) viewHeader
				.findViewById(R.id.head_lastUpdatedTextView);
		// header layout
		measureView(viewHeader);
		heightHeaderView = viewHeader.getMeasuredHeight();
		Log.e("heightHeaderView", "heightHeaderView=" + heightHeaderView);
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// heightHeaderView);
		// 思前想后，还是决定用改变padding的方式去做，这样效率更快(之前用marginTop的改变来做)
		// if(listener!=null){
		// headerUpdating();
		// }else{
		// setHeaderPaddingTop(-1 * heightHeaderView);
		// }
		setHeaderPaddingTop(-1 * heightHeaderView);// 默认设置不可见，将刷新的接口提供给外部调用
		layoutInner.addView(viewHeader, 0);
	}

	public void setPullDownUpForUpdate() {
		scrollAll.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastY = (int) event.getRawY();
					// 这里貌似不会执行，我暂时也不明白为什么不执行ACTION_DOWN方法，这是很奇怪的一个问题啊
					break;
				case MotionEvent.ACTION_MOVE:
					if (currentState == HEADER_UPDATING
							|| currentState == FOOTER_UPDATING) {
						return false;// 交给其他的方法去处理
					}
					if (heightScroll == 0) {
						heightScroll = scrollAll.getHeight();// 避免重复获取它的高度
						Log.e("第一次获取高度", "heightScroll=" + heightScroll);
						// 这个高度是固定的，是Scrollview当前一屏幕所能拥有的最大高度
						heightInner = layoutInner.getHeight();
						// 这个是Scrollview里面包含的Linearlayout的总高度
						Log.e("第一次获取高度", "heightInner=" + heightInner);
					}
					heightInner = layoutInner.getHeight();
					int downPosition = scrollAll.getScrollY();// 获取scrollview往下移动的位置
					int tempY = (int) event.getRawY();
					// Log.e("aaaaa", "downPosition=" + downPosition);
					if (!isDownEdge
							&& downPosition >= heightInner - heightScroll) {
						isDownEdge = true;
						// Log.e("aaaaa", "isDownEdge = true;=");
						lastY = tempY;
					} else if (!isUpEdge && downPosition <= 0) {
						isUpEdge = true;
						// Log.e("aaaaa", "isUpEdge = true;");
						lastY = tempY;
					} else if (!isUpEdge && !isDownEdge) {
						break;
					}
					int moveY = tempY - lastY;
					// Log.e("aaaaa", "moveY=" + moveY);
					if (!(moveY < -6 || moveY > 6)) {
						// 返回true表示执行自身的ontouch方法，若返回false则执行子view的onTouch方法进行处理
						return false;// 交给其他的方法去处理
					}
					if (moveY > 0
							&& isUpEdge
							&& (currentState == NULL_STATE
									|| currentState == HEADER_PULL_UPDATE || currentState == HEADER_RELEASE_UPDATE)) {
						headerPrepareToUpdating(moveY);
						return true;
					} else if (moveY < 0
							&& isDownEdge
							&& (currentState == NULL_STATE
									|| currentState == FOOTER_PULL_UPDATE || currentState == FOOTER_RELEASE_UPDATE)) {
						footerPrepareToUpdating(-moveY);
						return true;
					}
					break;
				case MotionEvent.ACTION_UP:

					isDownEdge = false;
					isUpEdge = false;

					if (currentState == HEADER_PULL_UPDATE
							|| currentState == HEADER_RELEASE_UPDATE) {
						int paddingTop = getHeaderPaddingTop();
						if (paddingTop >= 0) {
							// 开始刷新
							headerUpdating(false);
						} else {
							// 还没有执行刷新，重新隐藏
							setHeaderPaddingTop(-heightHeaderView);
							currentState = NULL_STATE;
						}
					} else if (currentState == FOOTER_PULL_UPDATE
							|| currentState == FOOTER_RELEASE_UPDATE) {
						int paddingBottom = getFooterPaddingBottom();
						if (paddingBottom >= 0) {
							// 开始执行footer 刷新
							footerUpdating();
						} else {
							// 还没有执行刷新，重新隐藏
							setFooterPaddingBottom(-heightFooterView);
							currentState = NULL_STATE;
						}
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	/**
	 * header refreshing 头部的view正在刷新
	 * 这个方法之所有公开是为了让外部调用，比如外部点击另外某项的时候想让让界面出现一个正在刷新的图层，那么就可以利用这个方法
	 * 
	 * @param isOutComing
	 *            代表是否为外部调用，如果是外部调用，这下面不调用刷新方法，此外需用用户自己手动让刷新方法消失
	 */
	public void headerUpdating(boolean isOutComing) {
		currentState = HEADER_UPDATING;// 头部正在刷新
		setHeaderPaddingTop(0);
		imgHeaderArrow.clearAnimation();// 先清除动画，然后设置图片为null
		imgHeaderArrow.setImageDrawable(null);
		progressBarHeader.setVisibility(View.VISIBLE);
		txtUpdatingHeader.setText("正在刷新...");
		if (listener != null && !isOutComing) {
			listener.onHeaderUpdating(this);
		}
	}

	/**
	 * 底部的view正在刷新
	 */
	private void footerUpdating() {
		currentState = FOOTER_UPDATING;// 底部正在刷新
		setFooterPaddingBottom(0);//
		imgFooterArrow.clearAnimation();// 先清除动画，然后设置图片为null
		imgFooterArrow.setImageDrawable(null);
		progressBarFooter.setVisibility(View.VISIBLE);
		txtUpdatingFooter.setText("正在载入...");
		if (listener != null) {
			listener.onFooterUpdating(this);
		}
	}

	private void setHeaderPaddingTop(int paddingTop) {
		viewHeader.setPadding(0, paddingTop, 0, 0);
		// 设置headerview距离顶部的距离
	}

	private void setFooterPaddingBottom(int paddingBottom) {
		viewFooter.setPadding(0, 0, 0, paddingBottom);
		// viewFooter.invalidate();
		// 设置footerview距离底部的距离
	}

	/**
	 * 获取当前header view 的PaddingTop值
	 */
	private int getHeaderPaddingTop() {
		int paddingTop = viewHeader.getPaddingTop();
		return paddingTop;
	}

	/**
	 * 获取当前footer view 的PaddingBottom值
	 */
	private int getFooterPaddingBottom() {
		int paddingBottom = viewFooter.getPaddingBottom();
		return paddingBottom;
	}

	/**
	 * 下拉刷新完毕之后,可以调用这个方法让头部的view隐藏起来
	 */
	public void setHeaderUpdatingComplete() {
		setHeaderPaddingTop(-heightHeaderView);
		progressBarHeader.setVisibility(View.GONE);
		imgHeaderArrow.setImageResource(R.drawable.arrow_down);// 由于动画的原因，这里反而设置的图片是朝上
		imgHeaderArrow.setVisibility(View.VISIBLE);
		setCurrentTime(txtTimeHeader);
		txtUpdatingHeader.setText("下拉刷新");
		currentState = NULL_STATE;
	}

	/**
	 * 上拉刷新完毕之后,可以调用这个方法让底部的view隐藏起来
	 */
	public void setFooterUpdatingComplete() {
		setFooterPaddingBottom(-heightFooterView);//
		imgFooterArrow.setVisibility(View.VISIBLE);
		imgFooterArrow.setImageResource(R.drawable.arrow_up);// 和上面相反
		progressBarFooter.setVisibility(View.GONE);
		txtUpdatingFooter.setText("上拉获取更多");
		setCurrentTime(txtTimeFooter);
		currentState = NULL_STATE;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释放
	 * 
	 * @param moveY
	 *            ,手指滑动的距离
	 */
	private void headerPrepareToUpdating(int moveY) {
		float paddingTop = -1 * heightHeaderView + moveY * 0.43f;
		viewHeader.setPadding(0, (int) paddingTop, 0, 0);
		if (paddingTop >= 0 && currentState != HEADER_RELEASE_UPDATE) {
			currentState = HEADER_RELEASE_UPDATE;
			imgHeaderArrow.clearAnimation();
			imgHeaderArrow.startAnimation(animationRotate01);
			txtUpdatingHeader.setText("松手刷新");
		} else if (paddingTop < 0 && currentState != HEADER_PULL_UPDATE) {// 拖动时没有释放
			if (currentState != NULL_STATE) {
				imgHeaderArrow.clearAnimation();
				imgHeaderArrow.startAnimation(animationRotate02);
			}
			currentState = HEADER_PULL_UPDATE;
			txtUpdatingHeader.setText("下拉刷新");
		}
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 * 
	 * @param moveY
	 *            ,手指滑动的距离
	 */
	private void footerPrepareToUpdating(int moveY) {
		scrollAll.smoothScrollTo(0,
				(int) (heightInner - heightScroll + moveY * 0.43f));
		float paddintBottom = -1 * heightFooterView + moveY * 0.43f;
		// Log.e("asdfasdf", "paddintBottom=" + paddintBottom);
		setFooterPaddingBottom((int) paddintBottom);
		// LayoutParams params = (LayoutParams) viewHeader.getLayoutParams();
		// float newTopMargin = params.topMargin + moveY * 0.4f;
		// params.topMargin = (int) newTopMargin;
		// viewHeader.setLayoutParams(params);
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		if (currentState != FOOTER_RELEASE_UPDATE && paddintBottom >= 0) {
			imgFooterArrow.clearAnimation();
			imgFooterArrow.startAnimation(animationRotate01);
			txtUpdatingFooter.setText("松手刷新");
			currentState = FOOTER_RELEASE_UPDATE;
		} else if (currentState != FOOTER_PULL_UPDATE && paddintBottom < 0) {
			if (currentState != NULL_STATE) {
				imgFooterArrow.clearAnimation();
				imgFooterArrow.startAnimation(animationRotate02);
			}
			txtUpdatingFooter.setText("上拉获取更多");
			currentState = FOOTER_PULL_UPDATE;
		}
	}

	/**
	 * 当正在刷新的时候会调用这个接口里面的方法， 1.下拉刷新 2.上拉刷新
	 */
	public interface OnUpdatingListener {
		public void onHeaderUpdating(XiaoMuZhuang puller);

		public void onFooterUpdating(XiaoMuZhuang puller);
	}

	/**
	 * 不设置监听器的话就不会调用上面的刷新方法
	 * 
	 * @param listener
	 */
	public void setOnHeaderUpdatingListener(OnUpdatingListener listener) {
		this.listener = listener;
	}

	/**
	 * 给传进来的textview设置当前的时间
	 */
	private void setCurrentTime(TextView txt) {
		txt.setVisibility(View.VISIBLE);
		Calendar a = Calendar.getInstance();
		int hour = a.get(Calendar.HOUR);
		int minute = a.get(Calendar.MINUTE);
		int second = a.get(Calendar.SECOND);
		txt.setText("更新于:" + hour + ":" + minute + ":" + second);
	}

	/**
	 * 当一个view的高度获取不到的时候，先调用这个方法测量一下，那么就可以获取到view的高度了
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		view.measure(childWidthSpec, childHeightSpec);
	}
}
