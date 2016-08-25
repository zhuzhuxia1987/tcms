package com.tcms.utils;


/**
  * @ClassName: MatchUtil
  * @Description: �ַ���ƥ�乤����
  * @author �����
  * @date 2016-5-17 ����11:16:29
  * @package_name ·����com.tcms.utils
  */
public class MatchUtil {
	// �绰�����������ʽ
	static String NumberRE1 = "(^(0\\d{2,3})?(\\d{7,8})(,\\d{3,})?)$";
	// �ֻ�����������ʽ
	private static String PHONEREG = "^(13\\d|147|15[0-35-9]|18[025-9])\\d{8}$";

	// ����ĸ��ͷ��Ӣ�Ļ��������3��50λ�ַ���
	private static String CHACCOUNT = "^\\w{3,20}$";

	// private static String CHACCOUNT = "^[A-Z0-9a-z_]{3,50}$";

	private static String PASSWORDREG = "^[\\x20-\\x7E]{6,50}$";
	private static String ALLNUMBER = "^\\d{6,50}";

	/**
	 * ��֤�ַ����Ƿ����ֻ�����
	 * 
	 * @param matchStr
	 *            ��ƥ����ַ���
	 * @return ����Ƿ���true���򷵻�false
	 */
	public static boolean isPhoneNum(String matchStr) {

		return matchStr == null ? false : matchStr.matches(PHONEREG);

	}

	/**
	 * �ж��Ƿ�Ϊȫ����
	 * 
	 * @param matchStr
	 * @return
	 */
	public static boolean isAllNumber(String matchStr) {
		return matchStr == null ? false : matchStr.matches(ALLNUMBER);

	}

	/**
	 * ��֤�ַ����Ƿ��ǵ绰�������ֻ�������
	 * 
	 * @param matchStr
	 *            ��ƥ����ַ���
	 * @return ����Ƿ���true���򷵻�false
	 */
	public static boolean isContactNum(String matchStr) {
		return matchStr == null ? false : (matchStr.matches(PHONEREG) || matchStr.matches(NumberRE1));
	}

	/**
	 * �ж��Ƿ��ǺϷ����ʺ�
	 * 
	 * @param matcherStr
	 *            ��������ַ���
	 * @return ����Ϸ�����true ���򷵻�false
	 */
	public static boolean isLicitAccount(String matcherStr) {

		return matcherStr == null ? false : (matcherStr.matches(PHONEREG) || matcherStr.matches(CHACCOUNT));

	}

	public static boolean isLicitPassword(String matcherStr) {

		return matcherStr == null ? false : matcherStr.matches(PASSWORDREG);

	}
}
