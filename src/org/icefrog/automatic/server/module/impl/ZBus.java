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
import org.icefrog.automatic.server.util.HttpUtil;
import org.icefrog.automatic.server.util.LogUtil;

public class ZBus implements IModule{
	
	private final String ZBusCountKey = "ZBusCount";    //ZBus一共检验的次数
	private final String ZBusSuccessKey = "ZBusSuccess";//ZBus正确的次数
	private final String ZBusIdentityMillKey = "ZBusIdentityMill";//记录监测此项功能的毫秒数
	
	@Override
	public boolean identity(Properties properties){
		LogUtil.info("开始检测Zbus连通性");
		long begin = System.currentTimeMillis();
		
		//添加检测ZBus次数
		addCount();
		boolean result = false;
		try{
			//获取到的url是不包含网络协议的(http://)
			Object url = properties.get(MasterCache.ZBusConfig.get("AddressKeyName"));
			
			if(!CommonUtil.securityParams(url)){
				LogUtil.error("未检测到配置有ZBus的任何有效信息!");
				addExecMill(String.valueOf(System.currentTimeMillis()-begin));
				return false;
			}
			
			LogUtil.debug("获取到的ZBus的IP and Port为:"+url);
			LogUtil.debug("正在尝试使用http协议请求ZBus并获取响应");
		
			int code = HttpUtil.getResponseCode("http://"+url.toString(), HttpUtil.GET, 1500);
			if(code == 200){
				LogUtil.info("ZBus配置正常! 响应状态码为:"+code);
				addSuccessCount();
				result = true;
			}else{
				LogUtil.error("ZBus配置可能存在问题! 响应状态码为:"+code);
				result = false;
			}
		}catch (Exception e) {
			result = false;
			LogUtil.error("ZBus配置可能存在问题! "+e.getMessage());
		}
		
	    
	    long end = System.currentTimeMillis();
	    addExecMill(String.valueOf(end-begin));
	    
	    return result;
	}

	private void addCount(){
		int mysqlCount = Integer.valueOf(MasterCache.responseResult.get(ZBusCountKey) == null 
				|| MasterCache.responseResult.get(ZBusCountKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(ZBusCountKey)));
		MasterCache.responseResult.put(ZBusCountKey, ++mysqlCount);
	}
	
	private void addSuccessCount(){
		int successCount = Integer.valueOf(MasterCache.responseResult.get(ZBusSuccessKey) == null 
				|| MasterCache.responseResult.get(ZBusSuccessKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(ZBusSuccessKey)));
		MasterCache.responseResult.put(ZBusSuccessKey, ++successCount);
	}
	private void addExecMill(String mills){
		MasterCache.responseResult.put(ZBusIdentityMillKey,mills);
	}
	
}
