package com.tcms.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.internal.view.menu.MenuView.ItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.R;
import com.tcms.entity.HttpDatas;
import com.tcms.entity.JianYanXinXiBean;
import com.tcms.entity.UserInfo;
import com.tcms.utils.JSONUtil;
import com.tcms.utils.NetUtils;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;
import com.tcms.utils.PDFUltil;

//agree   agreeall    rollback
/**
 * @ClassName: ShenPiActivity
 * @Description: TODO
 * @author 朱恒章
 * @date 2016-7-27 上午10:24:13
 * @package_name 路径：com.tcms.activity
 */
public class ShenPiActivity extends BaseActivity implements OnClickListener {

	private ListView lv_projects;// 报告list
	private TextView tv_no_project;// 没有可审批报告
	private CheckBox cb_quanxuan;// 全选框
	private Button bt_yijianshenpi;// 意见审批按钮
	private Button bt_previous;
	private Button bt_next;// 下一份
	private TextView tvTitle;// 标题
	private static UserInfo user;
	private static Boolean ismu = false;// 是否全选
	private static String filename = null;

	private static ArrayList<HashMap<String, Object>> listItem;
	private MyAdapter mAdapter;
	private static String path;// 文件路径
	private PDFUltil pdf;
	static HashMap<String, JianYanXinXiBean> baogaoGroup = new HashMap<String, JianYanXinXiBean>();

