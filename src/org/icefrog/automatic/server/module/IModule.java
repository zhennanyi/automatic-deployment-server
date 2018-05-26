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


package org.icefrog.automatic.server.module;

import java.util.Properties;

/**
 * 不同需要检测的模块都应当实现此接口<br>
 * 传入的properties参数作为每一个具体的配置文件对象,<br>
 * 通过此对象和应用上下文则可以获取到有用的信息并且进行校验<br>
 * 返回boolean值,如果检测属于业务正常,应该返回true. 否则返回false<br>
 * 具体返回true or false 取决与业务需要<br>
 * 
 * @author: 刘苏文   icefrogsu@gmail.com
 */
public interface IModule {
	
	boolean identity(Properties properties);
	
}
