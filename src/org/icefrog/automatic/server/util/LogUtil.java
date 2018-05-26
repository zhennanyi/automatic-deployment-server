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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.icefrog.automatic.server.cache.MasterCache;

public class LogUtil {
	
	private static final Logger log = Logger.getLogger(LogUtil.class);
	
	public static void debug(String msg){
		JsonUtil.append("[DEBUG] "+msg);
		log.debug(msg);
	}
	
	public static void info(String msg){
		JsonUtil.append("[INFO] "+msg);
		log.info(msg);
	}
	
	public static void warn(String msg){
		JsonUtil.append("[WARN] "+msg);
		log.fatal(msg);
	}
	
	public static void error(String msg){
		JsonUtil.append("[ERROR] "+msg);
		log.error(msg);
	}
	
	public static void fatal(String msg){
		JsonUtil.append("[FATAL] "+msg);
		log.fatal(msg);
	}
	
	public static void appendRuntimeLog(String msg){
		try{
			File file = new File(MasterCache.RuntimeLogdir + File.separator+"smis_"+getDateStr() + "." + MasterCache.RuntimeLogStuffix);
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file, true);
			out.write((System.getProperty("line.separator")+"["+getDatetimeStr()+"]"+msg).getBytes(MasterCache.Encode));
			out.flush();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
			warn("写出日志内容:{"+msg+"}到服务器日志文件时出现异常:"+e.getMessage());
		}
	}
	
	public static String getDatetimeStr(){
		return getDateStr(MasterCache.DatetimePattern);
	}
	
	public static String getDateStr(){
		return getDateStr(MasterCache.DatePattern);
	}
	
	private static String getDateStr(String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new java.util.Date(System.currentTimeMillis()));
	}
}
