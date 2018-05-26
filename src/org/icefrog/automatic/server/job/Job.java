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

import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;

/**
 * 工作接口. 本接口的出现主要是应对多处理对多响应的情况(see:Spring.DispacterServlet.java)
 * 针对客户端不同的请求应当被分发到不同的工作实体中去处理 基于一次请求一次响应  在实现dosome方法时. 
 * 一定要返回有效的response.  至少要保证response.id 具有和传入的request.id 一致的值.
 * 只有在遵循此规则的前提下 .客户端和服务端才可以通过request.id and response.id 区分是否
 * 为一次同步的握手
 */
public interface Job {
	
	public Response dosome(Request request);
	
}
