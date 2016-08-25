package com.tcms.fragment;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.R;
import com.tcms.activity.AboutActivity;
import com.tcms.activity.LoginActivity;
import com.tcms.activity.MainActivity;
import com.tcms.activity.RegisterActivity;
import com.tcms.activity.ShenPiActivity;
import com.tcms.entity.HttpDatas;
import com.tcms.entity.JianYanXinXiBean;
import com.tcms.entity.UserInfo;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;
import com.tcms.utils.JSONUtil;
import com.tcms.utils.NetUtils;

/**
 * @ClassName: ShenPiFragment
 * @Description: 审批 fragment
 * @author 朱恒章
 * @date 2016-5-18 上午10:23:27
 * @package_name 路径：com.tcms.fragment 方法索引： getbaolist(String uid, String spqx,
 *               String clid);获取报告列表 loadLoginName(String jigouini, String
 *               phone) 加载用户 getcailiao(String userid, String spqx) 获取材料列表
 * 
 */
public class ShenPiFragment extends Fragment implements OnClickListener {

	private TextView tvTitle;// 标题
	private TextView tvdangqianuser;
	private Spinner spcailiao;// 材料下拉菜单
	private TextView tvcailiaonull;// 材料为空时显示
	private Button bt_chaxun;// 查询按钮
	private UserInfo user;// 后台传递过来的用户信息
	private String loginname;
	private ArrayAdapter<String> arr_cailiao; // 适配器数据——材料
	Hashtable<String, String> cailiaoGroup = new Hashtable<String, String>();
	private String cailiaoid;
	private String userid;
	private List<JianYanXinXiBean> list;
	Hashtable<String, Object> baogaoGroup = new Hashtable<String, Object>();

