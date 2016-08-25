package com.tcms.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ScrollView;

/**
 * @ClassName: MainPaperAdapter
 * @Description: ��ҳ���PaperAdapter
 * @author �����
 * @date 2016-5-18 ����10:22:36
 * @package_name ·����com.tcms.fragment
 */
public class MainPaperAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentlist;

	public MainPaperAdapter(FragmentManager fm, ArrayList<Fragment> fragmentlist) {

		super(fm);
		this.fragmentlist = fragmentlist;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {

		return (fragmentlist == null ? null : fragmentlist.get(index));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (fragmentlist == null ? 0 : fragmentlist.size());
	}

}
