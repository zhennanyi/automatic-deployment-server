package org.icefrog.automatic.server.job.impl;

import org.icefrog.automatic.server.core.Request;
import org.icefrog.automatic.server.core.Response;
import org.icefrog.automatic.server.job.Job;

/**
 * Describes requests that are not normally resolved. 
 * As you can see, the implementation simply responds
 */
public class UnknowJob implements Job{
	
	@Override
	public Response dosome(Request request) {
		Response response = new Response();
		response.setId(request.getId());
		response.setMessage("unknown request");
		return response;
	}
}
