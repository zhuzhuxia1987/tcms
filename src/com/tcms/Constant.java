package com.tcms;

public class Constant {
	// http����״̬���־
	public final static int STATAS_OK = 200;// ����OK
	public final static int NO_RESPONSE = 400;// ��������Ӧ �Ҳ�����Ӧ��Դ
	public final static int S_EXCEPTION = 500;// ����������
	public final static int RESPONESE_EXCEPTION = 160;// ��Ӧ�쳣
	public final static int TIMEOUT = 101;// ����ʱ
	public final static int NO_NETWORK = 102;// û�ÿ�������
	public final static int NULLPARAMEXCEPTION = 103;// ����Ϊ���쳣
	public final static int RESPONSE_OK = 200;
	public final static int OS_FAILED = 403;
	public final static int RELOGIN = 4001;
	public final static int SERVER_EXCEPTION = 5001;
	public final static int NULLPARAM = 4006;// ����ֵΪ��
	public final static int LOSEPARAM = 4005;// ȱ�ٲ���
	public final static String SHENPIQX = "002004001";// ����Ȩ��
	public final static String SAVAVERSIONCODE = "savaVersionCode";// ���������һ�������õİ汾
	// ���ʵ�����   
//	public final static String HOST = "http://192.168.0.122:8080/cimsSeam/seam/resource/v1";
	public final static String HOST = "http://jh5138.xicp.net:9580/cimsSeam/seam/resource/v1";
	public final static String USERAPI = HOST + "/RegisterContext/saveUser";
	public final static String SEAM = "seam/resource/v1/";
	public final static String GETLOGINNAME = SEAM
			+ "Login2AZContext/getLoginName";
	public final static String GETCAILIAO = SEAM
			+ "JianYanXX2AZContext/getcailiao";
	public final static String AGREE = SEAM
			+ "JianYanXX2AZContext/agree";
	public final static String AGREEALL = SEAM
			+ "JianYanXX2AZContext/agreeall";
	public final static String ROLLBACK = SEAM
			+ "JianYanXX2AZContext/rollback";
	public final static String GETDAICHULIBAOGAO = SEAM
			+ "JianYanXX2AZContext/getProveJianYanList";
	public final static String LOANAPI = HOST + "/Login2AZContext/loginV2";
	public final static String OrganAPI = HOST
			+ "/RegisterContext/getJiGouList";
	public final static String UPDATEUSER = HOST
			+ "/RegisterContext/updateUser";
	public final static String ISPDF = SEAM
			+ "JianYanXX2AZContext/isPDF";
	public final static String DOWNLOAD = SEAM
			+ "DownloadFileConxt/download";
	public final static String ORPREFERENCES = "tcmsConfig";
	public final static String DOWNLOADURL = "http://192.168.0.63:8080/cimsSeam/seam/resource/v1/VersionUpContext/getLatestVersionCode";
	public final static String DOWNLOADAPK = "http://192.168.0.63:8080/cimsSeam/seam/resource/v1/VersionUpContext/downloadApk";
	public final static String ISSAVEPW = "isSavepassword";
	public final static String USERNAME = "userName";
	public final static String PASSWORD = "passWord";
	public final static String JIGOUINI = "jigouini";
	public final static String JIGOUMC = "jigoumc";
	public final static String LOGINNSME = "loginname";
	public final static String XINGMING = "xingming";
	public final static String USERID = "userid";
	public final static String USERICONPATH = "usericonpath";// �û�ͼ�񱣴��url
																// =qrcode+path;//�û���url+�洢·��
	public final static String ENCODEPASSWORD = "tcmstcms12345678";
}