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

public class CommonUtil {
	
	
	public static boolean securityParams(Object... params){
		if(params == null || params.length == 0){
			return false;
		}
		for (Object item : params) {
			if(item != null){
				if(item instanceof String){
					String tempItem = String.valueOf(item);
					if("".equals(tempItem.replaceAll(" ", "")) || "null".equals(tempItem.replaceAll(" ", "").toLowerCase())){
						return false;
					}else{
						return true;
					}
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
}
