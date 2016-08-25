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
 * @author �����
 * @date 2016-7-27 ����10:24:13
 * @package_name ·����com.tcms.activity
 */
public class ShenPiActivity extends BaseActivity implements OnClickListener {

	private ListView lv_projects;// ����list
	private TextView tv_no_project;// û�п���������
	private CheckBox cb_quanxuan;// ȫѡ��
	private Button bt_yijianshenpi;// ���������ť
	private Button bt_previous;
	private Button bt_next;// ��һ��
	private TextView tvTitle;// ����
	private static UserInfo user;
	private static Boolean ismu = false;// �Ƿ�ȫѡ
	private static String filename = null;

	private static ArrayList<HashMap<String, Object>> listItem;
	private MyAdapter mAdapter;
	private static String path;// �ļ�·��
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
		mAdapter = new MyAdapter(getDate(), this);// �õ�һ��MyAdapter����
		lv_projects.setAdapter(mAdapter);// ΪListView��Adapter

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
							// ������Ϊlist�ĳ���

							// ˢ��listview��TextView����ʾ
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
				Log.v("MyListViewBase", "������ListView��Ŀ" + arg2);// ��LogCat�������Ϣ
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
					jyxx.setJianyanbz("����ͨ��||" + "������:" + user.getXingming()
							+ "||ǩ������:" + jyxx.getPiZhuRQ());
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
										.getDelContactsIdSet());// ����
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
		/* Ϊ��̬����������� */
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

	// ˢ��listview��TextView����ʾ
	private void dataChanged() {
		// ֪ͨlistViewˢ��
		mAdapter.notifyDataSetChanged();
		// TextView��ʾ���µ�ѡ����Ŀ

	}

	public static String GetNowDate() {
		String temp_str = "";
		Date day = new Date();

		// ����aa��ʾ�����硱�����硱 HH��ʾ24Сʱ�� �������hh��ʾ12Сʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		temp_str = sdf.format(day);
		return temp_str;
	}

	static class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;// �õ�һ��LayoutInfalter�����������벼��
		private static HashMap<Integer, Boolean> isSelected;
		private static ArrayList<HashMap<String, Object>> list;
		private static List<Integer> delContactsIdSet;// ��ѡʱѡ����б�
		ViewHolder holder;
		public HashMap<Integer, Integer> visiblebutton;// ������¼�Ƿ���ʾbutton
		public HashMap<Integer, Integer> visiblecheck;// ������¼�Ƿ���ʾcheckBox

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

		/* ���캯�� */
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

			// �۲�convertView��ListView�������
			Log.v("MyListViewBase", "getView " + position + " " + convertView);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.bg_listview, null);
				holder = new ViewHolder();
				/* �õ������ؼ��Ķ��� */
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

				convertView.setTag(holder);// ��ViewHolder����
			} else {
				holder = (ViewHolder) convertView.getTag();// ȡ��ViewHolder����
			}
			/* ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
			holder.image.setImageResource(R.drawable.itemimage);
			holder.title
					.setText(list.get(position).get("ItemTitle").toString());
			holder.text.setText(list.get(position).get("ItemText").toString());
			holder.bt.setVisibility(visiblebutton.get(position));
			holder.cb.setVisibility(visiblecheck.get(position));
			// ����isSelected������checkbox��ѡ��״��
			holder.cb.setChecked(getIsSelected().get(position));

			/* ΪButton��ӵ���¼� */
			holder.bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.v("MyListViewBase", "�����˰�ť" + position); // ��ӡButton�ĵ����Ϣ
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
								jy.setJianyanbz("����ͨ��||" + "������:"
										+ user.getXingming() + "||ǩ������:"
										+ jy.getPiZhuRQ());

								agree(jy, position);

								return true;
							case R.id.tuihui:

								jy.setPiZhuYuanId(user.getUserid());
								jy.setPiZhuRQ(GetNowDate());
								jy.setJianYanZT(Integer.valueOf(6));
								jy.setRollbackzt(Integer.valueOf(8));
								jy.setJianyanbz("������ǩ����||" + "���ɣ�����||������:"
										+ user.getXingming() + "||ǩ������:"
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
					// �ı�CheckBox��״̬ ,���ȫѡ״̬�£���ѡ��
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

			return list.size();// ��������ĳ���
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