	ArrayList<HashMap<String, Object>> baogaolist = new ArrayList<HashMap<String, Object>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_shenpi, container, false);
		user = MainActivity.getUser();// 后台传递过来的用户信息
		initView(view);
		SharedPreferences sp = getOrSharedPrefences(getActivity());
		loginname = sp.getString(Constant.XINGMING, null);
		if (loginname == null) {
			String jigouini = user.getJigouini();
			String phone = user.getPhone();
			if (jigouini == null) {
				spcailiao.setVisibility(View.GONE);
				tvcailiaonull.setVisibility(View.VISIBLE);
				bt_chaxun.setEnabled(false);
				return view;
			}
			loadLoginName(jigouini, phone);
		} else {
			if (user.getJigouini() == null) {
				return view;
			}
			tvdangqianuser.setText("检测到您在当前机构下姓名：" + loginname);
			userid = sp.getString(Constant.USERID, null);
			user.setUserid(userid);
			user.setJigouid(sp.getString(Constant.JIGOUINI, null));
			user.setXingming(sp.getString(Constant.XINGMING, null));
			getcailiao(userid, Constant.SHENPIQX);
		}

		return view;

	}

	/**
	 * 初始化View对象
	 * 
	 * @param view
	 */
	private void initView(View view) {
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		TCMSApplication.setBoldText(tvTitle);
		tvTitle.setText(R.string.title_daishenpichaxun);
		tvdangqianuser = (TextView) view.findViewById(R.id.tv_dangqianuser);
		spcailiao = (Spinner) view.findViewById(R.id.sp_cailiao);
		// spbaogao = (Spinner) view.findViewById(R.id.sp_baogao);
		bt_chaxun = (Button) view.findViewById(R.id.bt_daishenpi_chaxun);
		bt_chaxun.setOnClickListener(this);
		tvcailiaonull = (TextView) view.findViewById(R.id.tv_cailiaonull);
		tvcailiaonull.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_daishenpi_chaxun:
			// startActivity(new Intent(getActivity(), ShenPiActivity.class));
			getbaolist(userid, Constant.SHENPIQX, cailiaoid);
			break;
		default:
			break;
		}
	}

	private void getbaolist(String uid, String spqx, String clid) {
		HttpDatas datas = new HttpDatas(user.getJigouini()
				+ Constant.GETDAICHULIBAOGAO);

		datas.putParam("uid", uid);
		datas.putParam("spqx", spqx);
		datas.putParam("clid", clid);
		NetUtils.sendRequest(datas, getActivity(), getString(R.string.loading),
				new TaskCallBack() {

					@Override
					public void beforeTask() {
						// TODO Auto-generated method stub

					}

					@Override
					public int excueHttpResponse(String respondsStr) {
						// TODO Auto-generated method stub
						System.out.println(respondsStr);
						int code = 0;
						if (respondsStr == null || respondsStr.equals("[]")) {
							return code;
						}

						try {

							JSONArray array = new JSONArray(respondsStr);
							list = JSONUtil.jsonArray2ArrayList(array,
									JianYanXinXiBean.class);
							System.out.println(list);
							Iterator it = list.iterator();
							while (it.hasNext()) {
								JianYanXinXiBean p = (JianYanXinXiBean) it
										.next();
								System.out.println(p);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("ItemTitle", p.getJianYanBH());
								map.put("ItemText", p.getBaoGaomenu());
								baogaolist.add(map);
								baogaoGroup.put(p.getJianYanBH(), p);
							}
							System.out.println(baogaolist);
							System.out.println(baogaoGroup);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return code;
					}

					@Override
					public void afterTask(int result) {
						// TODO Auto-generated method stub
						switch (result) {

						case 0:
							Intent intent = new Intent(getActivity(),
									ShenPiActivity.class);
							intent.putExtra("baogaolist",
									(Serializable) baogaolist);
							intent.putExtra("baogaoGroup",
									(Serializable) baogaoGroup);
							intent.putExtra("userinfo", user);
							getActivity().startActivity(intent);

							baogaolist.clear();
							baogaoGroup.clear();
							break;
						default:
							TCMSApplication.showResultToast(result,
									getActivity());
							break;
						}
					}

				});
	}

	private void loadLoginName(String jigouini, String phone) {
		HttpDatas datas = new HttpDatas(jigouini + Constant.GETLOGINNAME);

		datas.putParam("phone", phone);

		NetUtils.sendRequest(datas, getActivity(),
				getString(R.string.chushihua), new TaskCallBack() {
					// ArrayList<LoanSummary> list;
					UserInfo info;

					@Override
					public int excueHttpResponse(String respondsStr) {
						int code = 0;
						try {
							JSONObject jsonObject = new JSONObject(respondsStr);
							code = jsonObject.getInt("coding");
							if (code == Constant.RESPONSE_OK) {
								// loginname = jsonObject.getString("msg");
								info = JSONUtil.jsonObject2Bean(jsonObject,
										UserInfo.class);
								// list =
								// JSONUtil.jsonArray2ArrayList(jsonArray,
								// LoanSummary.class);
								userid = info.getUserid();
								user.setUserid(userid);
								user.setJigouid(info.getJigouid());
								user.setXingming(info.getXingming());
								SharedPreferences sp = getOrSharedPrefences(getActivity());
								Editor editor = sp.edit();
								editor.putString(Constant.LOGINNSME,
										info.getLoginname());
								editor.putString(Constant.XINGMING,
										info.getXingming());
								editor.putString(Constant.USERID, userid);
								editor.commit();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						return code;
					}

					@Override
					public void beforeTask() {
					}

					@Override
					public void afterTask(int result) {
						switch (result) {
						case Constant.RESPONSE_OK:
							tvdangqianuser.setText("检测到您在当前机构下姓名："
									+ info.getXingming());
							getcailiao(info.getUserid(), Constant.SHENPIQX);
							break;
						case Constant.OS_FAILED:
							tvdangqianuser
									.setText("未检测到您在当前机构注册过用户，请与当前检测机构联系！！");
							spcailiao.setVisibility(View.GONE);
							tvcailiaonull.setVisibility(View.VISIBLE);
							bt_chaxun.setEnabled(false);
							break;
						default:
							spcailiao.setVisibility(View.GONE);
							tvcailiaonull.setVisibility(View.VISIBLE);
							bt_chaxun.setEnabled(false);
							TCMSApplication.showResultToast(result,
									getActivity());
							break;
						}
					}

				});
	}

	private void getcailiao(String userid, String spqx) {
		HttpDatas datas = new HttpDatas(user.getJigouini()
				+ Constant.GETCAILIAO);

		datas.putParam("userid", userid);
		datas.putParam("spqx", spqx);

		NetUtils.sendRequest(datas, getActivity(),
				getString(R.string.loadingcailiao), new TaskCallBack() {
					ArrayList<String> cailiaolist = new ArrayList<String>();

					@Override
					public void beforeTask() {
						// TODO Auto-generated method stub

					}

					@Override
					public int excueHttpResponse(String respondsStr) {
						System.out.println(respondsStr);
						int code = 0;
						if (respondsStr == null || respondsStr.equals("[]")) {
							return code;
						}
						code = 1;

						try {
							JSONArray array = new JSONArray(respondsStr);
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonobject = array.getJSONObject(i);
								String cailiaomc = jsonobject.getString("name");
								String cailiaoid = jsonobject.getString("id");
								cailiaolist.add(cailiaomc);
								cailiaoGroup.put(cailiaomc, cailiaoid);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return code;
					}

					@Override
					public void afterTask(int result) {
						// TODO Auto-generated method stub
						switch (result) {

						case 1:
							if (cailiaolist.size() == 0) {
								spcailiao.setVisibility(View.GONE);
								tvcailiaonull.setVisibility(View.VISIBLE);
								bt_chaxun.setEnabled(false);

							} else {
								clspinnerlist(cailiaolist);
							}

							break;
						default:
							TCMSApplication.showResultToast(result,
									getActivity());
							break;
						}
					}

				});

	}

	private void clspinnerlist(ArrayList<String> cailiaolist) {
		// TODO Auto-generated method stub
		arr_cailiao = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item, cailiaolist);
		arr_cailiao
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spcailiao.setAdapter(arr_cailiao);
		spcailiao.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String cailiaomc = spcailiao.getItemAtPosition(position)
						.toString();
				cailiaoid = cailiaoGroup.get(cailiaomc);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

	}

	public static SharedPreferences getOrSharedPrefences(Context context) {
		return context.getSharedPreferences(Constant.ORPREFERENCES,
				Context.MODE_PRIVATE);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		getcailiao(userid, Constant.SHENPIQX);

		super.onResume();
	}

}
