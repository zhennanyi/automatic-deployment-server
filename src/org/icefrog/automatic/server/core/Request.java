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

import org.icefrog.automatic.server.util.LogUtil;

import net.sf.json.JSONObject;

/**
 * SMIS Request
 * 
 * @author icefrogsu@gmail.com
 */
public class Request {
	
	private long id;				//请求编号
	private String headingCode;		//头部报文,此报文需要和前端编码约束用于判断不同的操作,服务器做判断时应当严格区分大小写
	private String message;			//请求消息
	
	private static String formerJson;		//请求的原json数据
	
	public Request(){}
	
	/**
	 * 将请求内容解析成request对象		<br>
	 * 传入的json字符串必须包含key:	<br>
	 * <code>
	 * 	requestid	 int		<br>
	 * 	message		 string		<br>
	 * 	headingcode  string		<br>
	 * </code>
	 */
	public Request parse(String requestStr){
		formerJson = requestStr;
		requestStr = requestStr.replaceAll("\\\\", "\\\\\\\\");
		System.out.println(requestStr);
		Request request = new Request();
		try{
			JSONObject json = JSONObject.fromObject(requestStr);
			if(json != null){
				request.setId(Long.parseLong(String.valueOf(json.get("requestid"))));
				request.setMessage(String.valueOf(json.get("message")));
				request.setHeadingCode(String.valueOf(json.get("headingcode")));
			}else{
				//Skip
			}
		}catch (Exception e) {
			LogUtil.error("解析json为request对象时引发异常:"+e.getMessage());
			LogUtil.appendRuntimeLog("解析json为request对象时引发异常:"+e.getMessage());
		}
		return request;
	}
	
	/**
	 * 返回请求时最原始的json字符串
	 */
	public String getFormerJson(){
		return formerJson;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(obj instanceof Request){
			Request req = (Request)obj;
			if(this.id == req.getId()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "Request [id=" + id + ", headingCode=" + headingCode + ", message=" + message + "]";
	}
	
	/**
	 * 求编号,同一次握手 响应也应该使用此编号,由客户端生成 用于验证是否为同一次握手
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * 请求报文,可能为容易内容,可能是json,可能是xml 具体看客户端实现
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 识别代码
	 */
	public String getHeadingCode() {
		return headingCode;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setHeadingCode(String headingCode) {
		this.headingCode = headingCode;
	}
}
