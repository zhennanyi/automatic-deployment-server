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

import java.io.IOException;
import java.util.Properties;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.util.LogUtil;
import org.icefrog.automatic.server.util.PropertiesUtil;

/***
 * System resource initializer
 * 
 * @author icefrogsu@gmail.com
 */
public class Resource {
	 
	public static void initResource(){
		try{
			//初始化一些资源到内存
			LogUtil.info("正在注册系统运行环境");
			PropertiesUtil putil = new PropertiesUtil(MasterCache.SystemRuntimePath);
			Properties properties = putil.getProperties();
			
			MasterCache.MachinePort = Integer.parseInt((String)properties.get("config.port"));
			MasterCache.InterflowPort = Integer.parseInt((String)properties.get("config.interflow.port"));
			MasterCache.MasterConfigName = (String)properties.get("config.master.configname");
			MasterCache.Encode = (String)properties.get("config.default.encode");
			MasterCache.RuntimeLogdir = (String)properties.get("config.runtime.logdir");
			MasterCache.DatetimePattern = (String) properties.get("config.datetime.pattern");
			MasterCache.DatePattern = (String) properties.get("config.date.pattern");
			MasterCache.ComparisonConfigName = (String) properties.getProperty("config.project.comparison");
			
			//系统初始化完成
			LogUtil.info("系统环境注册完成");
		}catch (Exception e) {
			LogUtil.error("注册系统运行环境时出现异常."+e.getMessage()+" 请检查服务端程序配置是否异常!");
			LogUtil.appendRuntimeLog("注册系统运行环境时出现异常. "+e.getMessage());
		}
	}
	
	public static Properties getMainconfigProperties() throws IOException{
		PropertiesUtil putil = new PropertiesUtil(MasterCache.SystemRuntimePath);
		return putil.getProperties();
	}
	
	public static void reInit(){
		initResource();
	}
}
