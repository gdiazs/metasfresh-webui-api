package de.metas.ui.web.window.model;

import org.adempiere.util.Check;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.compiere.util.Evaluatees;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import de.metas.ui.web.window.descriptor.DocumentEntityDescriptor;

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

public final class DocumentRepositoryQuery
{
	public static final Builder builder(final DocumentEntityDescriptor entityDescriptor)
	{
		return new Builder(entityDescriptor);
	}

	public static final DocumentRepositoryQuery ofRecordId(final DocumentEntityDescriptor entityDescriptor, final Object recordId)
	{
		Check.assumeNotNull(recordId, "Parameter recordId is not null");
		return builder(entityDescriptor).setRecordId(recordId).build();
	}

	private final DocumentEntityDescriptor entityDescriptor;
	private final Object recordId;
	private final Document parentDocument;

	private transient Evaluatee _evaluationContext = null; // lazy

	private DocumentRepositoryQuery(final Builder builder)
	{
		super();
		entityDescriptor = builder.entityDescriptor;
		recordId = builder.recordId;
		parentDocument = builder.parentDocument;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("entityDescriptor", entityDescriptor)
				.add("recordId", recordId)
				.add("parentDocument", parentDocument)
				.toString();
	}

	public DocumentEntityDescriptor getEntityDescriptor()
	{
		return entityDescriptor;
	}

	public Object getRecordId()
	{
		return recordId;
	}

	public Document getParentDocument()
	{
		return parentDocument;
	}

	public Object getParentLinkId()
	{
		return parentDocument == null ? null : parentDocument.getDocumentId();
	}

	public Evaluatee getEvaluationContext()
	{
		if (_evaluationContext == null)
		{
			_evaluationContext = createEvaluationContext();
		}
		return _evaluationContext;
	}

	private Evaluatee createEvaluationContext()
	{
		if (parentDocument != null)
		{
			return parentDocument.asEvaluatee();
		}

		final int windowNo = Env.WINDOW_MAIN; // TODO: get the proper windowNo
		final boolean onlyWindow = false;
		return Evaluatees.ofCtx(Env.getCtx(), windowNo, onlyWindow);
	}

	public static final class Builder
	{
		private final DocumentEntityDescriptor entityDescriptor;
		private Document parentDocument;
		private Object recordId;

		private Builder(final DocumentEntityDescriptor entityDescriptor)
		{
			super();
			this.entityDescriptor = Preconditions.checkNotNull(entityDescriptor);
		}

		public DocumentRepositoryQuery build()
		{
			return new DocumentRepositoryQuery(this);
		}

		public Builder setRecordId(final Object recordId)
		{
			this.recordId = recordId;
			return this;
		}

		public Builder setParentDocument(final Document parentDocument)
		{
			this.parentDocument = parentDocument;
			return this;
		}
	}
}