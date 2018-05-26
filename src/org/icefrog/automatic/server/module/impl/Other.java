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

import java.util.Properties;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.module.IModule;
import org.icefrog.automatic.server.util.CommonUtil;
import org.icefrog.automatic.server.util.LogUtil;

public class Other implements IModule{
	
	private final String OtherCountKey = "OtherCount";    //Other一共检验的次数
	private final String OtherSuccessKey = "OtherSuccess";//Other正确的次数
	private final String OtherIdentityMillKey = "OtherIdentityMill";//记录监测此项功能的毫秒数
	
	@Override
	public boolean identity(Properties properties) {
		long begin = System.currentTimeMillis();
		boolean result = false;
		//增加一次总检测次数
		addCount();
		
		try{
			Object rootdomain = properties.get(MasterCache.RootDomain);
			Object serverIndex = properties.get(MasterCache.ServerIndex);
			Object devMode = properties.get(MasterCache.DevMode);
			
			if(!CommonUtil.securityParams(rootdomain,serverIndex,devMode)){
				LogUtil.error("关于RootDomain,ServerIndex,DevMode的配置项可能不完全,若非必要,请忽略");
				addExecMill(String.valueOf(System.currentTimeMillis() - begin));
				return false;
			}
			
			//检测RootDomain
			if("paicheji.com".equals(rootdomain.toString())){
				LogUtil.info("RootDomain配置正确（paicheji.com）");
			}else{
				LogUtil.info("RootDomain配置不正确,配置为（"+rootdomain+"）");
			}
			
			
			//检测ServerIndex
			int iIndex = Integer.parseInt(serverIndex.toString().replaceAll(" ", ""));
			if(iIndex <= 0){
				LogUtil.info("serverIndex配置可能有问题：（"+serverIndex+"）");
			}else{
				LogUtil.info("serverIndex配置正确!");
			}
			
			//检测DevMode
			if("true".equalsIgnoreCase(devMode.toString())){
				LogUtil.info("DevMode指示为true,生产环境不推荐此设置!");
			}else{
				LogUtil.info("DevMode配置正确!");
			}
			//增加一次监测成功次数
			addSuccessCount();
			result = true;
		}catch (Exception e) {
			LogUtil.error(e.getMessage());
			result = false;
		}
		
		long end = System.currentTimeMillis();
		//增加执行时间
		addExecMill(String.valueOf(end-begin));
		return result;
	}
	
	private void addCount(){
		int mysqlCount = Integer.valueOf(MasterCache.responseResult.get(OtherCountKey) == null 
				|| MasterCache.responseResult.get(OtherCountKey) == ""? "0" 
				: String.valueOf(MasterCache.responseResult.get(OtherCountKey)));
		MasterCache.responseResult.put(OtherCountKey, ++mysqlCount);
	}
	
	private void addSuccessCount(){
		int successCount = Integer.valueOf(MasterCache.responseResult.get(OtherSuccessKey) == null 
				|| MasterCache.responseResult.get(OtherSuccessKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(OtherSuccessKey)));
		MasterCache.responseResult.put(OtherSuccessKey, ++successCount);
	}

	private void addExecMill(String mills){
		MasterCache.responseResult.put(OtherIdentityMillKey,mills);
	}
}
