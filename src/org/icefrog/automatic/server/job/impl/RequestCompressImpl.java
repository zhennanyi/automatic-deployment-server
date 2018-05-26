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
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;
import org.icefrog.automatic.server.util.CompressUtil;
import org.icefrog.automatic.server.util.LogUtil;
import org.icefrog.automatic.server.util.PropertiesUtil;

public class RequestCompressImpl implements Job{

	@Override
	public Response dosome(Request request) {
		Response response = new Response();
		response.setId(request.getId());
		try {
			String onlineProjectKey = request.getMessage();
			PropertiesUtil proputil = new PropertiesUtil(MasterCache.ComparisonConfigName);
			Properties properties = proputil.getProperties();
			String srcDir = String.valueOf(properties.get(onlineProjectKey));
			String descDir = String.valueOf(properties.get("config.compress.dir"));
			descDir += File.separator + onlineProjectKey + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date(System.currentTimeMillis())) + ".zip";
			//开始压缩文件,此处可能会因为文件过大而导致压缩时间长. 
			CompressUtil.compress(new File(srcDir),descDir);
			//如果压缩成功,返回文件的绝对路径(服务器). 失败则返回failed
			response.setMessage(descDir);
		} catch (Exception e) {
			response.setMessage("failed");
			LogUtil.appendRuntimeLog("压缩服务器项目文件时引发异常,异常信息为:"+e.getMessage());
		}
		return response;
	}
}
