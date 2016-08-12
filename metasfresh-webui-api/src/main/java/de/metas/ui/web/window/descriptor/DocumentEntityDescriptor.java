package de.metas.ui.web.window.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import de.metas.printing.esb.base.util.Check;

/*
 * #%L
 * metasfresh-webui-api
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

public class DocumentEntityDescriptor
{
	public static final Builder builder()
	{
		return new Builder();
	}

	private final String id;
	private final String detailId;
	private final List<DocumentFieldDescriptor> fields;
	private final Map<String, DocumentEntityDescriptor> includedEntitiesByDetailId;

	private final DocumentEntityDataBindingDescriptor dataBinding;

	private final DocumentFieldDependencyMap dependencies;

	// Legacy
	private final int tabNo;

	private DocumentEntityDescriptor(final Builder builder)
	{
		super();
		id = Preconditions.checkNotNull(builder.id, "id is null");
		detailId = builder.detailId;
		fields = ImmutableList.copyOf(builder.fields);
		includedEntitiesByDetailId = ImmutableMap.copyOf(builder.includedEntitiesByDetailId);
		dataBinding = Preconditions.checkNotNull(builder.dataBinding, "dataBinding not null");
		dependencies = builder.buildDependencies();

		// legacy:
		tabNo = builder.tabNo;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("fields", fields)
				.add("entityDataBinding", dataBinding)
				.add("includedEntitites", includedEntitiesByDetailId.isEmpty() ? null : includedEntitiesByDetailId)
				.toString();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (!(obj instanceof DocumentEntityDescriptor))
		{
			return false;
		}

		final DocumentEntityDescriptor other = (DocumentEntityDescriptor)obj;
		return Objects.equals(this.id, other.id);
	}

	public String getDetailId()
	{
		return detailId;
	}

	public List<DocumentFieldDescriptor> getFields()
	{
		return fields;
	}

	public Collection<DocumentEntityDescriptor> getIncludedEntities()
	{
		return includedEntitiesByDetailId.values();
	}

	public DocumentEntityDescriptor getIncludedEntityByDetailId(final String detailId)
	{
		final DocumentEntityDescriptor includedEntityDescriptor = includedEntitiesByDetailId.get(detailId);
		if (includedEntityDescriptor == null)
		{
			throw new IllegalArgumentException("No included entity found for detailId=" + detailId + " in " + this);
		}
		return includedEntityDescriptor;
	}

	public DocumentEntityDataBindingDescriptor getDataBinding()
	{
		return dataBinding;
	}

	public DocumentFieldDependencyMap getDependencies()
	{
		return dependencies;
	}

	// legacy
	public int getTabNo()
	{
		return tabNo;
	}

	public static final class Builder
	{
		private String id;
		private final List<DocumentFieldDescriptor> fields = new ArrayList<>();
		private final Map<String, DocumentEntityDescriptor> includedEntitiesByDetailId = new LinkedHashMap<>();
		private DocumentEntityDataBindingDescriptor dataBinding;
		private String detailId;

		// Legacy
		private Integer tabNo;

		private Builder()
		{
			super();
		}

		public DocumentEntityDescriptor build()
		{
			return new DocumentEntityDescriptor(this);
		}

		public Builder setId(final int id)
		{
			Check.assume(id > 0, "id > 0 but it was {}", id);
			this.id = String.valueOf(id);
			return this;
		}

		public Builder setDetailId(final String detailId)
		{
			this.detailId = detailId;
			return this;
		}

		public Builder addField(final DocumentFieldDescriptor field)
		{
			fields.add(field);
			return this;
		}

		public Builder addIncludedEntity(final DocumentEntityDescriptor includedEntity)
		{
			final String detailId = includedEntity.getDetailId();
			Check.assumeNotEmpty(detailId, "detailId is not empty for {}", includedEntity);
			includedEntitiesByDetailId.put(detailId, includedEntity);
			return this;
		}

		public Builder setDataBinding(final DocumentEntityDataBindingDescriptor dataBinding)
		{
			this.dataBinding = dataBinding;
			return this;
		}

		public DocumentFieldDependencyMap buildDependencies()
		{
			final DocumentFieldDependencyMap.Builder dependenciesBuilder = DocumentFieldDependencyMap.builder();
			fields.stream().forEach(field -> dependenciesBuilder.add(field.getDependencies()));
			return dependenciesBuilder.build();
		}

		public Builder setTabNo(int tabNo)
		{
			this.tabNo = tabNo;
			return this;
		}
	}
}