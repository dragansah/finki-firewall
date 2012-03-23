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

package com.dragansah.finkifirewall.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.AbstractSelectModel;

import com.dragansah.finkifirewall.domain.FirewallProfile;
import com.dragansah.finkifirewall.domain.Lab;
import com.dragansah.finkifirewall.services.FirewallService;

public class Index
{
	@Inject
	private FirewallService firewallService;

	@Property
	private Lab lab;

	public List<Lab> getLabs()
	{
		return firewallService.findAllLabs();
	}

	@Property
	private FirewallProfile profile;

	public List<FirewallProfile> getProfiles()
	{
		return firewallService.findAllFirewallProfiles();
	}

	public SelectModel getLabModel()
	{
		return new AbstractSelectModel()
		{
			@Override
			public List<OptionModel> getOptions()
			{
				List<OptionModel> options = new ArrayList<OptionModel>();
				for (Lab lab : firewallService.findAllLabs())
					options.add(new OptionModelImpl(lab.getCode(), lab));
				return options;
			}

			@Override
			public List<OptionGroupModel> getOptionGroups()
			{
				return null;
			}
		};
	}

	@Inject
	private ValueEncoderSource encoderSource;

	public ValueEncoder<Lab> getLabEncoder()
	{
		return encoderSource.getValueEncoder(Lab.class);
	}

	@Property
	private Lab selectedLab;

	void onSuccessFromActivateProfileForm(FirewallProfile profile)
	{
		firewallService.setActiveProfileForLab(selectedLab, profile);
	}

	void onRemoveLabProfile(Lab lab)
	{
		firewallService.removeLabProfile(lab);
	}

}
