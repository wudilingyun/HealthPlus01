package com.vee.imandforum.heahth_news_utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.yunfox.s4aservicetest.response.YysNewsResponse;

import android.R.integer;
import android.R.string;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder.OutputFormat;
import android.os.Environment;
import android.os.StatFs;
import android.text.StaticLayout;

public class JsonCache {
	private static final String TAG = "FileCache";
	private static final String CACHDIR = "GsonCach";// 指定缓存目录名字
	private static final String WHOLESALE_CONV = ".cach";// 扩展名
	public static JsonCache GETINSTANCE;
	private static final int MB = 1024 * 1024;
	private static final int MAX_SIZE = 10;
	private static final int FRRE_SD_SPACE_NEED_TO_CACHE = 10; // sd的最小可用于分配给缓存空间。
	private List<YysNewsResponse> s;

	// 1 初始化缓存，首先移除原来的缓存。防止空间不够。
	private JsonCache() {
		// removeCache(getCachDirectory());
	}

	// 2 得到SD卡目录
	public String getSDPath() {
		File sdDir = null;
		// 先判断SD卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			System.out.println("SD卡存在且处于挂载状态");
			// 获取SD卡的根目录。
			sdDir = Environment.getExternalStorageDirectory();
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return " ";
		}

	}

	public static JsonCache getInstance() {
		if (GETINSTANCE == null) {
			GETINSTANCE = new JsonCache();
		}
		return GETINSTANCE;
	}

	// 3 获取SD卡剩余空间
	private int freeSpaceOnSd() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) statFs.getAvailableBlocks() * (double) statFs
				.getBlockSize()) / MB;

		return (int) sdFreeMB;

	}

	// 4 得到缓存文件的目录
	public String getCachDirectory() {
		String cachDir = getSDPath() + "/" + CACHDIR;
		return cachDir;
	}

	// 5 将url 转换成文件名
	private String convertUrltoFileName(String url) {

		return url.hashCode() + WHOLESALE_CONV;// url的哈希吗 +扩展名
	}

	// 6.更新最后读取数据时间
	public void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();// 系统当前时间
		file.setLastModified(newModifiedTime);
	}

	// 7. 从缓存中获取Json数据
	@SuppressWarnings("unused")
	public String getJson(final String url) {
		final String path = getCachDirectory() + "/"
				+ convertUrltoFileName(url);
		File file = new File(path);
		if (file.exists()) {
			String result = "";
			try {
				InputStream in = new FileInputStream(file);
				BufferedReader bf = new BufferedReader(
						new InputStreamReader(in));
				String line = "";

				while ((line = bf.readLine()) != null) {
					result = result + line;
				}
				in.close();
				bf.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (path == null) {
				file.delete();// 不存在删除这个目录
			} else {
				System.out.println("-------》+命中缓存文件");
				updateFileTime(path);
				return result;
			}
		}

		return "";
	}

	// 8.向文件缓存当中存储一个json数据
	public void saveJson(String json, String url) {

		if (json == null) {
			return;
		}
		// 如果SD卡的剩余空间小于 文件缓存所需要最小空间
		if (FRRE_SD_SPACE_NEED_TO_CACHE > freeSpaceOnSd()) {
			System.out.println("剩余空间" + freeSpaceOnSd());
			System.out.println("准备向文件中写入数据");
			return;
		}
		String filename = convertUrltoFileName(url);// 找到文件名
		String dir = getCachDirectory();
		File dirFile = new File(dir);

		// 判断缓存目录dirFile是否存在
		if (!dirFile.exists()) {
			dirFile.mkdir();// 不存在就把这个目录创建出来。
		}
		// 创建SdCard缓存文件的路径
		File file = new File(dir + "/" + filename);

		try {
			if (file.exists()) {
				file.delete();
			}

			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			out.write(json.getBytes());
			System.out.println("写入SD卡Json数据成功");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 8.1 写入序列化过的对象

	public void saveObject(List<YysNewsResponse> o, String name) {

		if (o == null) {
			return;
		}
		// 如果SD卡的剩余空间小于 文件缓存所需要最小空间
		if (FRRE_SD_SPACE_NEED_TO_CACHE > freeSpaceOnSd()) {
			System.out.println("剩余空间" + freeSpaceOnSd());
			System.out.println("准备向文件中写入数据");
			return;
		}
		String filename = convertUrltoFileName(name);// 找到文件名
		String dir = getCachDirectory();
		File dirFile = new File(dir);

		// 判断缓存目录dirFile是否存在
		if (!dirFile.exists()) {
			dirFile.mkdir();// 不存在就把这个目录创建出来。
		}
		// 创建SdCard缓存文件的路径
		File file = new File(dir + "/" + filename);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(o);// 写入
			System.out.println("写入成功");
			fos.close(); // 关闭输出流
			oos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 8.1获取 序列化对象
	@SuppressWarnings({ "unused", "unchecked" })
	public List<YysNewsResponse> getObject(final String url) {
		final String path = getCachDirectory() + "/"
				+ convertUrltoFileName(url);
		File file = new File(path);
		if (file.exists()) {
			String result = "";
			try {
				InputStream in = new FileInputStream(file);

				ObjectInputStream ois = new ObjectInputStream(in);
				s = (List<YysNewsResponse>) ois.readObject();
				in.close();
				ois.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (path == null) {
				file.delete();// 不存在删除这个目录
			} else {
				updateFileTime(path);
				return s;
			}
		}

		return s;
	}

	// 9.已文件最后修改时间进行升序排序
	private class FileLastModifSort implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {
			if (file1.lastModified() > file2.lastModified()) {
				return 1;
			} else if (file1.lastModified() == file2.lastModified()) {
				return -1;
			} else {
				return 0;
			}

		}

	}

	// 10. 如果空间不足。对文件进行按照时间的升序排序，删除掉比较老的缓存。
	public boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();// 将文件夹内容放入到File数组当中
		if (files == null) {
			return true;
		}
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			// 如果文件的名字已 .cach结尾
			if (files[i].getName().endsWith(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}
		// 判断缓存当中的数据是否已经超过了最大值，或者SDcard空间不足
		if (dirSize > MAX_SIZE * MB
				|| FRRE_SD_SPACE_NEED_TO_CACHE > freeSpaceOnSd()) {
			// 取文件的一半+1;
			int removeFactor = (int) ((0.4 * files.length) + 1);
			// 按照时间进行排序
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++) {
				// 删除那0.4+1文件
				files[i].delete();
			}
			if (freeSpaceOnSd() <= MAX_SIZE) {
				return false;
			}

		}

		return true;

	}

}
