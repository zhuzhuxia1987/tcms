package com.tcms.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tcms.TCMSApplication;
import com.tcms.R;
import com.tcms.entity.UserInfo;
import com.tcms.fragment.HomeFragment;
import com.tcms.fragment.MainPaperAdapter;
import com.tcms.fragment.MoreFragment;
import com.tcms.fragment.MyTcmsFragment;
import com.tcms.fragment.ShenPiFragment;
import com.tcms.view.CustomDialog;
import com.tcms.view.CustomDialog.ButtonRespond;

/**
 * @ClassName: MainActivity
 * @Description: 主界面
 * @author 朱恒章
 * @date 2016-5-17 上午11:32:26
 * @package_name 路径：com.tcms.activity
 */
public class MainActivity extends BaseFragmentActivity {

	public static final int HomeIndex = 0;

	public static final int ShenPiIndex = 1;
	// public static final int MyTcmsIndex = 2;
	public static final int MoreIndex = 2;

	private ViewPager viewPager;
	private ArrayList<Fragment> fragmentList;

	private MyTcmsFragment myTcmsFragment;

	private ShenPiFragment shenPiFragment;
	private MoreFragment moreFragment;
	private HomeFragment homeFragment;

	private RadioGroup rgNavigation;// 导航栏的单选框
	private RadioButton rbHome;// 主页
	// private RadioButton rbMytcms;// 我的

	private RadioButton rbShenPi;// 审批
	private RadioButton rbMore;// 更多

	private CustomDialog dialog;
	private int lastIndex = 0;// 上一条目
	private int currentIndex = 0;// 当前页
	public static UserInfo user;

	public static UserInfo getUser() {
		return user;
	}

	public static void setUser(UserInfo user) {
		MainActivity.user = user;
	}

	public Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		Serializable serializable = intent.getSerializableExtra("User");
		if (serializable != null) {
			user = (UserInfo) serializable;
			TCMSApplication.user = user;
		}
		initView();

		// // 通过线程方式管理Sd卡下面的图片缓存
		// ThreadPoolService.execute(new CustomRunnable<Void, Void>() {
		// @Override
		// public Void executeTask(Void... param) {
		// File projectdirec = FileUitls.createDirectory(MainActivity.this,
		// Constant.PROJECTIMG);
		// FileUitls.deleteOldfiles(projectdirec, Constant.MAXFIESNUM,
		// Constant.MAXFIESZISE, 1);
		// return null;
		// }
		// });
		//
		// // 清除除开本用户外的其它用户图像
		// if (user.getPicture() != null) {
		// ThreadPoolService.execute(new CustomRunnable<Void, Void>() {
		// @Override
		// public Void executeTask(Void... param) {
		// File projectdirec = FileUitls.createDirectory(MainActivity.this,
		// Constant.USERICFODER);
		// File safe = new File(projectdirec.getPath() + "/" +
		// user.getPicture().substring(user.getPicture().lastIndexOf('/')));
		// FileUitls.deleteOtherfiles(projectdirec, safe);
		// return null;
		// }
		// });
		// }
	}

	/**
	 * 页面初始化
	 */

	private void initView() {
		viewPager = (ViewPager) this.findViewById(R.id.viewPager);
		myTcmsFragment = new MyTcmsFragment();
		// investmentFragment = new InvestmentFragment();
		shenPiFragment = new ShenPiFragment();
		moreFragment = new MoreFragment();
		homeFragment = new HomeFragment();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(HomeIndex, homeFragment);
		// fragmentList.add(InvestIndex, investmentFragment);
		fragmentList.add(ShenPiIndex, shenPiFragment);
		// fragmentList.add(MyTcmsIndex, myTcmsFragment);
		fragmentList.add(MoreIndex, moreFragment);

		viewPager.setAdapter(new MainPaperAdapter(getSupportFragmentManager(),
				fragmentList));
		viewPager.setCurrentItem(0);
		viewPager.setOffscreenPageLimit(5);
		viewPager.setOnPageChangeListener(new MainPaperChangeListener());
		rgNavigation = (RadioGroup) this.findViewById(R.id.rg_navigation);
		rgNavigation
				.setOnCheckedChangeListener(new NavigationCheckChangeListener());

		rbHome = (RadioButton) this.findViewById(R.id.rb_home);
		// rbMytcms = (RadioButton) this.findViewById(R.id.rb_mytcms);
		// rbInvestment = (RadioButton) this.findViewById(R.id.);
		rbShenPi = (RadioButton) this.findViewById(R.id.rb_shenpi);
//		rbShenPi.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (isChecked) {
//					shenPiFragment.getcailiao(userid, spqx);
//					
//					
//				}
//			}
//		});
		rbMore = (RadioButton) this.findViewById(R.id.rb_more);

	}

	class NavigationCheckChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.rb_home:
				viewPager.setCurrentItem(HomeIndex);
				break;
			// case R.id.rb_mytcms:
			// viewPager.setCurrentItem(MyTcmsIndex);
			// break;
			// case R.id.rb_investment:
			// viewPager.setCurrentItem(InvestIndex);
			// break;
			case R.id.rb_shenpi:
				viewPager.setCurrentItem(ShenPiIndex);
				// shenPiFragment.viewOnscreen();
				break;
			case R.id.rb_more:
				viewPager.setCurrentItem(MoreIndex);
				break;

			default:
				break;
			}
		}

	}

	class MainPaperChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int paperIndex) {
			lastIndex = currentIndex;
			switch (paperIndex) {
			case HomeIndex:
				rbHome.setChecked(true);
				break;
			// case MyTcmsIndex:
			// rbMytcms.setChecked(true);
			// break;
			// case InvestIndex:
			// rbInvestment.setChecked(true);
			// investmentFragment.loadProjectFirst();
			// break;
			case ShenPiIndex:
				rbShenPi.setChecked(true);
				break;
			case MoreIndex:
				rbMore.setChecked(true);
				break;

			default:
				break;
			}

			currentIndex = paperIndex;
		}
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	// public RadioButton getRbShooting() {
	// return rbShooting;
	// }

	public RadioButton getRbShenPi() {
		return rbShenPi;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (viewPager.getCurrentItem() == ShenPiIndex) {
			// 防止 android Skipped 30 frames! The application may be doing too
			// much work on its
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// shenPiFragment.viewOnscreen();
				}
			}, 20);
		}
	}

	@Override
	public void onPause() {
		if (viewPager.getCurrentItem() == ShenPiIndex) {
			// shenPiFragment.viewOutscreen();
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// if (viewPager.getCurrentItem() == ShenPiIndex) {
		// if (shenPiFragment.isLl_Normal_resultShowqing()) {
		// shenPiFragment.setLl_Normal_resultVisibility(View.GONE);
		// shenPiFragment.setviewFinderViewVisibility(View.VISIBLE);
		// shenPiFragment.restartPreviewAfterDelay(0L);
		// } else {
		// exitAPP();
		// }
		//
		// } else {
		exitAPP();
		// }
	}

	/**
	 * 退出程序
	 */
	public void exitAPP() {
		dialog = new CustomDialog(MainActivity.this, new ButtonRespond() {

			@Override
			public void buttonRightRespond() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				TCMSApplication.instance.onTerminate();
			}

			@Override
			public void buttonLeftRespond() {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});
		dialog.setDialogTitle(R.string.exit_app);
		dialog.setDialogMassage(R.string.live_for_time);
		dialog.setLeftButtonText(R.string.no);
		dialog.setRightButtonText(R.string.yes);
		// dialog.setLeftButonBackgroud(R.drawable.b);
		dialog.show();
	}

}
