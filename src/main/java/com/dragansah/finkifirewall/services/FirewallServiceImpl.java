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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Asset;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.dragansah.finkifirewall.Constants;
import com.dragansah.finkifirewall.domain.FirewallProfile;
import com.dragansah.finkifirewall.domain.Lab;

public class FirewallServiceImpl implements FirewallService
{
	private Asset labsFile;

	private Asset profilesFile;

	public FirewallServiceImpl(Map<String, Asset> config)
	{
		labsFile = config.get(Constants.LABS_FILE_PATH);
		profilesFile = config.get(Constants.PROFILES_FILE_PATH);
	}

	@Override
	public List<Lab> findAllLabs()
	{
		return findAll(labsFile, Lab.class);
	}

	@Override
	public List<FirewallProfile> findAllFirewallProfiles()
	{
		return findAll(profilesFile, FirewallProfile.class);
	}

	@Override
	public Lab findLabByCode(String code)
	{
		if (code == null)
			return null;

		for (Lab lab : findAllLabs())
			if (lab.getCode().equals(code))
				return lab;

		return null;
	}

	@Override
	public FirewallProfile findFirewallProfileByName(String name)
	{
		if (name == null)
			return null;

		for (FirewallProfile profile : findAllFirewallProfiles())
			if (profile.getName().equals(name))
				return profile;

		return null;
	}

	@SuppressWarnings("deprecation")
	private <T> List<T> findAll(Asset file, final Class<T> clazz)
	{
		try
		{
			return new ObjectMapper().readValue(new File(file.getResource().toURL().toURI()),
					TypeFactory.collectionType(List.class, clazz));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setActiveProfileForLab(Lab selectedLab, FirewallProfile profile)
	{
		List<Lab> labs = findAllLabs();
		for (Lab lab : labs)
			if (lab.getName().equals(selectedLab.getName()))
				lab.setActiveProfile(profile.getName());

		selectedLab.setActiveProfile(profile.getName());
		try
		{
			new ObjectMapper().writeValue(new File(labsFile.getResource().toURL().toURI()), labs);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		refreshClearOsProfile();
	}

	@Override
	public void removeLabProfile(Lab selectedLab)
	{
		List<Lab> labs = findAllLabs();
		for (Lab lab : labs)
			if (lab.getName().equals(selectedLab.getName()))
				lab.setActiveProfile("");

		try
		{
			new ObjectMapper().writeValue(new File(labsFile.getResource().toURL().toURI()), labs);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		refreshClearOsProfile();
	}

	private void refreshClearOsProfile()
	{

	}
}