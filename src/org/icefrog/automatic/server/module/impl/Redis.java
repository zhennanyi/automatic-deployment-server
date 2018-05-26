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

import redis.clients.jedis.Jedis;

public class Redis implements IModule{
	
	private final String RedisCountKey = "RedisCount";    //Redis一共检验的次数
	private final String RedisSuccessKey = "RedisSuccess";//Redis正确的次数
	private final String RedisIdentityMillKey = "RedisIdentityMill";//记录监测此项功能的毫秒数
	
	@Override
	public boolean identity(Properties properties) {
		LogUtil.info("开始检测Redis连通性");
		long begin = System.currentTimeMillis();
		//增加总检测次数
		addCount();
		boolean result = false;
		try{
			
			
			Object url = properties.get(MasterCache.RedisConfig.get("IpKeyName"));
			Object port = properties.get(MasterCache.RedisConfig.get("PortKeyName"));
			Object password = properties.get(MasterCache.RedisConfig.get("PasswordKeyName"));
			
			if(!CommonUtil.securityParams(url,port,password)){
				LogUtil.error("未检测到配置有Redis的任何有效信息!");
				addExecMill(String.valueOf(System.currentTimeMillis()-begin));
				return false;
			}
			
			
			LogUtil.debug("Redis url:"+url);
			LogUtil.debug("Redis port:"+port);
			LogUtil.debug("Redis password:"+password);
		
			Jedis jedis = new Jedis(url.toString(), Integer.parseInt(String.valueOf(port)));
			jedis.auth(password.toString());//设置密码认证
			String ping = jedis.ping();
			jedis.close();//关闭redis连接
			if("PONG".equalsIgnoreCase(ping)){
				LogUtil.info("Redis状态正常!");
				addSuccessCount();
				result = true;
			}else{
				LogUtil.error("Redis状态异常,我们在ping redis时返回了意料之外的结果:"+ping);
				result = false;
			}
		}catch (Exception e) {
			LogUtil.error("Redis可能存在问题,如果在意料之外应当立即处理此问题,Redis错误消息:"+e.getMessage());
			result = false;
		}
		long end = System.currentTimeMillis();
		//记录程序执行时间
		addExecMill(String.valueOf(end-begin));
		return result;
	}
	
	private void addCount(){
		int mysqlCount = Integer.valueOf(MasterCache.responseResult.get(RedisCountKey) == null 
				|| MasterCache.responseResult.get(RedisCountKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(RedisCountKey)));
		MasterCache.responseResult.put(RedisCountKey, ++mysqlCount);
	}
	
	private void addSuccessCount(){
		int successCount = Integer.valueOf(MasterCache.responseResult.get(RedisSuccessKey) == null 
				|| MasterCache.responseResult.get(RedisSuccessKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(RedisSuccessKey)));
		MasterCache.responseResult.put(RedisSuccessKey, ++successCount);
	}

	private void addExecMill(String mills){
		MasterCache.responseResult.put(RedisIdentityMillKey,mills);
	}
}
