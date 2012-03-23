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

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;

public class GridTotalRows
{
	@Parameter
	private boolean excludeTotalRows;

	@InjectContainer
	private Grid grid;

	void afterRender(MarkupWriter writer)
	{
		if (excludeTotalRows)
			return;

		writer.element("span").text(String.format("%d-%d (%d)", startRow(), endRow(), totalRows()));
		writer.end();
	}

	int startRow()
	{
		if (totalRows() == 0)
			return 0;
		return (grid.getCurrentPage() - 1) * grid.getRowsPerPage() + 1;
	}

	int endRow()
	{
		if (totalRows() < grid.getCurrentPage() * grid.getRowsPerPage())
			return totalRows();

		return grid.getCurrentPage() * grid.getRowsPerPage();
	}

	int totalRows()
	{
		return grid.getDataSource().getAvailableRows();
	}
}
