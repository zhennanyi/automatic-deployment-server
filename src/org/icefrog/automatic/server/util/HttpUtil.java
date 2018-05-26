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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	
	/**
	 * 发起一个请求并且返回响应状态码
	 * 
	 * @param requestUrl 请求地址
	 * @param method 请求方式,本类提供静态的GET/POST方式  会自动转换为全大写
	 * @param timeout 超时毫秒数
	 */
	public static int getResponseCode(String requestUrl,String method,int timeout) throws IOException{
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		connection.setConnectTimeout(timeout);
		return connection.getResponseCode();
	}
	
}
