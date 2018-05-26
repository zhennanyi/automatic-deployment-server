/*
 * Copyright 2018 ICE FROG SOFTWARE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */


package org.icefrog.automatic.server.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressUtil {
	private static final String BASE_DIR = "";
	private static final int BUFFER = 1024 * 1024;
	// 符号"/"用来作为目录标识判断符
	private static final String PATH = "/";

	private CompressUtil() {}

	public static void fileToZip(String inputFileName, String outputFileName) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFileName));
		// out.setEncoding("GBK"); // ###### 这句话是关键，指定输出的编码方式
		File f = new File(inputFileName);
		fileToZip(out, f, "");
		out.close();
	}

	private static void fileToZip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			base = base.length() == 0 ? "" : base + "/";
			out.putNextEntry(new ZipEntry(base));
			for (int i = 0; i < fl.length; i++) {
				fileToZip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			int n = in.read(buffer);
			while (n != -1) {
				out.write(buffer, 0, n);
				n = in.read(buffer);
			}
			in.close();
		}
	}
	public static void compress(File srcFile) throws Exception {
		String name = srcFile.getName();
		String basePath = srcFile.getParent();
		String destPath = basePath + name + ".zip";
		compress(srcFile, destPath);
	}

	public static void compress(File srcFile, File destFile) throws Exception {
		// 对输出文件做CRC32校验
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destFile), new CRC32());
		ZipOutputStream zos = new ZipOutputStream(cos);
		zos.setLevel(9);
		compress(srcFile, zos, BASE_DIR);
		zos.flush();
		zos.close();
	}
	public static void compress(File srcFile, String destPath) throws Exception {
		compress(srcFile, new File(destPath));
	}

	private static void compress(File srcFile, ZipOutputStream zos, String basePath) throws Exception {
		if (srcFile.isDirectory()) {
			compressDir(srcFile, zos, basePath);
		} else {
			compressFile(srcFile, zos, basePath);
		}
	}

	private static void compressDir(File dir, ZipOutputStream zos, String basePath) throws Exception {
		File[] files = dir.listFiles();
		// 构建空目录
		if (files.length < 1) {
			ZipEntry entry = new ZipEntry(dir.getName() + PATH);
			zos.putNextEntry(entry);
			zos.closeEntry();
		}
		for (File file : files) {
			// 递归压缩
			// 这里加上basePath就会带上当前目录，如果不加就会只打包该目录下的文件/文件夹
			compress(file, zos, basePath + dir.getName() + PATH);
		}
	}

	private static void compressFile(File file, ZipOutputStream zos, String dir) throws Exception {
		//压缩包内文件名定义,如果有多级目录，那么这里就需要给出包含目录的文件名 ,如果用WinRAR打开压缩包，中文名将显示为乱码
		ZipEntry entry = new ZipEntry(dir + file.getName());
		zos.putNextEntry(entry);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = bis.read(data, 0, BUFFER)) != -1) {
			zos.write(data, 0, count);
		}
		bis.close();
		zos.closeEntry();
	}
}
