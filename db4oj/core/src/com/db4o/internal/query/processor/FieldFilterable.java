/* Copyright (C) 2004 - 2010  Versant Inc.  http://www.db4o.com */
package com.db4o.internal.query.processor;

public interface FieldFilterable {
	void filter(QField field, ParentCandidate candidate);
}
