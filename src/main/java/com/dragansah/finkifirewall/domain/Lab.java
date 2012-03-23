// Copyright 2012 Dragan Sahpaski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.dragansah.finkifirewall.domain;

import org.apache.tapestry5.beaneditor.NonVisual;

public class Lab
{
	private String code;
	private String name;
	private String activeProfile;
	private String ipClass;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getActiveProfile()
	{
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile)
	{
		this.activeProfile = activeProfile;
	}

	@NonVisual
	public String getIpClass()
	{
		return ipClass;
	}

	public void setIpClass(String ipClass)
	{
		this.ipClass = ipClass;
	}
}
