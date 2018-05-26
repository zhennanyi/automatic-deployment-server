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


package org.icefrog.automatic.server.job.impl;

import java.util.UUID;

import org.icefrog.automatic.server.cache.MasterCache;
import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;

//客户端请求获得项目覆盖上传的秘钥
public class RequestRequiredPrivateKeyImpl implements Job{

	@Override
	public Response dosome(Request request) {
		
		String publicKey = UUID.randomUUID().toString().replaceAll("-", "");
		String privateKey = UUID.randomUUID().toString().replaceAll("-", "");
		MasterCache.SMISComparisonPublicKey = publicKey;
		MasterCache.SMISComparisonPrivateKey = privateKey;
		
		Response response = new Response();
		response.setId(request.getId());
		response.setMessage(MasterCache.SMISComparisonPrivateKey);
		return response;
	}
}
