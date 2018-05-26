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


package org.icefrog.automatic.server.job.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.icefrog.automatic.server.cache.FileUploadCache;
import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.core.RuntimeEnvironment;
import org.icefrog.automatic.server.job.Job;
import org.icefrog.automatic.server.util.LogUtil;
import org.icefrog.automatic.server.util.PropertiesUtil;

/***
 * 文件上传具体工作实现类<br>
 * 此工具用于保存前端传入的一个输入流 并且循环读取到内存中和写出到磁盘. 此实现依赖每一次执行此代码前的文件信息包括文件名,文件大小 其中文件大小(byte
 * size)将作为初始化byte数组的初始化大小
 * 
 * @author icefrogsu@gmail.com
 */
public class DoUploadImpl implements Job {

	@Deprecated
	@Override
	public Response dosome(Request request) {
		return null;
	}

	public Response dosome(InputStream in) {
		System.out.println("进入文件上传!!!");
		Response response = new Response();
		try {
			byte[] data = new byte[FileUploadCache.getFileSize()];
			int idx = 0;
			int totalLen = data.length;
			int readLen = 0;
			System.out.println("data.length:" + data.length);
			while (idx < totalLen) {
				System.out.println("循环读取数据中...");
				readLen = in.read(data, idx, totalLen - idx);
				System.out.println("读到:" + readLen);
				if (readLen > 0) {
					idx = idx + readLen;
				} else {
					break;
				}
			}
			PropertiesUtil proputil = new PropertiesUtil(MasterCache.ComparisonConfigName);
			String serverProjectAbsolutionPath = String
					.valueOf(proputil.getProperties().get(FileUploadCache.getServerDir()));
			String localurl = "";
			if (MasterCache.environment == RuntimeEnvironment.Debug) {
				localurl = FileUploadCache.getLocalURL();
			} else {
				// 为Linux平台考虑,否则文件上传会失败.
				localurl = FileUploadCache.getLocalURL().replaceAll("\\\\", "/");
			}
			File file = new File(serverProjectAbsolutionPath + File.separator + localurl);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}
			OutputStream out = new FileOutputStream(file);
			out.write(data);
			out.flush();
			out.close();
			System.out.println("文件写出成功!");
			response.setMessage("success");
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setMessage("failed");
			LogUtil.appendRuntimeLog("上传文件时引发异常,异常信息:" + e.getMessage());
			return response;
		} finally {
			FileUploadCache.setCacheStatus(0);// 修改状态为本次文件接受完成
		}
	}
}
