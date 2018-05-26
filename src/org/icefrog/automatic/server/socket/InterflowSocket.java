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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.icefrog.automatic.server.cache.FileUploadCache;
import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;
import org.icefrog.automatic.server.job.JobEnumeration;
import org.icefrog.automatic.server.job.impl.DoUploadImpl;
import org.icefrog.automatic.server.job.impl.GetServerMD5InfoImpl;
import org.icefrog.automatic.server.job.impl.MD5CodeCompareImpl;
import org.icefrog.automatic.server.job.impl.OnlineProjectGeterImpl;
import org.icefrog.automatic.server.job.impl.RecordFileInfoImpl;
import org.icefrog.automatic.server.job.impl.RequestCompressImpl;
import org.icefrog.automatic.server.job.impl.RequestRequiredPrivateKeyImpl;
import org.icefrog.automatic.server.job.impl.UnknowJob;
import org.icefrog.automatic.server.util.LogUtil;

/**
 * 具备理论无限次握手的Socket通信 并且本类充当不同请求类型的分发角色. 所有请求必须路由此方法
 * 并且按照特定的编码条例(request.headingcode) 进行分发<br>
 */
public class InterflowSocket implements ISocket {

	@Override
	public void init() {

	}

	@Override
	public void work() {
		while (true) {
			System.out.println("开启监听...");
			ServerSocket server = null;
			Socket socket = null;
			try {
				server = new ServerSocket(MasterCache.InterflowPort);
				socket = server.accept();
				InputStream in = socket.getInputStream();
				System.out.println("状态:" + FileUploadCache.getCacheStatus());
				if (in != null) {
					// request
					// 检查文件上传是否具备缓存,如果有,则直接跳转到文件处理模块
					if (FileUploadCache.getCacheStatus() == 1) {
						System.out.println("进入状态1的比对!");
						Response response = new DoUploadImpl().dosome(in);
						OutputStream out = socket.getOutputStream();
						if (out != null) {
							out.write(response.toString().getBytes(MasterCache.Encode));
							out.flush();
							out.close();
						}
						// free resources
						socket.close();
						server.close();
						continue;
					}
					byte[] inputedData = new byte[1024 * 1024 * 10];// Using
																	// 1024
																	// bytes is
																	// not
																	// enough
					int inputedCount = in.read(inputedData);
					String informJson = new String(inputedData, 0, inputedCount, MasterCache.Encode);
					LogUtil.appendRuntimeLog(informJson);
					Request request = new Request().parse(informJson);
					// 判断请求报文,通过和客户端编码约束 不同的报文处理/响应不同的数据 为了强化编码体验.
					// 应当将具体工作的代码块使用接口进行约束
					Job job = null;
					switch (request.getHeadingCode()) {
					case JobEnumeration.OnlineProjectList:
						job = new OnlineProjectGeterImpl();
						break;
					case JobEnumeration.MD5CodeCompare:
						job = new MD5CodeCompareImpl();
						break;
					case JobEnumeration.GetServerMD5:
						job = new GetServerMD5InfoImpl();
						break;
					case JobEnumeration.GetPrivateKey:
						job = new RequestRequiredPrivateKeyImpl();
						break;
					case JobEnumeration.RequestFileInfo:
						job = new RecordFileInfoImpl();
						break;
					case JobEnumeration.RequestCompressServer:
						job = new RequestCompressImpl();
						break;
					default:
						job = new UnknowJob();
						break;
					}
					// Call the dosome method and response returned to client
					Response response = job.dosome(request);
					OutputStream out = socket.getOutputStream();
					if (out != null) {
						out.write(response.toString().getBytes(MasterCache.Encode));
						out.flush();
						out.close();
					}
				}
			} catch (java.net.BindException binde) {
				LogUtil.error("InterflowSocket打开失败!地址已经在使用,请先关闭之前的实例!");
				LogUtil.appendRuntimeLog("InterflowSocket打开失败!地址已经在使用,请先关闭之前的实例!");
				System.exit(-1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null) {
						socket.close();
					}
					if (server != null) {
						server.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

}
