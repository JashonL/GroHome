package com.growatt.grohome.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class PinYinUtil {
	public static String getPinYin(String name){
		String result="";
		try {
			//ʹ��pinyin4������תƴ������
			//1)�趨��ƴ���ĸ�ʽ
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			//2)����һ����̬�������յ�һ���趨�ĸ�ʽ����ת��
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<name.length();i++){
				//������תƴ��֮ǰҪ��һ���жϣ���һ�µ�ǰҪת�����ַ����ǲ��Ǻ���
				String string = name.substring(i,i+1);
				if(string.matches("[\u4E00-\u9FFF]")){
					//"DAN","SHAN"
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(name.charAt(i), format);
					sb.append(pinyin[0]);
				}else{
					sb.append(string);
				}
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断有没有中文:没有返回-1；有返回字符串
	 * @param name
	 * @return
	 */
	public static String isPinYin(String name){
		boolean isFlag = false;
		String result="";
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<name.length();i++){
				String string = name.substring(i,i+1);
				if(string.matches("[\u4E00-\u9FFF]")){
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(name.charAt(i), format);
					sb.append(pinyin[0].substring(0,1));
					isFlag = true;
				}else{
					sb.append(string);
				}
			}
			result = sb.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isFlag) result = "-1";
		return result;
	}

}
