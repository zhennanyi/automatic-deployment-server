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

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.util.LogUtil;

/***
 * 注册当前工具运行在Debug模式还是Online模式.  
 * 不同的模式在某些情况下分别表示Windows/Linux运行环境 .
 * 
 * @author icefrogsu@gmail.com
 */
public class EnvironmentConfig {

	public static void setRuntimeEnvironment(RuntimeEnvironment environment, String[] args) {
		
		if (environment == RuntimeEnvironment.Debug) {
			MasterCache.SystemRuntimePath = "C:\\Icefrog\\DeveloperFiles\\Workspaces\\Eclipse neon3\\smisserver\\bin\\master.properties";
			MasterCache.environment = RuntimeEnvironment.Debug;
		} else if (environment == RuntimeEnvironment.Online) {
			MasterCache.environment = RuntimeEnvironment.Online;
			if (args.length > 0) {
				String configPath = args[0];
				if (configPath == null || configPath.replaceAll(" ", "") == "") {
					LogUtil.error(String.format("配置文件{%s} 路径错误!", configPath));
				} else {
					MasterCache.SystemRuntimePath = args[0];
				}
			} else {
				LogUtil.error("请传入主配置文件!");
				return;
			}
		}
	}
	
}
