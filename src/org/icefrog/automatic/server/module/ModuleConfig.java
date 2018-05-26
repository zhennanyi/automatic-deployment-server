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

import java.util.ArrayList;
import java.util.List;

import org.icefrog.automatic.server.module.IModule;

/**
 * 需要验证的product配置器<br>
 * 维护数据时应当注意,配置的数据均使用static存储
 */
public class ModuleConfig {
	
	private static List<IModule> modules = new ArrayList<>();
	
	public static void addModule(IModule module){
		if(module != null){
			modules.add(module);
		}
	}
	
	public static List<IModule> getModules(){
		return modules;
	}
	
	public static void removeAll(){
		modules = new ArrayList<IModule>();
	}
	
	public static void removeAt(IModule module){
		modules.remove(module);
	}
	
	public static void removeAt(int index){
		modules.remove(index);
	}
}
