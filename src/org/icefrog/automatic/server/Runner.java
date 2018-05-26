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

package org.icefrog.automatic.server;

import org.icefrog.automatic.server.core.EnvironmentConfig;
import org.icefrog.automatic.server.core.Resource;
import org.icefrog.automatic.server.core.RuntimeEnvironment;
import org.icefrog.automatic.server.module.ModuleConfig;
import org.icefrog.automatic.server.module.impl.FTP;
import org.icefrog.automatic.server.module.impl.MySQL;
import org.icefrog.automatic.server.module.impl.Other;
import org.icefrog.automatic.server.module.impl.Redis;
import org.icefrog.automatic.server.module.impl.ZBus;
import org.icefrog.automatic.server.thread.CoreWorkThread;
import org.icefrog.automatic.server.thread.InterflowThread;
import org.icefrog.automatic.server.util.LogUtil;

public class Runner {
	
	public static void main(String[] args){
		
		//将需要检测的产品配置到内存中
		ModuleConfig.addModule(new MySQL()); //MySQL
		ModuleConfig.addModule(new Redis()); //Redis
		ModuleConfig.addModule(new ZBus());  //ZBus
		ModuleConfig.addModule(new FTP());	 //FTP
		ModuleConfig.addModule(new Other()); //Other

		//配置运行环境
		EnvironmentConfig.setRuntimeEnvironment(RuntimeEnvironment.Debug, args);
		
		//注册系统运行环境
		Resource.initResource();
		LogUtil.appendRuntimeLog("**************************************************");
		LogUtil.appendRuntimeLog("系统初始化完成,正在开启工作线程. 数量:2");
		
		//开启核心工作线程
		CoreWorkThread core_t = new CoreWorkThread();
		core_t.start();
		
		InterflowThread interflow_t = new InterflowThread();
		interflow_t.start();
		
		LogUtil.appendRuntimeLog("系统运行中...");
	}
}