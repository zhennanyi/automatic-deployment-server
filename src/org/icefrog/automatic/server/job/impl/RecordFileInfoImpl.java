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

import org.icefrog.automatic.server.cache.FileUploadCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;

import net.sf.json.JSONObject;

public class RecordFileInfoImpl implements Job{

	@Override
	public Response dosome(Request request) {
		System.out.println("开始接受文件名...");
		Response response = new Response();
		response.setId(request.getId());
		try{
			String formerJson = request.getFormerJson();
			formerJson = formerJson.replaceAll("\\\\", "\\\\\\\\");
			JSONObject json = JSONObject.fromObject(formerJson);
			String localUrl = json.getString("localurl");
			String filename = json.getString("filename");
			String filesize = json.getString("filesize");
			String serverdir= json.getString("serverDirKey");
			
			//添加文件信息到缓存中
			FileUploadCache.setFileName(filename);
			FileUploadCache.setFileSize(Integer.parseInt(filesize));
			FileUploadCache.setLocalURL(localUrl);
			FileUploadCache.setServerDir(serverdir);
			response.setMessage("success");
		}catch (Exception e) {
			response.setMessage("success");
			e.printStackTrace();
		}finally{
			FileUploadCache.setCacheStatus(1);//修改状态为待接受文件
			System.out.println("状态修改成为1,等待接下来的文件...");
		}
		return response;
	}
}
