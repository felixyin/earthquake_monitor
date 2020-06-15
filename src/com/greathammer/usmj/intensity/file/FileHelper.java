package com.greathammer.usmj.intensity.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件助手. <br>
 * 读写文件.
 * 
 * @version 0.1.3 2016-07-07
 * @author Zheng Chao
 */
public class FileHelper {

	private FileHelper() {
		throw new AssertionError();
	}

	/**
	 * 写文件准备. <br>
	 * 检查文件是否存在. 若不存在, 则创建文件夹与该文件.
	 * <p>
	 * <i>Tag:Method 1-1</i>
	 * 
	 * @param filePath
	 *            文件路径 <br>
	 *            完全路径/路径/文件名 均可
	 * @throws IOException
	 */
	public static boolean prepareForWriting(String filePath) throws IOException {
		File file = new File(filePath);
		boolean fileReady = false;
		if (!file.exists()) {
			if (file.getParentFile() != null) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
			}
			file.createNewFile();
			fileReady = true;
		} else {
			fileReady = true;
		}
		return fileReady;
	}

	/**
	 * 读文件准备. <br>
	 * 检查文件是否存在.
	 * <p>
	 * <i>Tag:Method 1-2</i>
	 * 
	 * @param String
	 *            filePath 文件路径 <br>
	 *            完全路径/路径/文件名 均可
	 * @throws IOException
	 */
	public static boolean prepareForReading(String filePath) {
		File file = new File(filePath);
		Boolean fileReady = false;
		if (file.exists())
			fileReady = true;

		return fileReady;
	}

	/**
	 * 将字符串写入文件中. <br>
	 * 包含对文件路径存在的判断. <br>
	 * 文件为"UTF-8"格式编码
	 * <p>
	 * <i>Tag:Method 1-3</i>
	 * 
	 * @param text
	 * @param filePath
	 * @throws IOException
	 */
	public static void stringToFile(String text, String filePath, boolean overwrite, boolean newline)
			throws IOException {

		boolean fileReady = prepareForWriting(filePath);

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		if (fileReady) {
			try {
				fos = new FileOutputStream(filePath, !overwrite);
				osw = new OutputStreamWriter(fos, "UTF-8");
				bw = new BufferedWriter(osw);
				bw.write(text);
				if (newline)
					bw.newLine();
			} finally {
				if (fos != null) {
					if (osw != null) {
						if (bw != null) {
							bw.close();
							bw = null;
						}
						osw.close();
						osw = null;
					}
					fos.close();
					fos = null;
				}
			}
		}
	}

	/**
	 * 读取文件成字符串.
	 * <p>
	 * <i>Tag:Method 1-4</i>
	 * 
	 * @param String
	 *            filePath
	 * @param boolean
	 *            compress 段行是否保留, true: 压缩, 不保留; false: 保留, 原文
	 * @throws IOException
	 */
	public static String stringFromFile(String filePath, boolean compress) throws IOException {

		boolean fileReady = prepareForReading(filePath);

		FileReader fr = null;
		StringBuilder sb = null;
		if (fileReady) {
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String nextLine;
			sb = new StringBuilder();

			nextLine = br.readLine();
			if (nextLine != null) {
				sb.append(nextLine);
				while ((nextLine = br.readLine()) != null) {
					if (!compress)
						sb.append("\n");
					sb.append(nextLine);
				}
			}

			if (br != null) {
				br.close();
				br = null;
			}
			if (fr != null) {
				fr.close();
				fr = null;
			}
		}
		if (sb == null || sb.toString().equalsIgnoreCase(""))
			return null;
		else
			return sb.toString();

	}

	/**
	 * 读取文件成字节.
	 * <p>
	 * <i>Tag:Method 1-5</i>
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static byte[] bytesFromFile(String filePath) throws IOException {

		boolean fileReady = prepareForReading(filePath);

		byte[] data = new byte[] {};
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();

		if (fileReady) {
			FileInputStream fis = new FileInputStream(filePath);
			boolean done = false;

			while (!done) {
				int next = fis.read();
				if (next == -1) {
					done = true;
				} else {
					dataStream.write(((byte) next));
				}
			}
			fis.close();
			fis = null;
		}
		data = dataStream.toByteArray();
		dataStream.close();
		dataStream = null;
		return data;

	}

	/**
	 * 将字节写入文件.
	 * <p>
	 * <i>Tag:Method 1-6</i>
	 * <p>
	 * 用在图片生成上 <br>
	 * 包含对文件路径存在的判断.
	 * 
	 * @param filePath
	 * @param data
	 * @throws IOException
	 */
	public static void bytesToFile(String filePath, byte[] data) throws IOException {
		// if (filePath == null || filePath.length() == 0)
		// ZCLog.systemDebugLog(new IllegalArgumentException("Must include a
		// valid path"), true, false);
		boolean fileReady = prepareForWriting(filePath);

		if (fileReady) {
			File file = new File(filePath);

			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);

			ps.write(data);
			ps.close();
			ps = null;
			fos.close();
			fos = null;
		}

	}

	/**
	 * 读取csv内容
	 * <p>
	 * <i>Tag:Method 1-7</i> <br>
	 * 以首行值为key
	 * 
	 * @param csvFilePath
	 * @return
	 */
	public static List<Map<String, String>> dictionaryFromCSVFile(String csvFilePath) throws IOException {
		Map<String, String> oneLineDictionary = new HashMap<>();
		List<Map<String, String>> contentArray = new ArrayList<>();

		boolean fileReady = prepareForReading(csvFilePath);

		if (fileReady) {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(csvFilePath)), "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine();
			if (line != null && line.trim().length() > 0) {
				String keys[] = line.split("\\,");

				line = reader.readLine();
				while (line != null) {
					if (line.trim().length() > 0) {
						String values[] = line.split("\\,");
						oneLineDictionary = new HashMap<>();
						for (int i = 0; i < keys.length; i++) {
							oneLineDictionary.put(keys[i], values[i]);
						}
						contentArray.add(oneLineDictionary);
					}
					line = reader.readLine();
				}
			}
			reader.close();
		}
		return contentArray;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 演示.
	 */
	public static void main(String[] args) {
		// // Method 1 Test
		// // Method 1-1 prepareForWriting
		// String method1_1 = "test/method1_1.txt";
		// String method1_1T = "test/method1_1T.txt";
		// try {
		// System.out.println("Method 1-1 prepareForWriting: " +
		// (prepareForWriting(method1_1)?"true":"false"));;
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// // Method 1-2 prepareForReading
		// System.out.println("Method 1-2 prepareForReading: " +
		// (prepareForReading(method1_1)?"true":"false"));
		// // Method 1-3 writeTextToFile
		// String method1_3 = "Hellow World!";
		// try {
		// stringToFile(method1_3, method1_1, false, true);
		// stringToFile(method1_3, method1_1, true, true);
		// stringToFile(method1_3, method1_1, false, false);
		// stringToFile(method1_3, method1_1, false, true);
		// stringToFile("", method1_1, false, true);
		// stringToFile("", method1_1, false, true);
		// stringToFile(method1_3, method1_1, false, false);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// // Method 1-4 stringFromFile
		// try {
		// String method1_4Loose = stringFromFile(method1_1, false);
		// System.out.println("Method 1-4 stringFromFile 原文:
		// \n\\\\\\\\\\\\\\\\\\\\\\\\\n" + method1_4Loose +
		// "\n\\\\\\\\\\\\\\\\\\\\\\\\");
		// stringToFile(method1_4Loose, method1_1T, true, false);
		// System.out.println("\t 紧密:\n\t" + stringFromFile(method1_1, true));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// // Method 1-5 bytesFromFile
		// String method1_5S = "test/pic.gif";
		// byte[] method1_5 = null;
		// try {
		// method1_5 = bytesFromFile(method1_5S);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// System.out.println("Method 1-5 bytesFromFile 读取大小为" +
		// method1_5.length + "字节");
		// // Method 1-6 bytesToFile
		// String method1_6S = "test/pic_wr.gif";
		// try {
		// bytesToFile(method1_6S, method1_5);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// Method 1-7 readCSVFile
		String method1_7S = "gis/places/placesInfo.csv";
		try {
			List<Map<String, String>> content = dictionaryFromCSVFile(method1_7S);
			System.out.println(content.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
