package com.greathammer.serial;

public class ASC {
	private static int ascNum;
	private static char strChar;

	public static void main(String[] args) {
		System.out.println(getAsc("ab"));
		System.out.println(backchar(98));

		System.out.println((int) 'A');
		System.out.println((char) 65);

		String stringConvertAsc = stringConvertAsc("16-11-02");
		System.out.println(stringConvertAsc);
	}

	/**
	 * 字符转ASC
	 * 
	 * @param st
	 * @return
	 */
	public static int getAsc(String st) {
		byte[] gc = st.getBytes();
		ascNum = (int) gc[0];
		return ascNum;
	}

	/**
	 * ASC转字符
	 * 
	 * @param backnum
	 * @return
	 */
	public static char backchar(int backnum) {
		strChar = (char) backnum;
		return strChar;
	}

	public static String stringConvertAsc(String text) {
		StringBuffer sb = new StringBuffer();
		String[] split = text.split("");
		for (String cha : split) {
			int asc = getAsc(cha);
			sb.append(asc);
		}
		return sb.toString();
	}

}
