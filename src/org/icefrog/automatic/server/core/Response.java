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


package org.icefrog.automatic.server.core;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/***
 * SMIS Response
 * 
 * @author icefrogsu@gmail.com
 */
public class Response {
	private long id;
	private String message;
	private Map<String, Object> paramsMap = new HashMap<String, Object>();
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("id", String.valueOf(this.id));
		json.put("message", this.message);
		json.put("data", this.paramsMap);
		return json.toString();
	}

	public Map<String, Object> putParamsMap(String key,Object value){
		this.paramsMap.put(key, value);
		return this.paramsMap;
	}
	
	public Map<String, Object> putParamsMap(String key,Object value,Object defaultval){
		if(value == null){
			value = defaultval;
		}
		this.paramsMap.put(key, value);
		return this.paramsMap;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}
}
