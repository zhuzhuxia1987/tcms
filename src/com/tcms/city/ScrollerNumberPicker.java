package com.tcms.city;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tcms.R;

/**
 * 
 * ����ѡ��
 * 
 * @author zihao
 * 
 */
public class ScrollerNumberPicker extends View {
	/** �ؼ���� */
	private float controlWidth;
	/** �ؼ��߶� */
	private float controlHeight;
	/** �Ƿ񻬶��� */
	private boolean isScrolling = false;
	/** ѡ������� */
	private ArrayList<ItemObject> itemList = new ArrayList<ScrollerNumberPicker.ItemObject>();
	/** �������� */
	private ArrayList<String> dataList = new ArrayList<String>();
	/** ���µ����� */
	private int downY;
	/** ���µ�ʱ�� */
	private long downTime = 0;
	/** �̴��ƶ� */
	private long goonTime = 200;
	/** �̴��ƶ����� */
	private int goonDistence = 100;
	/** ���߻��� */
	private Paint linePaint;
	/** �ߵ�Ĭ����ɫ */
	private int lineColor = 0xff000000;
	/** Ĭ������ */
	private float normalFont = 14.0f;
	/** ѡ�е�ʱ������ */
	private float selectedFont = 22.0f;
	/** ��Ԫ��߶� */
	private int unitHeight = 50;
	/** ��ʾ���ٸ����� */
	private int itemNumber = 7;
	/** Ĭ��������ɫ */
	private int normalColor = 0xff000000;
	/** ѡ��ʱ���������ɫ */
	private int selectedColor = 0xffff0000;
	/** �ɰ�߶� */
	private float maskHight = 48.0f;
	/** ѡ����� */
	private OnSelectListener onSelectListener;
	/** �Ƿ���� */
	private boolean isEnable = true;
	/** ˢ�½��� */
	private static final int REFRESH_VIEW = 0x001;
	/** �ƶ����� */
	private static final int MOVE_NUMBER = 5;
	/** �Ƿ�����ѡ�� */
	private boolean noEmpty = false;
	
	/** �����޸����ݣ�����ConcurrentModificationException�쳣 */
	private boolean isClearing = false;

	public ScrollerNumberPicker(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
		initData();
	}

