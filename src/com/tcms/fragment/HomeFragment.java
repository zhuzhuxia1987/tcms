package com.tcms.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import com.tcms.R;
import com.tcms.activity.NullActivity;
import com.tcms.entity.AdDomain;

/**
 * @ClassName: HomeFragment
 * @Description: 主页
 * @author 朱恒章
 * @date 2016-5-18 上午10:22:14
 * @package_name 路径：com.tcms.fragment
 */
public class HomeFragment extends Fragment implements OnClickListener {
	private ImageView yanfazhong;
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径

	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合

	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;
	private int currentItem = 0; // 当前图片的索引号
	// 定义的五个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	
	private RelativeLayout rlRecomMember;// 
	private RelativeLayout rlRecomProject;// 
	private RelativeLayout rlBecomeDeputy;// 
	private RelativeLayout rlInvestment;// 

	private ScheduledExecutorService scheduledExecutorService;

	// 异步加载图片
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	// 轮播banner的数据
	private List<AdDomain> adList;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
//		yanfazhong = (ImageView) view.findViewById(R.id.kaifazhong);
		rlRecomMember = (RelativeLayout) view.findViewById(R.id.rl_recom_member);
		rlRecomMember.setOnClickListener(this);
		rlRecomProject = (RelativeLayout) view.findViewById(R.id.rl_recom_project);
		rlRecomProject.setOnClickListener(this);
		rlBecomeDeputy = (RelativeLayout) view.findViewById(R.id.rl_become_deputy);
		rlBecomeDeputy.setOnClickListener(this);
		rlInvestment = (RelativeLayout) view.findViewById(R.id.rl_investment);
		rlInvestment.setOnClickListener(this);
		// 使用ImageLoader之前初始化
		initImageLoader();
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.top_banner_android)
				.showImageForEmptyUri(R.drawable.top_banner_android)
				.showImageOnFail(R.drawable.top_banner_android)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();

		initAdData(view);

		startAd();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.rl_recom_member:// 
			startActivity(new Intent(getActivity(), NullActivity.class));
			break;
		case R.id.rl_recom_project://
			startActivity(new Intent(getActivity(), NullActivity.class));
			break;

		case R.id.rl_become_deputy:// 
			startActivity(new Intent(getActivity(), NullActivity.class));
			break;

		case R.id.rl_investment:// 
			// 
			startActivity(new Intent(getActivity(), NullActivity.class));
			break;

	

		default:
			break;
		}
	}
	private void initImageLoader() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(getActivity(),
				IMAGE_CACHE_PATH);

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).defaultDisplayImageOptions(defaultOptions)
				.memoryCacheExtraOptions(480, 800)
				.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
				.memoryCacheSize(12 * 1024 * 1024)
				.discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}

	private void initAdData(View view) {
		// 广告数据
		adList = getBannerAd();

		imageViews = new ArrayList<ImageView>();

		// 点
		dots = new ArrayList<View>();
		dotList = new ArrayList<View>();
		dot0 = view.findViewById(R.id.v_dot0);
		dot1 = view.findViewById(R.id.v_dot1);
		dot2 = view.findViewById(R.id.v_dot2);
		dot3 = view.findViewById(R.id.v_dot3);
		dot4 = view.findViewById(R.id.v_dot4);
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		//
		// tv_date = (TextView) findViewById(R.id.tv_date);
		// tv_title = (TextView) findViewById(R.id.tv_title);
		// tv_topic_from = (TextView) findViewById(R.id.tv_topic_from);
		// tv_topic = (TextView) findViewById(R.id.tv_topic);

		adViewPager = (ViewPager) view.findViewById(R.id.vp);
		adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		adViewPager.setOnPageChangeListener(new MyPageChangeListener());
		addDynamicView();
	}

	private void addDynamicView() {
		// 动态添加图片和下面指示的圆点
		// 初始化图片资源
		for (int i = 0; i < adList.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			// 异步加载图片
			mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,
					options);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));
		}
	}

	private void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	// @Override
	// protected void onStop() {
	// super.onStop();
	// // 当Activity不可见的时候停止切换
	// scheduledExecutorService.shutdown();
	// }

	private class MyPageChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			AdDomain adDomain = adList.get(position);
			// tv_title.setText(adDomain.getTitle()); // 设置标题
			// tv_date.setText(adDomain.getDate());
			// tv_topic_from.setText(adDomain.getTopicFrom());
			// tv_topic.setText(adDomain.getTopic());
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return adList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = imageViews.get(position);
			((ViewPager) container).addView(iv);
			final AdDomain adDomain = adList.get(position);
			// 在这个方法里面设置图片的点击事件
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 处理跳转逻
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(adDomain.getTargetUrl()));
					startActivity(intent);
				}
			});
			return iv;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}

	/**
	 * 轮播广播模拟数据
	 * 
	 * @return
	 */
	public static List<AdDomain> getBannerAd() {
		List<AdDomain> adList = new ArrayList<AdDomain>();

		AdDomain adDomain = new AdDomain();
		// adDomain.setId("108078");
		// adDomain.setDate("3月4日");
		// adDomain.setTitle("我和令计划只是同姓");
		// adDomain.setTopicFrom("阿宅");
		// adDomain.setTopic("我想知道令狐安和令计划有什么关系？");
		adDomain.setImgUrl("http://www.jsgl.com.cn/r/cms/www/red/images/banner_8.jpg");
		adDomain.setTargetUrl("http://www.jsgl.com.cn/jhhyxw/1087.jhtml");
		adDomain.setAd(false);
		adList.add(adDomain);

		AdDomain adDomain2 = new AdDomain();

		adDomain2
				.setImgUrl("http://www.jsgl.com.cn/r/cms/www/red/images/banner_7.jpg");
		adDomain2.setTargetUrl("http://www.jsgl.com.cn/jhhyxw/1037.jhtml");
		adDomain2.setAd(false);
		adList.add(adDomain2);

//		AdDomain adDomain3 = new AdDomain();
//		// adDomain3.setId("108078");
//		// adDomain3.setDate("3月6日");
//		// adDomain3.setTitle("我和令计划只是同姓");
//		// adDomain3.setTopicFrom("旭东");
//		// adDomain3.setTopic("“我想知道令狐安和令计划有什么关系？”");
//		adDomain3
//				.setImgUrl("http://www.jsgl.com.cn/r/cms/www/red/images/banner_4.jpg");
//		adDomain3.setAd(false);
//		adList.add(adDomain3);

		AdDomain adDomain4 = new AdDomain();
		// adDomain4.setId("108078");
		// adDomain4.setDate("3月7日");
		// adDomain4.setTitle("我和令计划只是同姓");
		// adDomain4.setTopicFrom("小软");
		// adDomain4.setTopic("“我想知道令狐安和令计划有什么关系？”");
		adDomain4
				.setImgUrl("http://www.jsgl.com.cn/r/cms/www/red/images/banner_2.jpg");
		adDomain4.setTargetUrl("http://www.jsgl.com.cn/jhhyxw/988.jhtml");
		adDomain4.setAd(false);
		adList.add(adDomain4);

		AdDomain adDomain5 = new AdDomain();
		// adDomain5.setId("108078");
		// adDomain5.setDate("3月8日");
		// adDomain5.setTitle("我和令计划只是同姓");
		// adDomain5.setTopicFrom("大熊");
		// adDomain5.setTopic("“我想知道令狐安和令计划有什么关系？”");
		adDomain5
				.setImgUrl("http://www.jsgl.com.cn/r/cms/www/red/images/banner_5.jpg");
		adDomain5.setTargetUrl("http://www.jsgl.net.cn/");
		adDomain5.setAd(true); // 代表是广告
		adList.add(adDomain5);

		return adList;
	}



}
