package com.tcms.utils;

import java.io.File;

import android.graphics.Bitmap;

/**
 * @ClassName: LoadImageRespone
 * @Description: ��װ����ͼƬ��ɹ��� ���ɵ�Bitmap ���ļ�ȫ·���������Ҫ���棩
 * @author �����
 * @date 2016-5-17 ����11:39:06
 * @package_name ·����com.tcms.utils
 */
public class LoadImageRespone {
	private Bitmap bitmap;// ͼƬ��bitmap
	private File iamgeFile;// ����·�� ���Ҫ����Ļ�

	public LoadImageRespone(Bitmap bitmap, File iamgeFile) {
		super();
		this.bitmap = bitmap;
		this.iamgeFile = iamgeFile;
	}

	public LoadImageRespone() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoadImageRespone(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public File getFileSavePath() {
		return iamgeFile;
	}

	public void setFileSavePath(File iamgeFile) {
		this.iamgeFile = iamgeFile;
	}

}