	public ScrollerNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
		initData();
	}

	public ScrollerNumberPicker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initData();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (!isEnable)
			return true;
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isScrolling = true;
			downY = (int) event.getY();
			downTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			actionMove(y - downY);
			onSelectListener();
			break;
		case MotionEvent.ACTION_UP:

			// �ƶ�����ľ���ֵ
			int move = (y - downY);
			move = move > 0 ? move : move * (-1);
			// �ж϶�ʱ���ƶ��ľ���
			if (System.currentTimeMillis() - downTime < goonTime
					&& move > goonDistence) {
				goonMove(y - downY);
			} else {
				actionUp(y - downY);
			}
			noEmpty();
			isScrolling = false;
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		drawLine(canvas);
		drawList(canvas);
		drawMask(canvas);
	}

	private synchronized void drawList(Canvas canvas) {
		if (isClearing)
			return;
		try {
			for (ItemObject itemObject : itemList) {
				itemObject.drawSelf(canvas);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		controlWidth = getWidth();
		if (controlWidth != 0) {
			setMeasuredDimension(getWidth(), itemNumber * unitHeight);
			controlWidth = getWidth();
		}

	}

	/**
	 * �����ƶ�һ������
	 */
	private synchronized void goonMove(final int move) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int distence = 0;
				while (distence < unitHeight * MOVE_NUMBER) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					actionThreadMove(move > 0 ? distence : distence * (-1));
					distence += 10;

				}
				actionUp(move > 0 ? distence - 10 : distence * (-1) + 10);
				noEmpty();
			}
		}).start();
	}

	/**
	 * ����Ϊ�գ�������ѡ��
	 */
	private void noEmpty() {
		if (!noEmpty)
			return;
		for (ItemObject item : itemList) {
			if (item.isSelected())
				return;
		}
		int move = (int) itemList.get(0).moveToSelected();
		if (move < 0) {
			defaultMove(move);
		} else {
			defaultMove((int) itemList.get(itemList.size() - 1)
					.moveToSelected());
		}
		for (ItemObject item : itemList) {
			if (item.isSelected()) {
				if (onSelectListener != null)
					onSelectListener.endSelect(item.id, item.itemText);
				break;
			}
		}
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		isClearing = true;
		itemList.clear();
		for (int i = 0; i < dataList.size(); i++) {
			ItemObject itmItemObject = new ItemObject();
			itmItemObject.id = i;
			itmItemObject.itemText = dataList.get(i);
			itmItemObject.x = 0;
			itmItemObject.y = i * unitHeight;
			itemList.add(itmItemObject);
		}
		isClearing = false;

	}

	/**
	 * �ƶ���ʱ��
	 * 
	 * @param move
	 */
	private void actionMove(int move) {
		for (ItemObject item : itemList) {
			item.move(move);
		}
		invalidate();
	}

	/**
	 * �ƶ����߳��е���
	 * 
	 * @param move
	 */
	private void actionThreadMove(int move) {
		for (ItemObject item : itemList) {
			item.move(move);
		}
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);
	}

	/**
	 * �ɿ���ʱ��
	 * 
	 * @param move
	 */
	private void actionUp(int move) {
		int newMove = 0;
		if (move > 0) {
			for (int i = 0; i < itemList.size(); i++) {
				if (itemList.get(i).isSelected()) {
					newMove = (int) itemList.get(i).moveToSelected();
					if (onSelectListener != null)
						onSelectListener.endSelect(itemList.get(i).id,
								itemList.get(i).itemText);
					break;
				}
			}
		} else {
			for (int i = itemList.size() - 1; i >= 0; i--) {
				if (itemList.get(i).isSelected()) {
					newMove = (int) itemList.get(i).moveToSelected();
					if (onSelectListener != null)
						onSelectListener.endSelect(itemList.get(i).id,
								itemList.get(i).itemText);
					break;
				}
			}
		}
		for (ItemObject item : itemList) {
			item.newY(move + 0);
		}
		slowMove(newMove);
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);

	}

	/**
	 * �����ƶ�
	 * 
	 * @param move
	 */
	private synchronized void slowMove(final int move) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// �ж�����
				int m = move > 0 ? move : move * (-1);
				int i = move > 0 ? 1 : (-1);
				// �ƶ��ٶ�
				int speed = 1;
				while (true) {
					m = m - speed;
					if (m <= 0) {
						for (ItemObject item : itemList) {
							item.newY(m * i);
						}
						Message rMessage = new Message();
						rMessage.what = REFRESH_VIEW;
						handler.sendMessage(rMessage);
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					for (ItemObject item : itemList) {
						item.newY(speed * i);
					}
					Message rMessage = new Message();
					rMessage.what = REFRESH_VIEW;
					handler.sendMessage(rMessage);
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (ItemObject item : itemList) {
					if (item.isSelected()) {
						if (onSelectListener != null)
							onSelectListener.endSelect(item.id, item.itemText);
						break;
					}
				}

			}
		}).start();
	}

	/**
	 * �ƶ���Ĭ��λ��
	 * 
	 * @param move
	 */
	private void defaultMove(int move) {
		for (ItemObject item : itemList) {
			item.newY(move);
		}
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);
	}

	/**
	 * ��������
	 */
	private void onSelectListener() {
		if (onSelectListener == null)
			return;
		for (ItemObject item : itemList) {
			if (item.isSelected()) {
				onSelectListener.selecting(item.id, item.itemText);
			}
		}
	}

	/**
	 * ��������
	 * 
	 * @param canvas
	 */
	private void drawLine(Canvas canvas) {

		if (linePaint == null) {
			linePaint = new Paint();
			linePaint.setColor(lineColor);
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(1f);
		}

		canvas.drawLine(0, controlHeight / 2 - unitHeight / 2 + 2,
				controlWidth, controlHeight / 2 - unitHeight / 2 + 2, linePaint);
		canvas.drawLine(0, controlHeight / 2 + unitHeight / 2 - 2,
				controlWidth, controlHeight / 2 + unitHeight / 2 - 2, linePaint);
	}

	/**
	 * �����ڸǰ�
	 * 
	 * @param canvas
	 */
	private void drawMask(Canvas canvas) {
		LinearGradient lg = new LinearGradient(0, 0, 0, maskHight, 0x00f2f2f2,
				0x00f2f2f2, TileMode.MIRROR);
		Paint paint = new Paint();
		paint.setShader(lg);
		canvas.drawRect(0, 0, controlWidth, maskHight, paint);

		LinearGradient lg2 = new LinearGradient(0, controlHeight - maskHight,
				0, controlHeight, 0x00f2f2f2, 0x00f2f2f2, TileMode.MIRROR);
		Paint paint2 = new Paint();
		paint2.setShader(lg2);
		canvas.drawRect(0, controlHeight - maskHight, controlWidth,
				controlHeight, paint2);

	}

	/**
	 * ��ʼ������ȡ���õ�����
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		TypedArray attribute = context.obtainStyledAttributes(attrs,
				R.styleable.NumberPicker);
		unitHeight = (int) attribute.getDimension(
				R.styleable.NumberPicker_unitHight, 32);
		normalFont = attribute.getDimension(
				R.styleable.NumberPicker_normalTextSize, 14.0f);
		selectedFont = attribute.getDimension(
				R.styleable.NumberPicker_selecredTextSize, 22.0f);
		itemNumber = attribute.getInt(R.styleable.NumberPicker_itemNumber, 7);
		normalColor = attribute.getColor(
				R.styleable.NumberPicker_normalTextColor, 0xff000000);
		selectedColor = attribute.getColor(
				R.styleable.NumberPicker_selecredTextColor, 0xffff0000);
		lineColor = attribute.getColor(R.styleable.NumberPicker_lineColor,
				0xff000000);
		maskHight = attribute.getDimension(
				R.styleable.NumberPicker_maskHight, 48.0f);
		noEmpty = attribute.getBoolean(R.styleable.NumberPicker_noEmpty,
				false);
		isEnable = attribute.getBoolean(R.styleable.NumberPicker_isEnable,
				true);
		attribute.recycle();

		controlHeight = itemNumber * unitHeight;

	}

	/**
	 * ��������
	 * 
	 * @param data
	 */
	public void setData(ArrayList<String> data) {
		this.dataList = data;
		initData();
	}

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public int getSelected() {
		for (ItemObject item : itemList) {
			if (item.isSelected())
				return item.id;
		}
		return -1;
	}

	/**
	 * ��ȡ���ص�����
	 * 
	 * @return
	 */
	public String getSelectedText() {
		for (ItemObject item : itemList) {
			if (item.isSelected())
				return item.itemText;
		}
		return "";
	}

	/**
	 * �Ƿ����ڻ���
	 * 
	 * @return
	 */
	public boolean isScrolling() {
		return isScrolling;
	}

	/**
	 * �Ƿ����
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return isEnable;
	}

	/**
	 * �����Ƿ����
	 * 
	 * @param isEnable
	 */
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	/**
	 * ����Ĭ��ѡ��
	 * 
	 * @param index
	 */
	public void setDefault(int index) {
		float move = itemList.get(index).moveToSelected();
		defaultMove((int) move);
	}

	/**
	 * ��ȡ�б��С
	 * 
	 * @return
	 */
	public int getListSize() {
		if (itemList == null)
			return 0;
		return itemList.size();
	}

	/**
	 * ��ȡĳ�������
	 * 
	 * @param index
	 * @return
	 */
	public String getItemText(int index) {
		if (itemList == null)
			return "";
		return itemList.get(index).itemText;
	}

	/**
	 * ����
	 * 
	 * @param onSelectListener
	 */
	public void setOnSelectListener(OnSelectListener onSelectListener) {
		this.onSelectListener = onSelectListener;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				invalidate();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * ��������
	 * 
	 * @author zoudong
	 */
	private class ItemObject {
		/** id */
		public int id = 0;
		/** ���� */
		public String itemText = "";
		/** x���� */
		public int x = 0;
		/** y���� */
		public int y = 0;
		/** �ƶ����� */
		public int move = 0;
		/** ���廭�� */
		private Paint textPaint;
		/** ���巶Χ���� */
		private Rect textRect;

		public ItemObject() {
			super();
		}

		/**
		 * ��������
		 * 
		 * @param canvas
		 */
		public void drawSelf(Canvas canvas) {

			if (textPaint == null) {
				textPaint = new Paint();
				textPaint.setAntiAlias(true);
			}

			if (textRect == null)
				textRect = new Rect();

			// �ж��Ƿ�ѡ��
			if (isSelected()) {
				textPaint.setColor(selectedColor);
				// ��ȡ�����׼λ�õľ���
				float moveToSelect = moveToSelected();
				moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
						* (-1);
				// ���㵱ǰ�����С
				float textSize = (float) normalFont
						+ ((float) (selectedFont - normalFont) * (1.0f - (float) moveToSelect
								/ (float) unitHeight));
				textPaint.setTextSize(textSize);
			} else {
				textPaint.setColor(normalColor);
				textPaint.setTextSize(normalFont);
			}

			// ���ذ�Χ�����ַ�������С��һ��Rect����
			textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
			// �ж��Ƿ����
			if (!isInView())
				return;

			// ��������
			canvas.drawText(itemText, x + controlWidth / 2 - textRect.width()
					/ 2, y + move + unitHeight / 2 + textRect.height() / 2,
					textPaint);

		}

		/**
		 * �Ƿ��ڿ��ӽ�����
		 * 
		 * @param rect
		 * @return
		 */
		public boolean isInView() {
			if (y + move > controlHeight
					|| (y + move + unitHeight / 2 + textRect.height() / 2) < 0)
				return false;
			return true;
		}

		/**
		 * �ƶ�����
		 * 
		 * @param _move
		 */
		public void move(int _move) {
			this.move = _move;
		}

		/**
		 * �����µ�����
		 * 
		 * @param move
		 */
		public void newY(int _move) {
			this.move = 0;
			this.y = y + _move;
		}

		/**
		 * �ж��Ƿ���ѡ��������
		 * 
		 * @return
		 */
		public boolean isSelected() {
			if ((y + move) >= controlHeight / 2 - unitHeight / 2 + 2
					&& (y + move) <= controlHeight / 2 + unitHeight / 2 - 2)
				return true;
			if ((y + move + unitHeight) >= controlHeight / 2 - unitHeight / 2
					+ 2
					&& (y + move + unitHeight) <= controlHeight / 2
							+ unitHeight / 2 - 2)
				return true;
			if ((y + move) <= controlHeight / 2 - unitHeight / 2 + 2
					&& (y + move + unitHeight) >= controlHeight / 2
							+ unitHeight / 2 - 2)
				return true;
			return false;
		}

		/**
		 * ��ȡ�ƶ�����׼λ����Ҫ�ľ���
		 */
		public float moveToSelected() {
			return (controlHeight / 2 - unitHeight / 2) - (y + move);
		}
	}

	/**
	 * ѡ���������
	 * 
	 * @author zoudong
	 * 
	 */
	public interface OnSelectListener {

		/**
		 * ����ѡ��
		 * 
		 * @param id
		 * @param text
		 */
		public void endSelect(int id, String text);

		/**
		 * ѡ�е�����
		 * 
		 * @param id
		 * @param text
		 */
		public void selecting(int id, String text);

	}
}
