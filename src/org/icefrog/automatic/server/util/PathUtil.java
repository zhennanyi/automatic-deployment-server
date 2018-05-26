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

public class PathUtil {
	
	/**
	 * 获得当前进程执行的完整(绝对)路径,不包含程序执行名<br>
	 * 可能会因为获取失败而导致返回 '/' 
	 */
	public static String getStartupPath(){
		String prop = System.getProperty("java.class.path","null;null;null");
		String[] splited = prop.split(";");
		return splited[0].equals("null") ? "/" : splited[0];
	}
}
