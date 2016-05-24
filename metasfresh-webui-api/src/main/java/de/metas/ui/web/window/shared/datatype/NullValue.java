package de.metas.ui.web.window.shared.datatype;

/*
 * #%L
 * de.metas.ui.web.vaadin
 * %%
 * Copyright (C) 2016 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

public final class NullValue
{
	public static final Object valueOrNull(final Object value)
	{
		return value == null ? instance : value;
	}
	
	public static final boolean isNull(final Object value)
	{
		return value == null || value == instance;
	}
	
	public static final transient NullValue instance = new NullValue();
	
	private NullValue()
	{
		super();
	}
	
	@Override
	public String toString()
	{
		return "";
	}
}