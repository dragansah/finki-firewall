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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.internal.util.AbstractResource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ClasspathAssetAliasManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import com.dragansah.finkifirewall.Constants;
import com.dragansah.finkifirewall.domain.FirewallProfile;
import com.dragansah.finkifirewall.domain.Lab;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class FinkiFirewallModule
{
	public static void bind(ServiceBinder binder)
	{
		binder.bind(FirewallService.class, FirewallServiceImpl.class);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration)
	{
		configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
		configuration.override(SymbolConstants.PRODUCTION_MODE, false);
	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, Object> configuration)
	{
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en, MK_mk");
	}

	@Contribute(ClasspathAssetAliasManager.class)
	public static void setupAssetsAliases(MappedConfiguration<String, String> configuration)
	{
		configuration.add("finkifirewall-0.0.1", "com/dragansah/finkifirewall");
	}

	@Contribute(FirewallService.class)
	public static void provideJsonDataSources(MappedConfiguration<String, Asset> config,
			AssetSource assetSource, Logger logger)
	{
		final String labsFilePath = System.getProperty(Constants.LABS_FILE_PATH);
		final String profilesFilePath = System.getProperty(Constants.PROFILES_FILE_PATH);
		final String clearOsConfigFilePath = System
				.getProperty(Constants.CLEAR_OS_CONFIG_FILE_PATH);
		final String usersFilePath = System.getProperty(Constants.USERS_FILE_PATH);
		final String clearOsStaticConfigFilePath = System
				.getProperty(Constants.CLEAR_OS_STATIC_CONFIG_FILE_PATH);

		logger.debug("Contributing to FirewallService");
		logger.debug("labsFilePath:" + labsFilePath);
		logger.debug("profilesFilePath:" + profilesFilePath);
		logger.debug("clearOsConfigFilePath:" + clearOsConfigFilePath);
		logger.debug("clearOsStaticConfigFilePath:" + clearOsStaticConfigFilePath);
		logger.debug("usersFilePath:" + usersFilePath);

		Asset labsFile = null, profilesFile = null, clearOsConfigFile = null, clearOsStaticConfigFile = null, usersFile = null;

		if (labsFilePath != null)
			labsFile = fromFilePath(labsFilePath);

		if (profilesFilePath != null)
			profilesFile = fromFilePath(profilesFilePath);

		if (clearOsConfigFilePath != null)
			clearOsConfigFile = fromFilePath(clearOsConfigFilePath);
		
		if (clearOsStaticConfigFilePath != null)
			clearOsStaticConfigFile = fromFilePath(clearOsStaticConfigFilePath);

		if (usersFilePath != null)
			usersFile = fromFilePath(usersFilePath);

		if (labsFilePath == null)
			labsFile = assetSource.getClasspathAsset("com/dragansah/finkifirewall/labs.json");

		if (profilesFilePath == null)
			profilesFile = assetSource
					.getClasspathAsset("com/dragansah/finkifirewall/firewall-profiles.json");

		if (clearOsConfigFilePath == null)
			clearOsConfigFile = assetSource
					.getClasspathAsset("com/dragansah/finkifirewall/clearos.config");

		if (clearOsStaticConfigFilePath == null)
			clearOsStaticConfigFile = assetSource
					.getClasspathAsset("com/dragansah/finkifirewall/clearos-static.config");

		if (usersFilePath == null)
			usersFile = assetSource.getClasspathAsset("com/dragansah/finkifirewall/users.json");

		config.add(Constants.LABS_FILE_PATH, labsFile);
		config.add(Constants.PROFILES_FILE_PATH, profilesFile);
		config.add(Constants.CLEAR_OS_CONFIG_FILE_PATH, clearOsConfigFile);
		config.add(Constants.USERS_FILE_PATH, usersFile);
		config.add(Constants.CLEAR_OS_STATIC_CONFIG_FILE_PATH, clearOsStaticConfigFile);
	}

	private static Asset fromFilePath(final String filePath)
	{
		return new Asset()
		{
			@Override
			public String toClientURL()
			{
				try
				{
					return new File(filePath).toURI().toURL().toString();
				}
				catch (MalformedURLException e)
				{
					throw new RuntimeException(e);
				}
			}

			@Override
			public Resource getResource()
			{
				return formFilePath(filePath);
			}
		};
	}

	private static Resource formFilePath(final String profilesFilePath)
	{
		return new AbstractResource(profilesFilePath)
		{
			@Override
			public URL toURL()
			{
				try
				{
					return new File(profilesFilePath).toURI().toURL();
				}
				catch (MalformedURLException e)
				{
					throw new RuntimeException(e);
				}
			}

			@Override
			protected Resource newResource(String path)
			{
				return null;
			}
		};
	}

	/**
	 * This is a service definition, the service will be named "TimingFilter". The interface,
	 * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
	 * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
	 * appropriate Logger instance. Requests for static resources are handled at a higher level, so
	 * this filter will only be invoked for Tapestry related requests.
	 * <p/>
	 * <p/>
	 * Service builder methods are useful when the implementation is inline as an inner class (as
	 * here) or require some other kind of special initialization. In most cases, use the static
	 * bind() method instead.
	 * <p/>
	 * <p/>
	 * If this method was named "build", then the service id would be taken from the service
	 * interface and would be "RequestFilter". Since Tapestry already defines a service named
	 * "RequestFilter" we use an explicit service id that we can reference inside the contribution
	 * method.
	 */
	public RequestFilter buildTimingFilter(final Logger log)
	{
		return new RequestFilter()
		{
			public boolean service(Request request, Response response, RequestHandler handler)
					throws IOException
			{
				long startTime = System.currentTimeMillis();

				try
				{
					// The responsibility of a filter is to invoke the corresponding method
					// in the handler. When you chain multiple filters together, each filter
					// received a handler that is a bridge to the next filter.

					return handler.service(request, response);
				}
				finally
				{
					long elapsed = System.currentTimeMillis() - startTime;

					log.info(String.format("Request time: %d ms", elapsed));
				}
			}
		};
	}

	/**
	 * This is a contribution to the RequestHandler service configuration. This is how we extend
	 * Tapestry using the timing filter. A common use for this kind of filter is transaction
	 * management or security. The @Local annotation selects the desired service by type, but only
	 * from the same module. Without @Local, there would be an error due to the other service(s)
	 * that implement RequestFilter (defined in other modules).
	 */
	public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
			@Local RequestFilter filter)
	{
		// Each contribution to an ordered configuration has a name, When necessary, you may
		// set constraints to precisely control the invocation order of the contributed filter
		// within the pipeline.

		configuration.add("Timing", filter);
	}

	/**
	 * Contribute a {@link ValueEncoder} for {@link FileModel}.
	 */
	public static void contributeValueEncoderSource(
			MappedConfiguration<Class<?>, ValueEncoder<?>> conf,
			final FirewallService firewallService)
	{
		conf.add(Lab.class, new ValueEncoder<Lab>()
		{
			public String toClient(Lab value)
			{
				return value.getCode();
			}

			public Lab toValue(String code)
			{
				return firewallService.findLabByCode(code);
			}
		});

		conf.add(FirewallProfile.class, new ValueEncoder<FirewallProfile>()
		{
			public String toClient(FirewallProfile value)
			{
				return value.getName();
			}

			public FirewallProfile toValue(String name)
			{
				return firewallService.findFirewallProfileByName(name);
			}
		});
	}
}
