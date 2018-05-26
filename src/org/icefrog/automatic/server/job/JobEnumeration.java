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


package org.icefrog.automatic.server.job;

public interface JobEnumeration {
	
	/**报文:用于获取当前进程所配置的服务器在线项目信息*/
	public static final String OnlineProjectList     = "b1bb0e5951a6a8f3ff7524cd0ad92ff1";
	
	/**报文:客户端请求服务器识别上传的客户端MD5值和服务器端MD5值*/
	public static final String MD5CodeCompare   	 = "a62e33be4197be8440f048da761755f7";
	
	/**报文:客户端请求服务端指定项目的MD5值列表*/
	public static final String GetServerMD5 		 = "0ea4ee48dd0258028cf7374de691b3b5";
	
	/**报文:客户端请求获得文件上传资质的秘钥*/
	public static final String GetPrivateKey 		 = "4f79a3158d691c5a7c1cda522763a247";
	
	/**报文:客户端首先提交文件的基本信息*/
	public static final String RequestFileInfo       = "3f74a7cf0b351cbaaf4875f7331bc25d";
	
	/**报文:客户端请求压缩指定服务器项目文件*/
	public static final String RequestCompressServer = "ed9ea8907f1963430bc8be4689d446b4";
	
}
