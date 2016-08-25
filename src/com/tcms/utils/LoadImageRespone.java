package com.tcms.utils;

import java.io.File;

import android.graphics.Bitmap;

/**
 * @ClassName: LoadImageRespone
 * @Description: 封装下载图片后成功后 生成的Bitmap 和文件全路径（如果需要保存）
 * @author 朱恒章
 * @date 2016-5-17 上午11:39:06
 * @package_name 路径：com.tcms.utils
 */
public class LoadImageRespone {
	private Bitmap bitmap;// 图片的bitmap
	private File iamgeFile;// 保存路径 如果要保存的话

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
