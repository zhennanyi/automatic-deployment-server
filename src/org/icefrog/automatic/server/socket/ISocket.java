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

/**
 * 本实例中Socket规约,在使用Socket时,应当将执行过程放在一条异步的线程中
 */
public interface ISocket {
	
	/**
	 * initializing resource
	 */
	public void init();
	
	/**
	 * work
	 */
	public void work();
	
	/**
	 * destroy resource
	 */
	public void destroy();
	
}
