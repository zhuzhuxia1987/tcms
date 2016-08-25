package com.tcms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * @ClassName: FileUitls
 * @Description: ����File����Ĺ���
 * @author �����
 * @date 2016-5-17 ����11:37:37
 * @package_name ·����com.tcms.utils
 */
public class FileUitls {

	/**
	 * �ж�Sd���Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isSdCardUsed(Context context) {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String getSDCardPath(Context context) {
		String path;
		if (isSdCardUsed(context)) {
			path = Environment.getExternalStorageDirectory().getPath();
		} else {
			path = Environment.getRootDirectory().getPath();
		}
		return path;
	}

	/**
	 * ��sd���´���һ��Ŀ¼����
	 * 
	 * @param context
	 * @param directoryName
	 *            �µ�Ŀ¼����
	 * @return
	 */
	public static File createDirectory(Context context, String directoryName) {
		String path;
		if (isSdCardUsed(context)) {
			path = Environment.getExternalStorageDirectory() + "/"
					+ directoryName;
		} else {
			path = Environment.getRootDirectory() + "/" + directoryName;
		}

		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				return file;
			}
		}
		return file;
	}

	/**
	 * ��sd���µõ�һ��ͼƬ��bitmap
	 * 
	 * @param fileName
	 *            sd���µ�������ƣ�sd����Ŀ¼���üӽ�ȥ��
	 * @return
	 */
	public static Bitmap getBitmapfromSDPathByName(Context context,
			String fileName) {
		File file = new File(getSDCardPath(context) + "/" + fileName);
		Bitmap bitmap = null;
		if (file.exists()) {
			bitmap = BitmapFactory.decodeFile(file.getPath());
		}
		return bitmap;
	}

	/**
	 * ���ļ�������Bitmap ����
	 * 
	 * @param filePath
	 *            ȫ·��
	 * @return
	 */
	public static Bitmap getBitmapfromFile(String filePath) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;

	}

	// �ݹ�
	public static long getFilesZise(File f) throws Exception// ȡ���ļ��д�С
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFilesZise(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static int getFilesNum(File direct) {
		int num = 0;
		if (direct == null || !direct.isDirectory() || !direct.exists()) {
			return 0;
		} else {

			File flist[] = direct.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					num = num + getFilesNum(flist[i]);
				} else {
					num++;
				}
			}
			return num;
		}
	}

	/**
	 * ɾ���ļ� ����ļ�����ĳ����
	 * 
	 * @param direct
	 *            Ŀ¼
	 * @param maxFiles
	 *            ��������ļ�����
	 * @param maxSize
	 *            �ļ��д�С ��λ�ֽ�
	 * @param deletfileNum
	 *            ɾ���ļ�����
	 */
	public static void deleteOldfiles(File direct, long maxFiles, long maxSize,
			long deletfileNum) {
		try {
			if (getFilesNum(direct) > maxFiles
					|| getFilesZise(direct) > maxSize) {
				HashMap<Long, File> fileMap = new HashMap<Long, File>();
				getSavetime(direct, fileMap);
				Set<Long> keyset = fileMap.keySet();
				ArrayList<Long> list = new ArrayList<Long>(keyset);
				Collections.sort(list, new Comparator<Long>() {
					@Override
					public int compare(Long lhs, Long rhs) {
						// TODO Auto-generated method stub
						return lhs.compareTo(rhs);
					}
				});
				int size = list.size();// �ļ�����
				if (size > deletfileNum) {
					for (int i = 0; i < deletfileNum; i++) {
						Long key = list.get(i);
						fileMap.get(key).delete();// ��ǰ�濪ʼɾ��
					}
				} else {
					for (int i = 0; i < size; i++) {
						Long key = list.get(i);
						fileMap.get(key).delete();// ��ǰ�濪ʼɾ��
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * ��ȡ�ļ��ı���ʱ��
	 * 
	 * @param direct
	 * @param fileMap
	 */
	private static void getSavetime(File direct, HashMap<Long, File> fileMap) {
		File flist[] = direct.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				// num = num + getFilesNum(flist[i]);
				getSavetime(flist[i], fileMap);
			} else {
				File file = flist[i];
				long key = file.lastModified();
				fileMap.put(key, file);
			}
		}
	}

	/**
	 * ɾ��ĳһĿ¼�³���ָ���ļ���������ļ�
	 * 
	 * @param direct
	 *            Ŀ¼
	 * @param safe
	 *            Ҫ�����ĵ��ļ�
	 */
	public static void deleteOtherfiles(File direct, File safe) {
		if (direct == null || !direct.exists() || !direct.isDirectory()
				|| safe == null) {
			return;
		} else {
			File[] files = direct.listFiles();
			int length = files.length;
			for (int i = 0; i < length; i++) {
				File file = files[i];
				if (!file.getPath().equals(safe.getPath())) {
					file.delete();
				}
			}
		}
	}

	/**
	 * �ļ�����
	 * 
	 * @param sourceFile
	 *            ԭ�ļ�
	 * @param targetFile
	 *            Ŀ���ļ�
	 */
	public static void copy(File sourceFile, File targetFile) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(sourceFile);
			output = new FileOutputStream(targetFile);
			byte[] b = new byte[1024 * 1];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			System.out.println("files in the dolder may be using by someone");
		}
	}
}
