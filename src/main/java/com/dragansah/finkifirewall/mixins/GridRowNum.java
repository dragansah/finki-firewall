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

package com.dragansah.finkifirewall.mixins;

import java.lang.annotation.Annotation;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;

public class GridRowNum
{
	@Parameter
	private boolean excludeRowNum;

	@InjectContainer
	private Grid grid;

	class RowNumPropertyConduit implements PropertyConduit
	{
		private int i;

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
		{
			return null;
		}

		@Override
		public void set(Object instance, Object value)
		{
		}

		@Override
		public Class<?> getPropertyType()
		{
			return String.class;
		}

		public void reset()
		{
			i = 0;
		}

		@Override
		public Object get(Object instance)
		{
			if (i >= grid.getRowsPerPage() * grid.getCurrentPage()
					|| i < grid.getRowsPerPage() * (grid.getCurrentPage() - 1)
					|| i >= grid.getDataSource().getAvailableRows())
				i = grid.getRowsPerPage() * (grid.getCurrentPage() - 1);

			return ++i;
		}
	}

	void cleanupRender()
	{
		if (excludeRowNum || grid.getDataSource() == null
				|| grid.getDataSource().getAvailableRows() == 0)
			return;
		((RowNumPropertyConduit) grid.getDataModel().get("rownum").getConduit()).reset();
	}

	void beginRender()
	{
		if (excludeRowNum || grid.getDataSource() == null
				|| grid.getDataSource().getAvailableRows() == 0)
			return;

		try
		{
			grid.getDataModel().reorder("rownum");
		}
		catch (RuntimeException exception)
		{
			grid.getDataModel().add("rownum", new RowNumPropertyConduit()).sortable(false);

			grid.getDataModel().reorder("rownum");
		}
	}
}
