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


package org.icefrog.automatic.server.socket;

import java.io.File;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.module.IModule;
import org.icefrog.automatic.server.module.ModuleConfig;
import org.icefrog.automatic.server.util.JsonUtil;
import org.icefrog.automatic.server.util.LogUtil;
import org.icefrog.automatic.server.util.PropertiesUtil;

/**
 * Core Socket 目前单独用于检测服务器端项目配置文件连通性检测
 * 
 * @author icefrogsu@gmail.com
 */
public class CoreSocket implements ISocket{

	private static final String CheckedProductCountKey = "CheckedProductCount";
	
	
	@Override
	public void init(){
	
	}

	@Override
	public void work() {
		while(true){
			try{
				PropertiesUtil putil = new PropertiesUtil(MasterCache.SystemRuntimePath);
				Properties properties = putil.getProperties();
				
				ServerSocket server = new ServerSocket(MasterCache.MachinePort);
				Socket socket = server.accept();
				
				//获取所有需要检测的项目路径mainconfig.properties
				Set<Object> set = properties.keySet();
				Iterator<Object> iterator = set.iterator();
				
				int projectCount = 0;
				while(iterator!=null && iterator.hasNext()){
					String currentKeyName = (String) iterator.next();
					if(currentKeyName.startsWith("check")){
						LogUtil.info("---------->> 切换到项目:  "+currentKeyName);
						projectCount++; //累加一个项目数量
						String path = (String) properties.get(currentKeyName);
						PropertiesUtil pu = null;
						Properties innerProperties = null;
						if(path.endsWith("propertise")){
							try{
								//配置的路径已经包含文件,则直接构建Properties对象
								pu = new PropertiesUtil(path);
								innerProperties = pu.getProperties();
							}catch (Exception e) {
								LogUtil.error("路径 "+path+" 找不到,如果确保该项目有此配置文件却看到此错误,应当立刻检查. 否则可忽略!");
								continue;
							}
						}else{
							try{
								pu = new PropertiesUtil(path + File.separator + MasterCache.MasterConfigName);
								innerProperties = pu.getProperties();
							}catch (Exception e) {
								LogUtil.error("路径 "+path + File.separator + MasterCache.MasterConfigName+" 找不到,如果确保该项目有此配置文件却看到此错误,应当立刻检查. 否则可忽略!");
								continue;
							}
						}
						
						//获取已配置的需要检测的项
						List<IModule> modules = ModuleConfig.getModules();
						for (IModule item : modules) {
							item.identity(innerProperties);
						}
					}else{
						//系统配置,不需要检测.Skip
					}
				}
				LogUtil.info("Done");
				MasterCache.responseResult.put(CheckedProductCountKey, projectCount);
				String json = JsonUtil.toJsonString();
				
				OutputStream out = socket.getOutputStream();
				out.write(json.getBytes(MasterCache.Encode));
				out.flush();
				out.close();
				socket.close();
				server.close();
				JsonUtil.clear();
				MasterCache.responseResult.clear();
			
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.appendRuntimeLog("服务器主程序出现异常,请参阅异常信息:"+e.getMessage());
				break;
			}
		}
	}

	@Override
	public void destroy() {
	}
	
}