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

import java.util.ArrayList;
import java.util.List;

import org.icefrog.automatic.server.cache.MasterCache;

import net.sf.json.JSONObject;

public class JsonUtil {
	
	public static List<String> logs = new ArrayList<>();
	
	public static List<String> append(String msg){
		logs.add(msg);
		return logs;
	}
	
	public static String toJsonString(){
		MasterCache.responseResult.put("logs", logs.toArray());
		JSONObject jsonObj = JSONObject.fromObject(MasterCache.responseResult);
		//JSONArray array = JSONArray.fromObject(logs);
		return jsonObj.toString();
	}
	
	public static void clear(){
		logs = new ArrayList<>();
	}
	
}
