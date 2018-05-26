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

import java.io.IOException;
import java.util.Properties;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.module.IModule;
import org.icefrog.automatic.server.util.CommonUtil;
import org.icefrog.automatic.server.util.LogUtil;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

public class FTP implements IModule{

	
	private final String FTPCountKey = "FTPCount";    //FTP一共检验的次数
	private final String FTPSuccessKey = "FTPSuccess";//FTP正确的次数
	private final String FTPIdentityMillKey = "FTPIdentityMill";//记录监测此项功能的毫秒数
	
	@Override
	public boolean identity(Properties properties) {
		LogUtil.info("开始检测FTP连通性");
		long begin = System.currentTimeMillis();
		//增加总检测次数
		addCount();
		boolean result = false;
		try{
			
			Object ip = properties.get(MasterCache.FTPConfig.get("FTPIPKey"));
			Object username = properties.get(MasterCache.FTPConfig.get("FTPAccessKey"));
			Object password = properties.get(MasterCache.FTPConfig.get("FPTPasswordKey"));
			
			if(!CommonUtil.securityParams(ip,username,password)){
				LogUtil.error("未检测到配置有FTP的任何有效信息!");
				addExecMill(String.valueOf(System.currentTimeMillis()-begin));
				return false;
			}
			
			LogUtil.debug("FTP IP:"+ip.toString());
			LogUtil.debug("FTP Username:"+username.toString());
			LogUtil.debug("FTP Password:"+password.toString());
			
			FtpClient client = FtpClient.create(ip.toString());
			client.setConnectTimeout(200);
			client.login(username.toString(), password.toString().toCharArray());
			client.setBinaryType();
			boolean connected = client.isConnected();
			client.close();
			if(connected){
				result = true;
				addSuccessCount();
				LogUtil.info("FTP配置正常!");
			}else{
				result = false;
				LogUtil.error("FTP配置错误,无法根据已有信息链接到FTP!");
			}
			long end = System.currentTimeMillis();
			//记录执行时间
			addExecMill(String.valueOf(end-begin));
		}catch (Exception e) {
			result = false;
			LogUtil.error("FTP配置错误! "+e.getMessage());
		}
		return result;
	}
	
	
	public static void main(String[] args) throws FtpProtocolException, IOException {
		 FtpClient client = FtpClient.create("192.168.0.128");
		 client.login("vuser01", "1231234".toCharArray());
		 client.setBinaryType();
		 System.out.println(client.isConnected());
	}
	
	
	private void addCount(){
		int mysqlCount = Integer.valueOf(MasterCache.responseResult.get(FTPCountKey) == null 
				|| MasterCache.responseResult.get(FTPCountKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(FTPCountKey)));
		MasterCache.responseResult.put(FTPCountKey, ++mysqlCount);
	}
	
	private void addSuccessCount(){
		int successCount = Integer.valueOf(MasterCache.responseResult.get(FTPSuccessKey) == null
				|| MasterCache.responseResult.get(FTPSuccessKey) == "" ? "0" 
				: String.valueOf(MasterCache.responseResult.get(FTPSuccessKey)));
		MasterCache.responseResult.put(FTPSuccessKey, ++successCount);
	}
	
	private void addExecMill(String mills){
		MasterCache.responseResult.put(FTPIdentityMillKey,mills);
	}

}
