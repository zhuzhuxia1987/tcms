package com.tcms.view;

import com.tcms.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: CustomDialog
 * @Description: �Զ���һ�������ĶԻ���
 * @author �����
 * @date 2016-5-17 ����11:39:46
 * @package_name ·����com.tcms.view
 */
public class CustomDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private ButtonRespond respond;

	private TextView tvDialogtitle;// �Ի������
	private TextView tvDialogMassage;// �Ի�����Ϣ
	private Button btLeft;// ��߰�ť
	private Button btRight;// �ұ߰�ť
	private LinearLayout llFreame;// �տ�view����

	public CustomDialog(Context context, ButtonRespond butt) {
		super(context, R.style.custom_dialog);
		this.context = context;
		this.respond = butt;
		setContentView(R.layout.custom_dialog);
		initView();
		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// WindowManager m = (WindowManager)
		// context.getSystemService(Context.WINDOW_SERVICE);

		// Display d = m.getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes();

		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.CENTER);

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}

	/**
	 * �����ʼ��
	 */
	private void initView() {

		tvDialogtitle = (TextView) this.findViewById(R.id.tv_dialog_title);
		tvDialogMassage = (TextView) this.findViewById(R.id.tv_dialog_massage);
		llFreame = (LinearLayout) this.findViewById(R.id.ll_frame);
		btLeft = (Button) this.findViewById(R.id.bt_left);
		btLeft.setOnClickListener(this);
		btRight = (Button) this.findViewById(R.id.bt_right);
		btRight.setOnClickListener(this);
		tvDialogMassage
				.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	/**
	 * ���öԻ������
	 * 
	 * @param resouseId
	 *            ��Դid
	 */
	public void setDialogTitle(int resouceId) {
		tvDialogtitle.setText(resouceId);
	}

	/**
	 * ���öԻ������
	 * 
	 * @param titleStr
	 *            ����
	 */
	public void setDialogTitle(String titleStr) {
		tvDialogtitle.setText(titleStr);
	}

	/**
	 * ���öԻ�����ʾ��Ϣ
	 * 
	 * @param resouceID
	 *            ��ԴID
	 */
	public void setDialogMassage(int resouceID) {
		tvDialogMassage.setText(resouceID);
	}

	/**
	 * ���öԻ�����ʾ��Ϣ
	 * 
	 * @param massage
	 *            ��ʾ��Ϣ
	 */
	public void setDialogMassage(String massage) {
		tvDialogMassage.setText(massage);
	}

	/**
	 * ������߰�ť����
	 * 
	 * @param resouceId
	 *            ��Դid
	 */
	public void setLeftButtonText(int resouceId) {
		btLeft.setText(resouceId);
	}

	/**
	 * ������߰�ť����
	 * 
	 * @param text
	 *            ��ť����
	 */
	public void setLeftButtonText(String text) {
		btLeft.setText(text);
	}

	/**
	 * �����ұ߰�ť����
	 * 
	 * @param resuoceId
	 *            ��Դid
	 */
	public void setRightButtonText(int resuoceId) {
		btRight.setText(resuoceId);
	}

	/**
	 * �����ұ߰�ť����
	 * 
	 * @param text
	 *            ��ť����
	 */
	public void setRightButtonText(String text) {
		btRight.setText(text);
	}

	/**
	 * ����������Ϣ�����Ƿ�ɼ�
	 * 
	 * @param visibility
	 */
	public void setMagssageViewVisibility(int visibility) {
		tvDialogMassage.setVisibility(visibility);
	}

	/**
	 * ��ܽ����Ƿ�ɼ�
	 * 
	 * @param visibility
	 */
	public void setFrameViewVisibility(int visibility) {
		llFreame.setVisibility(visibility);
	}

	/**
	 * ���տ�LinearLayout��Ӳ����ļ�
	 * 
	 * @param view
	 */
	public void addView2Frame(View view) {
		llFreame.addView(view, 0);
	}

	/**
	 * ������߰�ť�ı���
	 * 
	 * @param resid
	 */
	public void setLeftButonBackgroud(int resid) {
		btLeft.setBackgroundResource(resid);
	}

	/**
	 * �����ұ߰�ť�ı���
	 * 
	 * @param resid
	 */
	public void setRightButonBackgroud(int resid) {
		btRight.setBackgroundResource(resid);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_left:
			respond.buttonLeftRespond();
			break;
		case R.id.bt_right:
			respond.buttonRightRespond();
			break;
		default:
			break;
		}

	}

	public interface ButtonRespond {
		public void buttonLeftRespond();

		public void buttonRightRespond();

	}
}
