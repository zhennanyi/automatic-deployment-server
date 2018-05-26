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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;
import org.icefrog.automatic.server.util.PropertiesUtil;

/***
 * 获取在线服务器信息请求
 */
public class OnlineProjectGeterImpl implements Job{

	@Override
	public Response dosome(Request request) {
		Response response = new Response();
		response.setId(request.getId());
		try {
			PropertiesUtil pu = new PropertiesUtil(MasterCache.ComparisonConfigName);
			Properties properties = pu.getProperties();
			List<Map<String, String>> projects = new ArrayList<Map<String, String>>();
			Set<Object> set = properties.keySet();
			Iterator<Object> iterator = set.iterator();
			while(iterator != null && iterator.hasNext()){
				String key = String.valueOf(iterator.next());
				if(!key.startsWith("online")){
					continue;
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", key);
				map.put("url", String.valueOf(properties.get(key)));
				projects.add(map);
			}
			System.out.println(projects);
			response.setMessage("success");
			response.putParamsMap("projects", projects);
		} catch (IOException e) {
			e.printStackTrace();
			response.setMessage("failed");
		}
		return response;
	}
}
