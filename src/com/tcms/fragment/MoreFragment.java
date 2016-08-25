package com.tcms.fragment;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.R;
import com.tcms.activity.AboutActivity;
import com.tcms.activity.CityActivity;
import com.tcms.activity.CityChooseActivity;
import com.tcms.activity.HelpCaptionActivity;
import com.tcms.activity.LoginActivity;
import com.tcms.activity.MainActivity;
import com.tcms.activity.RegisterActivity;
import com.tcms.activity.SplashActivity;
import com.tcms.activity.TelUsActivity;
import com.tcms.entity.UserInfo;
import com.tcms.utils.APPUltil;
import com.tcms.view.CustomDialog;
import com.tcms.view.CustomDialog.ButtonRespond;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: MoreFragment
 * @Description: 更多 fragment
 * @author 朱恒章
 * @date 2016-5-18 上午10:22:52
 * @package_name 路径：com.tcms.fragment
 */
public class MoreFragment extends Fragment implements OnClickListener {

	private TextView tvTitle;
	private RelativeLayout rlgenggaijigou;// 更改地区机构
	private RelativeLayout rlCheckVerson;// 更换用户
	private RelativeLayout rlAboutUs;// 关于我们
	private RelativeLayout rlHelps;// 帮助说明
	private RelativeLayout rlTelUs;// 联系我们
	private RelativeLayout rl_Upversion;// 版本更新
	private Button btExitAPP;// 退出系统
	private CustomDialog dialog;
	private APPUltil appUltil;
	private Handler handler;
	private UserInfo user;

	// private C

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_more, container, false);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		TCMSApplication.setBoldText(tvTitle);
		tvTitle.setText(R.string.more);
		handler = ((MainActivity) getActivity()).handler;
		appUltil = new APPUltil(getActivity(), handler);
		rlgenggaijigou = (RelativeLayout) view
				.findViewById(R.id.rl_genggaijigou);
		rlgenggaijigou.setOnClickListener(this);
		rlCheckVerson = (RelativeLayout) view.findViewById(R.id.rl_checkVerson);
		rlCheckVerson.setOnClickListener(this);
		rlAboutUs = (RelativeLayout) view.findViewById(R.id.rl_aboutUs);
		rlAboutUs.setOnClickListener(this);
		rlHelps = (RelativeLayout) view.findViewById(R.id.rl_help);
		rlHelps.setOnClickListener(this);
		rlTelUs = (RelativeLayout) view.findViewById(R.id.rl_telUs);
		rlTelUs.setOnClickListener(this);
		rl_Upversion = (RelativeLayout) view.findViewById(R.id.rl_Upversion);
		rl_Upversion.setOnClickListener(this);
		btExitAPP = (Button) view.findViewById(R.id.bt_exitapp);
		btExitAPP.setOnClickListener(this);
		return view;
	}

	public MoreFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_genggaijigou:
			startActivityForResult(new Intent(getActivity(),
					CityChooseActivity.class), 200);
			break;
		case R.id.rl_checkVerson:
			// appUltil.getNewstVersion("正在检查更新");
			ClearUser();
			// dialog=appUltil.getDialog();
			// Toast.makeText(getActivity(), "检查更新", 0).show();
			break;
		case R.id.rl_aboutUs:
			startActivity(new Intent(getActivity(), AboutActivity.class));
			break;
		case R.id.rl_help:
			startActivity(new Intent(getActivity(), HelpCaptionActivity.class));
			break;
		case R.id.rl_telUs:
			startActivity(new Intent(getActivity(), TelUsActivity.class));
			break;
		case R.id.rl_Upversion:
			appUltil.getNewstVersion("正在检查更新");
			break;
		case R.id.bt_exitapp:
			((MainActivity) getActivity()).exitAPP();
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 200) {
			if (resultCode == 3001) {
				user = (UserInfo) data.getSerializableExtra("user");
				dialog = new CustomDialog(getActivity(), new ButtonRespond() {

					@Override
					public void buttonRightRespond() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						startActivity(new Intent(getActivity(),
								SplashActivity.class));
						getActivity().finish();

					}

					@Override
					public void buttonLeftRespond() {
						// TODO Auto-generated method stub
						dialog.dismiss();

					}
				});
				dialog.setDialogTitle(R.string.chongqi_app);
				dialog.setDialogMassage(R.string.chognxinqidong);
				dialog.setLeftButtonText(R.string.no);
				dialog.setRightButtonText(R.string.yes);
				// dialog.setLeftButonBackgroud(R.drawable.b);
				dialog.show();

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static SharedPreferences getOrSharedPrefences(Context context) {
		return context.getSharedPreferences(Constant.ORPREFERENCES,
				Context.MODE_PRIVATE);

	}

	/**
	 * 是否清除用户信息
	 */
	public void ClearUser() {
		dialog = new CustomDialog(getActivity(), new ButtonRespond() {

			@Override
			public void buttonRightRespond() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				SharedPreferences sp = getOrSharedPrefences(getActivity());
				Editor editor = sp.edit();
				editor.putBoolean(Constant.ISSAVEPW, false);
				editor.commit();
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}

			@Override
			public void buttonLeftRespond() {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});
		dialog.setDialogTitle(R.string.gguser);
		dialog.setDialogMassage(R.string.clearuser);
		dialog.setLeftButtonText(R.string.no);
		dialog.setRightButtonText(R.string.yes);
		// dialog.setLeftButonBackgroud(R.drawable.b);
		dialog.show();
	}

}