	ArrayList<HashMap<String, Object>> baogaolist = new ArrayList<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daishenpi);
		initivReabck(this);
		Intent i = getIntent();
		baogaoGroup = (HashMap<String, JianYanXinXiBean>) i
				.getSerializableExtra("baogaoGroup");
		baogaolist = (ArrayList<HashMap<String, Object>>) i
				.getSerializableExtra("baogaolist");
		user = (UserInfo) i.getSerializableExtra("userinfo");
		// user = MainActivity.getUser();
		initView();
		File fileDirect = Environment.getExternalStorageDirectory();
		path = fileDirect.getPath() + "/jypdf";
		pdf = new PDFUltil(ShenPiActivity.this, null);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		TCMSApplication.setBoldText(tvTitle);
		tvTitle.setText(R.string.doinvestment);
		bt_yijianshenpi = (Button) this.findViewById(R.id.bt_yijianshenpi);
		bt_yijianshenpi.setVisibility(View.GONE);
		bt_yijianshenpi.setOnClickListener(this);
		cb_quanxuan = (CheckBox) this.findViewById(R.id.bt_quanxuan);
		cb_quanxuan.setVisibility(View.GONE);
		lv_projects = (ListView) this.findViewById(R.id.lv_projects);
		tv_no_project = (TextView) this.findViewById(R.id.tv_no_project);
		if (baogaolist.size() == 0) {
			tv_no_project.setVisibility(View.VISIBLE);
			lv_projects.setVisibility(View.GONE);
		}
		bt_previous = (Button) this.findViewById(R.id.bt_previous);
		bt_previous.setOnClickListener(this);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
		View head = LayoutInflater.from(this).inflate(
				R.layout.invement_listview_head, null);
		lv_projects.addHeaderView(head);
		mAdapter = new MyAdapter(getDate(), this);// 得到一个MyAdapter对象
		lv_projects.setAdapter(mAdapter);// 为ListView绑定Adapter

		cb_quanxuan
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							for (int i = 0; i < listItem.size(); i++) {
								MyAdapter.getIsSelected().put(i, true);
							}
							ismu = true;
							// 数量设为list的长度

							// 刷新listview和TextView的显示
							dataChanged();
						} else {
							for (int i = 0; i < listItem.size(); i++) {
								MyAdapter.getIsSelected().put(i, false);

								mAdapter.visiblebutton.put(i, Button.VISIBLE);
								mAdapter.visiblecheck.put(i, CheckBox.GONE);

							}
							ismu = false;
							cb_quanxuan.setVisibility(View.GONE);
							bt_yijianshenpi.setVisibility(View.GONE);
							dataChanged();
						}
					}
				});

		lv_projects.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v("MyListViewBase", "你点击了ListView条目" + arg2);// 在LogCat中输出信息
				String jyid = baogaoGroup.get(
						listItem.get(arg2 - 1).get("ItemTitle")).getJianYanID();
				System.out.println(jyid);
				File f = new File(path + "/" + jyid + ".pdf");
				if (!f.exists()) {
					ispdf(jyid);
				} else {
					Intent intent = pdf.getPdfFileIntent(path + "/" + jyid
							+ ".pdf");
					startActivity(intent);
				}
			}
		});

		lv_projects.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// When clicked, show a toast with the TextView text
				cb_quanxuan.setVisibility(View.VISIBLE);
				bt_yijianshenpi.setVisibility(View.VISIBLE);

				for (int i = 0; i < listItem.size(); i++) {
					mAdapter.visiblebutton.put(i, Button.GONE);
					mAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
				}
				dataChanged();
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_yijianshenpi:
			//
			// ArrayList<JianYanXinXiBean> jyxxlist = new
			// ArrayList<JianYanXinXiBean>();
			JSONArray ja = new JSONArray();
			for (int i = 0; i < listItem.size(); i++) {
				if (MyAdapter.getIsSelected().get(i)) {
					JianYanXinXiBean jyxx = new JianYanXinXiBean();
					jyxx = baogaoGroup.get(listItem.get(i).get("ItemTitle"));
					jyxx.setPiZhuYuanId(user.getUserid());
					jyxx.setPiZhuRQ(GetNowDate());
					jyxx.setJianYanZT(Integer.valueOf(10));
					jyxx.setJianyanbz("审批通过||" + "审批人:" + user.getXingming()
							+ "||签字日期:" + jyxx.getPiZhuRQ());
					// jyxxlist.add(jyxx);
					JSONObject jo = JSONUtil.bean2JSONObject(jyxx);
					ja.put(jo);
					// MyAdapter.getIsSelected().put(i, false);
					// listItem.remove(i);
				}

			}
			agreeall(ja);
			break;

		default:
			break;
		}
	}

	private void agreeall(JSONArray ja) {
		// TODO Auto-generated method stub
		HttpDatas datas = new HttpDatas(user.getJigouini() + Constant.AGREEALL);

		datas.setJsonarray(ja);
		NetUtils.sendRequest(datas, this, getString(R.string.tijiao_ing),
				new TaskCallBack() {

					@Override
					public int excueHttpResponse(String respondsStr) {
						// TODO Auto-generated method stub

						int code = 0;

						try {
							JSONObject jsonObject = new JSONObject(respondsStr);
							code = jsonObject.getInt("coding");
						} catch (JSONException e) {
							// TODO Auto-generated catch
							// block
							e.printStackTrace();
						}

						return code;
					}

					@Override
					public void beforeTask() {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTask(int result) {
						// TODO Auto-generated method stub
						switch (result) {
						case Constant.RESPONSE_OK:
							if (ismu) {
								listItem.removeAll(listItem);
								tv_no_project.setVisibility(View.VISIBLE);
								lv_projects.setVisibility(View.GONE);
								cb_quanxuan.setVisibility(View.GONE);
								bt_yijianshenpi.setVisibility(View.GONE);

							} else {
								Collections.sort(MyAdapter
										.getDelContactsIdSet());// 降序
								for (int i = MyAdapter.getDelContactsIdSet()
										.size() - 1; i >= 0; i--) {

									listItem.remove((int) MyAdapter
											.getDelContactsIdSet().get(i));
									MyAdapter.getIsSelected().put(
											(int) MyAdapter
													.getDelContactsIdSet().get(
															i), false);

								}
								MyAdapter.getDelContactsIdSet().clear();
								// for (int i = 0; i < listItem.size(); i++) {
								// MyAdapter.getIsSelected().put(i, false);
								// }

							}
							// if (!ismu) {
							// mAdapter.remove();
							// }
							dataChanged();
							Toast.makeText(
									getApplicationContext(),
									ShenPiActivity.this
											.getString(R.string.tijiao_ok), 0)
									.show();

							break;
						case 403:
							Toast.makeText(
									getApplicationContext(),
									ShenPiActivity.this
											.getString(R.string.tijiao_error),
									0).show();

							break;

						default:
							showResulttoast(result, getApplicationContext());
							break;
						}

					}
				});
	}

	private void ispdf(String jyid) {

		HttpDatas datas = new HttpDatas(user.getJigouini() + Constant.ISPDF);
		datas.putParam("jyid", jyid);
		NetUtils.sendRequest(datas, this, new TaskCallBack() {

			@Override
			public int excueHttpResponse(String respondsStr) {
				// TODO Auto-generated method stub
				int code = 0;
				try {
					JSONObject jsonObject = new JSONObject(respondsStr);
					code = jsonObject.getInt("coding");
					filename = jsonObject.getString("msg");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return code;
			}

			@Override
			public void beforeTask() {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTask(int result) {
				// TODO Auto-generated method stub
				switch (result) {
				case Constant.RESPONSE_OK:

					String pdfini = user.getJigouini() + Constant.DOWNLOAD
							+ "?fileName=" + filename;
					// pdf.fileDownLoad(pdfini, path, filename,
					// getApplicationContext(), pd);
					pdf.downLoadpdf(pdfini, path, filename);
					// new Thread(downloadRun).start();
					break;
				case 403:
					Toast.makeText(getApplicationContext(),
							ShenPiActivity.this.getString(R.string.pdf_null), 0)
							.show();

					break;

				default:
					Toast.makeText(getApplicationContext(),
							ShenPiActivity.this.getString(R.string.pdf_null), 0)
							.show();
					break;
				}
			}
		});
	}

	// Runnable downloadRun = new Runnable(){
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// File fileDirect = Environment.getExternalStorageDirectory();
	// String path = fileDirect.getPath() + "/jypdf";
	// String pdfini=user.getJigouini()+Constant.DOWNLOAD+"/"+filename;
	// PDFUltil.fileDownLoad(pdfini, path, filename,
	// getApplicationContext(), pd);
	// }
	// };
	private ArrayList<HashMap<String, Object>> getDate() {
		listItem = new ArrayList<HashMap<String, Object>>();
		/* 为动态数组添加数据 */
		for (int i = 0; i < baogaolist.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", R.drawable.itemimage);
			map.put("ItemTitle", baogaolist.get(i).get("ItemTitle"));
			map.put("ItemText", baogaolist.get(i).get("ItemText"));

			listItem.add(map);
		}
		return listItem;

	}

	@Override
	public void doReback() {
		// TODO Auto-generated method stub

		baogaolist.clear();
		baogaoGroup.clear();
		finish();
	}

	// 刷新listview和TextView的显示
	private void dataChanged() {
		// 通知listView刷新
		mAdapter.notifyDataSetChanged();
		// TextView显示最新的选中数目

	}

	public static String GetNowDate() {
		String temp_str = "";
		Date day = new Date();

		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		temp_str = sdf.format(day);
		return temp_str;
	}

	static class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
		private static HashMap<Integer, Boolean> isSelected;
		private static ArrayList<HashMap<String, Object>> list;
		private static List<Integer> delContactsIdSet;// 多选时选择的列表
		ViewHolder holder;
		public HashMap<Integer, Integer> visiblebutton;// 用来记录是否显示button
		public HashMap<Integer, Integer> visiblecheck;// 用来记录是否显示checkBox

		public static ArrayList<HashMap<String, Object>> getList() {
			return list;
		}

		public static void setList(ArrayList<HashMap<String, Object>> list) {
			MyAdapter.list = list;
		}

		public static List<Integer> getDelContactsIdSet() {
			return delContactsIdSet;
		}

		public static void setDelContactsIdSet(List<Integer> delContactsIdSet) {
			MyAdapter.delContactsIdSet = delContactsIdSet;
		}

		public static HashMap<Integer, Boolean> getIsSelected() {
			return isSelected;
		}

		public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
			MyAdapter.isSelected = isSelected;
		}

		/* 构造函数 */
		public MyAdapter(ArrayList<HashMap<String, Object>> list,
				Context context) {
			this.list = list;
			this.mInflater = LayoutInflater.from(context);
			isSelected = new HashMap<Integer, Boolean>();
			delContactsIdSet = new ArrayList<Integer>();
			visiblebutton = new HashMap<Integer, Integer>();
			visiblecheck = new HashMap<Integer, Integer>();
			for (int i = 0; i < list.size(); i++) {
				getIsSelected().put(i, false);
				visiblebutton.put(i, Button.VISIBLE);
				visiblecheck.put(i, CheckBox.GONE);
			}

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			// 观察convertView随ListView滚动情况
			Log.v("MyListViewBase", "getView " + position + " " + convertView);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.bg_listview, null);
				holder = new ViewHolder();
				/* 得到各个控件的对象 */
				holder.image = (ImageView) convertView
						.findViewById(R.id.itemImage);
				holder.title = (TextView) convertView
						.findViewById(R.id.itemTitle);
				holder.text = (TextView) convertView
						.findViewById(R.id.itemText);
				holder.bt = (Button) convertView.findViewById(R.id.itemButton);
				holder.cb = (CheckBox) convertView.findViewById(R.id.itemcb);
				holder.pb = (ProgressBar) convertView
						.findViewById(R.id.download_progressBar);

				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			/* 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			holder.image.setImageResource(R.drawable.itemimage);
			holder.title
					.setText(list.get(position).get("ItemTitle").toString());
			holder.text.setText(list.get(position).get("ItemText").toString());
			holder.bt.setVisibility(visiblebutton.get(position));
			holder.cb.setVisibility(visiblecheck.get(position));
			// 根据isSelected来设置checkbox的选中状况
			holder.cb.setChecked(getIsSelected().get(position));

			/* 为Button添加点击事件 */
			holder.bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.v("MyListViewBase", "你点击了按钮" + position); // 打印Button的点击信息
					final JianYanXinXiBean jy = baogaoGroup.get(list.get(
							position).get("ItemTitle"));

					// showpopupmenu(position);
					PopupMenu popup = new PopupMenu(mInflater.getContext(), v);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.main,
							popup.getMenu());

					popup.show();

					popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
							case R.id.pizhun:

								jy.setPiZhuYuanId(user.getUserid());
								jy.setPiZhuRQ(GetNowDate());
								jy.setJianYanZT(Integer.valueOf(10));
								jy.setJianyanbz("审批通过||" + "审批人:"
										+ user.getXingming() + "||签字日期:"
										+ jy.getPiZhuRQ());

								agree(jy, position);

								return true;
							case R.id.tuihui:

								jy.setPiZhuYuanId(user.getUserid());
								jy.setPiZhuRQ(GetNowDate());
								jy.setJianYanZT(Integer.valueOf(6));
								jy.setRollbackzt(Integer.valueOf(8));
								jy.setJianyanbz("审批拒签！！||" + "理由：其它||审批人:"
										+ user.getXingming() + "||签字日期:"
										+ jy.getPiZhuRQ());
								rollback(jy, position);
								return true;
							default:
								return false;
							}

						}
					});
				}
			});

			holder.cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 改变CheckBox的状态 ,如果全选状态下，勾选上
					if (getIsSelected().get(position)) {
						delContactsIdSet.add(position);
						System.out.println(delContactsIdSet);
					} else {
						delContactsIdSet.remove((Integer) position);
						System.out.println(delContactsIdSet);
					}

				}
			});
			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						getIsSelected().put(position, true);
					} else {
						getIsSelected().put(position, false);
					}

				}
			});
			return convertView;
		}

		// public void remove() {
		//
		// for (int i = 0; i < list.size(); i++) {
		// if (getIsSelected().get(i)) {
		// getIsSelected().put(i, false);
		// list.remove(i);
		// }
		//
		// }
		//
		// }

		@Override
		public int getCount() {

			return list.size();// 返回数组的长度
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public final class ViewHolder {
			public ImageView image;
			public TextView title;
			public TextView text;
			public Button bt;
			public CheckBox cb;
			public ProgressBar pb;
		}

		public void agree(JianYanXinXiBean jy, final int s) {
			HttpDatas datas = new HttpDatas(user.getJigouini() + Constant.AGREE);
			datas.setJsonObject(JSONUtil.bean2JSONObject(jy));
			NetUtils.sendRequest(datas, mInflater.getContext(), mInflater
					.getContext().getString(R.string.tijiao_ing),
					new TaskCallBack() {

						@Override
						public void beforeTask() {
							// TODO Auto-generated method
							// stub

						}

						@Override
						public int excueHttpResponse(String respondsStr) {
							// TODO Auto-generated method
							// stub
							int code = 0;
							System.out.println(respondsStr);

							try {
								JSONObject jsonObject = new JSONObject(
										respondsStr);
								code = jsonObject.getInt("coding");
							} catch (JSONException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}

							return code;
						}

						@Override
						public void afterTask(int result) {
							// TODO Auto-generated method
							// stub
							switch (result) {
							case Constant.RESPONSE_OK:
								listItem.remove(s);
								notifyDataSetChanged();
								Toast.makeText(
										mInflater.getContext(),
										mInflater.getContext().getString(
												R.string.tijiao_ok), 0).show();

								break;
							case 403:
								Toast.makeText(
										mInflater.getContext(),
										mInflater.getContext().getString(
												R.string.tijiao_error), 0)
										.show();

								break;

							default:
								showResulttoast(result, mInflater.getContext());
								break;
							}
						}

					});
		}

		public void rollback(JianYanXinXiBean jy, final int s) {
			HttpDatas datas = new HttpDatas(user.getJigouini()
					+ Constant.ROLLBACK);

			datas.setJsonObject(JSONUtil.bean2JSONObject(jy));
			NetUtils.sendRequest(datas, mInflater.getContext(), mInflater
					.getContext().getString(R.string.tijiao_ing),
					new TaskCallBack() {

						@Override
						public void beforeTask() {
							// TODO Auto-generated method
							// stub

						}

						@Override
						public int excueHttpResponse(String respondsStr) {
							// TODO Auto-generated method
							// stub
							int code = 0;
							System.out.println(respondsStr);

							try {
								JSONObject jsonObject = new JSONObject(
										respondsStr);
								code = jsonObject.getInt("coding");
							} catch (JSONException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}

							return code;
						}

						@Override
						public void afterTask(int result) {
							// TODO Auto-generated method
							// stub
							switch (result) {
							case Constant.RESPONSE_OK:
								listItem.remove(s);
								notifyDataSetChanged();
								Toast.makeText(
										mInflater.getContext(),
										mInflater.getContext().getString(
												R.string.tijiao_ok), 0).show();

								break;
							case 403:
								Toast.makeText(
										mInflater.getContext(),
										mInflater.getContext().getString(
												R.string.tijiao_error), 0)
										.show();

								break;

							default:
								showResulttoast(result, mInflater.getContext());
								break;
							}
						}

					});
		}

	}

}
