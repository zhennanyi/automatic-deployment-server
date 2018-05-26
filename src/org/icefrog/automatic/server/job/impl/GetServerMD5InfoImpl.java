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
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;
import org.icefrog.automatic.server.util.LogUtil;
import org.icefrog.automatic.server.util.MD5;
import org.icefrog.automatic.server.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/***
 * Get server md5 info
 * 
 * @author icefrogsu@gmail.com
 */
public class GetServerMD5InfoImpl implements Job {

	List<Map<String, String>> result = new ArrayList<Map<String, String>>();
	String onlineProjectPath;
	String informPath;

	@Override
	public Response dosome(Request request) {
		JSONObject json = JSONObject.fromObject(request.getFormerJson());
		String serverName = json.getString("removeServerName");
		
		Response response = new Response();
		response.setId(request.getId());
		
		try {
			PropertiesUtil propu = new PropertiesUtil(MasterCache.ComparisonConfigName);
			Properties p = propu.getProperties();
			onlineProjectPath = String.valueOf(p.get(serverName));
			informPath = onlineProjectPath;
			onlineProjectPath = onlineProjectPath.replaceAll("\\\\", "\\\\\\\\");
			traverseFolder2(onlineProjectPath);
			JSONArray jsons = JSONArray.fromObject(result);
			response.setMessage(jsons.toString());
			
		} catch (Exception e) {
			LogUtil.appendRuntimeLog("服务器端计算文件MD5时出现异常,计算的项目为:"+serverName+" 错误有效信息为:"+e.getMessage());
		}
		return response;
	}
	
	
	/**
	 * 递归一个目录下的所有文件,并且此过程中计算出所有md5值并且添加到result - List<Map<String,String>> 中
	 */
	public void traverseFolder2(String path) {
		path = path.replaceAll("\\\\", "\\\\\\\\");
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				//Directory is empty
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						//Is directory
						String absolutePath = file2.getAbsolutePath();
						
						/* new start */
						Map<String, String> map = new HashMap<String, String>();
						map.put("url", absolutePath.replaceAll(informPath.replaceAll("\\\\", "\\\\\\\\"), ""));
						map.put("md5", "unmd5");
						map.put("size", "0");
						result.add(map);
						/* new end */
						
						traverseFolder2(absolutePath);
					} else {
						try {
							//is file
							Map<String, String> map = new HashMap<String, String>();
							String absolutePath = file2.getAbsolutePath();
							File checkedFile = new File(absolutePath);
//							String md5 = getFileMD5(checkedFile);
							String md5 = MD5.getMD5(checkedFile);
							String url = absolutePath.replaceAll(informPath.replaceAll("\\\\", "\\\\\\\\"), "");
							map.put("url", url);
							map.put("md5", md5);
							map.put("size", String.valueOf(checkedFile.length()));
							result.add(map);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			//File unexists
		}
	}

	/**
	 * Calc file message digest
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}

		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();

		} catch(Exception e){
			//Skip
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
