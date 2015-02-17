package pl.wcislo.sbql4j.db4o;

import com.db4o.internal.ObjectContainerBase;
import com.db4o.internal.Transaction;

public interface Db4oSbqlQuery<R> {
	public R executeQuery(ObjectContainerBase ocb, Transaction t);

}
