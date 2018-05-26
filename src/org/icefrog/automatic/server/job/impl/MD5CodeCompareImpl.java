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

import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/***
 * Not used
 */
public class MD5CodeCompareImpl implements Job{

	@Override
	public Response dosome(Request request) {
		String json = request.getFormerJson();
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		
		//{"clientmd5s":[{"path":"smis_2017-12-08.log","md5":"7f47c1223ffde60c70e24cbe305a92f9"},
		//{"path":"smis_2017-12-10.log","md5":"36591987b01ad76ddeaf76778359ac64"},
		//{"path":"smis_2017-12-11.log","md5":"f6f7b6122307e57fc260b0263d5b7e5b"},
		//{"path":"smis_2017-12-12.log","md5":"a21b908f6425d9c51c5384a2fb4493a5"}],
		//"requestid":"636486871721688143","message":"",
		//"headingcode":"a62e33be4197be8440f048da761755f7"}
		String serverName = jsonObject.getString("removeServerName");
		System.out.println("获取到的服务器key:"+serverName);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("clientmd5s"));
		for (Object item : jsonArray) {
			JSONObject jsonObj = JSONObject.fromObject(item);
			System.out.println("key:"+jsonObj.getString("md5") +"--- value:"+ jsonObj.getString("path"));
		}
		
		Response response = new Response();
		response.setId(request.getId());
		response.setMessage("success");
		response.setParamsMap(null);
		return response;
	}

}
