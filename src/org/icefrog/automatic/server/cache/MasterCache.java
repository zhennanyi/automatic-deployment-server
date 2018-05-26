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


package org.icefrog.automatic.server.cache;

import java.util.HashMap;
import java.util.Map;

import org.icefrog.automatic.server.core.RuntimeEnvironment;

/**
 * 主进程运行上下文<br>
 * MasterCache内存缓存对于smis(server/client)而言. 格外的重要 . 
 * 在程序运行初期,绝大部分项都会被加载到内存中.(read to config file)<br>
 * 由此可见,本工具暂时不支持配置文件热修改.  并且本缓存类提供static code chunk 
 * 用于初始化每一个项目中需要被检测的标准模块.(后续版本升级可能会移除,更改为前端动态指定)
 * 
 * @author icefrogsu@gmail.com
 */
public class MasterCache {
	
	//final
	public static final String RuntimeLogStuffix     = "log";
	
	//temp,请注意数据清理
	public static Map<String, Object> responseResult = new HashMap<String, Object>();
	
	/*
	 * 文件覆盖上传时所需要的公钥秘钥,如果不匹配将忽略文件上传的覆盖请求. 
	 * 生成公钥和秘钥应当统统由服务端生成维护. 并且一个任务段永远只会存在一组公秘钥 
	 * 也即是处于安全考虑,不允许同时多台客户端对服务器文件进行操作
	 */
	public static String SMISComparisonPublicKey     = "";
	public static String SMISComparisonPrivateKey    = "";
	
	public static String Encode 					 = "UTF-8";
	public static String SystemRuntimePath  		 = "/";
	public static String MasterConfigName   		 = "mainconfig.propertise";
	public static String ComparisonConfigName 		 = "comparison.properties";
	public static int MachinePort 					 = 60001; //本次实例的默认端口,可通过配置文件配置,运行初期会加载到内存中
	public static int InterflowPort 				 = 60002;
	public static String RuntimeLogdir 				 = "";	 //本工具运行时需要存放日志的目录,目录即可不需要文件名(文件名为今日时间)
	public static String DatetimePattern 			 = "";
	public static String DatePattern 				 = "";
	public static String ServerCompressDir           = "";
	public static RuntimeEnvironment environment  	 = RuntimeEnvironment.Debug;
	
	
	public static Map<String, String> MySQLConfig    = new HashMap<String, String>();
	public static Map<String, String> RedisConfig    = new HashMap<String, String>();
	public static Map<String, String> ZBusConfig     = new HashMap<String, String>();
	public static Map<String, String> FTPConfig      = new HashMap<String, String>();
	public static String			   RootDomain    = null;
	public static String 			   ServerIndex   = null;
	public static String 			   DevMode       = null;
	public static String 			   ShowSQL       = null;
	
	static{
		//配置mysql产品配置的key
		MySQLConfig.put("UrlKeyName", "master.database.jdbcUrl");
		MySQLConfig.put("UserName", "master.database.user");
		MySQLConfig.put("Password", "master.database.password");
		
		//配置Redis产品配置的key
		RedisConfig.put("IpKeyName", "host");
		RedisConfig.put("PortKeyName", "port");
		RedisConfig.put("PasswordKeyName", "password");
		
		//配置ZBus产品的配置key
		ZBusConfig.put("AddressKeyName", "brokerAddress");
		ZBusConfig.put("ZbusHandlerSwitch", "ZbusHandlerSwitch");
		
		//配置FTP产品的配置key
		FTPConfig.put("FTPIPKey", "ftpIP");
		FTPConfig.put("FTPAccessKey", "ftpAcc");
		FTPConfig.put("FPTPasswordKey", "ftpPass");
		
		//配置RootDomain
		RootDomain = "rootdomain";
		
		//配置ServerIndex
		ServerIndex = "serverIndex";
		
		//配置DevMode
		DevMode = "devMode";
		
		//配置ShowSQL
		ShowSQL = "showsql";
	}
	
}
