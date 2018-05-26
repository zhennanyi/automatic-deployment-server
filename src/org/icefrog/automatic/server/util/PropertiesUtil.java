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


package org.icefrog.automatic.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	Properties properties = new Properties();
	
	public PropertiesUtil(String path) throws IOException{
		InputStream in = new FileInputStream(path);
		this.properties.load(in);
	}
	
	public PropertiesUtil(InputStream in) throws IOException{
		this.properties.load(in);
	}
	
	public PropertiesUtil(File file) throws IOException{
		this.properties.load(new FileInputStream(file));
	}
	
	public Properties getProperties(){
		return this.properties;
	}
	
	public Object get(Object key){
		return this.properties.get(key);
	}
}
