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

/***
 * Fileupload Cache
 * 本地项目覆盖到服务器端时,用来临时缓存待保存的文件信息实体<br>
 * 具体包含文件的本地(客户端)地址,文件名,文件大小,和对应的服务端url.  包括最重要的cacheStatus.
 * cacheStatus用来标识当前是文件上传还是文件的信息上传. 在在interflow's Socket中有依据此字段
 * 作为判断标记.  因此后续实现在操作此字段时应当格外注意. 否则将造成不可预知的问题!
 * 
 * @author icefrogsu@gmail.com
 * 
 */
public class FileUploadCache {
	
	private static String localURL = "";
	
	private static String fileName = "";
	
	private static int    fileSize = 0;
	
	private static String serverDir = "";
	
	private static int cacheStatus = 0;//缓存状态.  0 结束  1. 等待文件
	
	/**类相关,记录缓存次数*/
	private static long cacheCount = 0;
	
	
	public static void clear(){
		setLocalURL("");
		setFileName("");
		setServerDir("");
		setFileSize(0);
		cacheCount++;
	}
	
	public long getCacheCount(){
		return cacheCount;
	}
	
	/**缓存当前待操作的文件路径,相对路径*/
	public static String getLocalURL() {
		return localURL;
	}

	/**缓存当前待操作的文件路径,相对路径*/
	public static void setLocalURL(String localURL) {
		FileUploadCache.localURL = localURL;
	}

	/**缓存当前待保存的文件名字,包含文件后缀*/
	public static String getFileName() {
		return fileName;
	}

	/**缓存当前待保存的文件名字,包含文件后缀*/
	public static void setFileName(String fileName) {
		FileUploadCache.fileName = fileName;
	}

	/**缓存当前待保存文件的具体字节数*/
	public static int getFileSize() {
		return fileSize;
	}

	/**缓存当前待保存文件的具体字节数*/
	public static void setFileSize(int fileSize) {
		FileUploadCache.fileSize = fileSize;
	}
	
	public static void setCacheStatus(int cacheStatus) {
		FileUploadCache.cacheStatus = cacheStatus;
	}
	
	public static int getCacheStatus() {
		return cacheStatus;
	}
	
	/**设置服务器需要处理的项目配置路径的key*/
	public static void setServerDir(String serverDir) {
		FileUploadCache.serverDir = serverDir;
	}
	
	/**获取服务器需要处理的项目配置路径的key*/
	public static String getServerDir() {
		return serverDir;
	}
	
	/**显示缓存的数据信息*/
	public static String showCache(){
		return String.format("LocalURL:%s,  FileName:%s,  FileSize:%d", getLocalURL(),getFileName(),getFileSize());
	}
}
