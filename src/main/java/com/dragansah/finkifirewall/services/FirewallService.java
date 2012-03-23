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

package com.dragansah.finkifirewall.services;

import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

import com.dragansah.finkifirewall.domain.FirewallProfile;
import com.dragansah.finkifirewall.domain.Lab;
import com.dragansah.finkifirewall.domain.User;

@UsesMappedConfiguration(key = String.class, value = Asset.class)
public interface FirewallService
{
	List<Lab> findAllLabs();

	List<FirewallProfile> findAllFirewallProfiles();

	Lab findLabByCode(String code);

	FirewallProfile findFirewallProfileByName(String name);

	void setActiveProfileForLab(Lab selectedLab, FirewallProfile profile);

	void removeLabProfile(Lab lab);

	List<User> findAllUsers();
	
	boolean userExists(String username);
}
