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


package org.icefrog.automatic.server.thread;

import org.icefrog.automatic.server.socket.CoreSocket;
import org.icefrog.automatic.server.socket.ISocket;
import org.icefrog.automatic.server.util.LogUtil;

public class CoreWorkThread extends Thread{
	
	@Override
	public void run() {
		LogUtil.debug("Core Thread成功分配到CPU时间片,加入执行队列成功!");
		ISocket socket = new CoreSocket();
		socket.work();
	}
}
