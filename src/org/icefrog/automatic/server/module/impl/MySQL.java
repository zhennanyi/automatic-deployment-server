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


package org.icefrog.automatic.server.module.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.module.IModule;
import org.icefrog.automatic.server.util.CommonUtil;
import org.icefrog.automatic.server.util.LogUtil;

public class MySQL implements IModule{

	private final String MySQLCountKey = "MySQLCount";    //MySQL一共检验的次数
	private final String MySQLSuccessKey = "MySQLSuccess";//MySQL正确的次数
	private final String MySQLIdentityMillKey = "MySQLIdentityMill";//记录监测此项功能的毫秒数
	
	@Override
	public boolean identity(Properties properties) {
		LogUtil.info("开始检测MySQL连通性");
		//增加总检测次数
		addCount();
		long begin = System.currentTimeMillis();
		boolean result = false;
		try {
			
			Object url = properties.get(MasterCache.MySQLConfig.get("UrlKeyName"));
			Object username = properties.get(MasterCache.MySQLConfig.get("UserName"));
			Object password = properties.get(MasterCache.MySQLConfig.get("Password"));
			
			if(!CommonUtil.securityParams(url,username,password)){
				LogUtil.error("未检测到配置有MySQL的任何有效信息!");
				addExecMill(String.valueOf(System.currentTimeMillis()-begin));
				return false;
			}
			
			LogUtil.debug("url:"+url);
			LogUtil.debug("username:"+username);
			LogUtil.debug("password:"+password);
			DriverManager.setLoginTimeout(2);
			Connection connection = DriverManager.getConnection(url.toString(), username.toString(), password.toString());
			LogUtil.info("MySQL状态正常!");
			connection.close();
			
			//增加检测成功次数
			addSuccessCount();
			result = true;
		} catch (Exception e) {
			LogUtil.error("MySQL可能存在问题,如果在意料之外应当立即处理此问题,MySQL错误消息:"+e.getMessage());
			result = false;
		}
		long end = System.currentTimeMillis();
		//增加执行毫秒数
		addExecMill(String.valueOf(end-begin));
		return result;
	}
	
	private void addCount(){
		int mysqlCount = Integer.valueOf(MasterCache.responseResult.get(MySQLCountKey) == null 
				|| MasterCache.responseResult.get(MySQLCountKey) == ""  ? "0" 
				: String.valueOf(MasterCache.responseResult.get(MySQLCountKey)));
		MasterCache.responseResult.put(MySQLCountKey, ++mysqlCount);
	}
	
	private void addSuccessCount(){
		int successCount = Integer.valueOf(MasterCache.responseResult.get(MySQLSuccessKey) == null 
				|| MasterCache.responseResult.get(MySQLSuccessKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(MySQLSuccessKey)));
		MasterCache.responseResult.put(MySQLSuccessKey, ++successCount);
	}
	
	private void addExecMill(String mills){
		MasterCache.responseResult.put(MySQLIdentityMillKey,mills);
	}
}