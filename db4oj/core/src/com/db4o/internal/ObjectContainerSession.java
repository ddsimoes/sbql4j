/* Copyright (C) 2007  Versant Inc.  http://www.db4o.com */

package com.db4o.internal;

import java.util.Comparator;

import pl.wcislo.sbql4j.db4o.Db4oSbqlQuery;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.constraints.UniqueFieldValueConstraintViolationException;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oDatabase;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.Db4oUUID;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.ext.InvalidIDException;
import com.db4o.ext.ObjectInfo;
import com.db4o.ext.StoredClass;
import com.db4o.ext.SystemInfo;
import com.db4o.foundation.Closure4;
import com.db4o.foundation.Iterator4;
import com.db4o.foundation.NotSupportedException;
import com.db4o.internal.activation.ActivationDepthProvider;
import com.db4o.internal.activation.ActivationMode;
import com.db4o.internal.activation.NullModifiedObjectQuery;
import com.db4o.internal.activation.UpdateDepth;
import com.db4o.internal.activation.UpdateDepthProvider;
import com.db4o.internal.callbacks.Callbacks;
import com.db4o.internal.events.EventRegistryImpl;
import com.db4o.internal.qlin.QLinRoot;
import com.db4o.internal.query.NativeQueryHandler;
import com.db4o.io.Storage;
import com.db4o.qlin.QLin;
import com.db4o.query.JdkComparatorWrapper;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.query.QueryComparator;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.generic.GenericReflector;
import com.db4o.types.TransientClass;

/**
 * @exclude
 * @sharpen.partial
 */
public class ObjectContainerSession implements InternalObjectContainer, TransientClass, ObjectContainerSpec   {
    
    protected final ObjectContainerBase _server;
    
    protected final Transaction _transaction;
    
    private boolean _closed = false;
    
    public ObjectContainerSession(ObjectContainerBase server, Transaction trans) {
        _server = server;
        _transaction = trans;
    }
    
    public ObjectContainerSession(ObjectContainerBase server) {
        this(server, server.newUserTransaction());
        _transaction.setOutSideRepresentation(this);
    }
    

    /** @param path */
    public void backup(String path) throws Db4oIOException, DatabaseClosedException,
        	NotSupportedException {
        throw new NotSupportedException();
    }

    public void backup(Storage storage, String path) throws Db4oIOException, DatabaseClosedException,
    		NotSupportedException {
    	throw new NotSupportedException();
    }

    public void bind(Object obj, long id) throws InvalidIDException, DatabaseClosedException {
        _server.bind(_transaction, obj, id);
    }
    
    public Config4Impl configImpl() {
    	// internal interface method doesn't need to lock
    	return _server.configImpl();
    }

    public Configuration configure() {
        
    	// FIXME: Consider throwing NotSupportedException here.
        // throw new NotSupportedException();
        
        synchronized(lock()){
            checkClosed();
            return _server.configure();
        }
    }

    public Object descend(Object obj, String[] path) {
        synchronized(lock()){
            checkClosed();
            return _server.descend(_transaction, obj, path);
        }
    }

    private void checkClosed() {
        if(isClosed()){
            throw new DatabaseClosedException();
        }
    }

    public Object getByID(long id) throws DatabaseClosedException, InvalidIDException {
        synchronized(lock()){
            checkClosed();
            return _server.getByID(_transaction, id);
        }
    }

    public Object getByUUID(Db4oUUID uuid) throws DatabaseClosedException, Db4oIOException {
        synchronized(lock()){
            checkClosed();
            return _server.getByUUID(_transaction, uuid);
        }
    }

    public long getID(Object obj) {
        synchronized(lock()){
            checkClosed();
            return _server.getID(_transaction, obj);
        }
    }

    public ObjectInfo getObjectInfo(Object obj) {
        synchronized(lock()){
            checkClosed();
            return _server.getObjectInfo(_transaction, obj);
        }
    }

    // TODO: Db4oDatabase is shared between embedded clients.
    // This should work, since there is an automatic bind
    // replacement. Replication test cases will tell.
    public Db4oDatabase identity() {
        synchronized(lock()){
            checkClosed();
            return _server.identity();
        }
    }

    public boolean isActive(Object obj) {
        synchronized(lock()){
            checkClosed();
            return _server.isActive(_transaction, obj);
        }
    }

    public boolean isCached(long id) {
        synchronized(lock()){
            checkClosed();
            return _server.isCached(_transaction, id);
        }
    }

    public boolean isClosed() {
        synchronized (lock()) {
            return _closed == true;
        }
    }

    public boolean isStored(Object obj) throws DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            return _server.isStored(_transaction, obj);
        }
    }

    public ReflectClass[] knownClasses() {
        synchronized(lock()){
            checkClosed();
            return _server.knownClasses();
        }
    }

    public Object lock() {
        return _server.lock();
    }
    
    public Object peekPersisted(Object object, int depth, boolean committed) {
        synchronized(lock()){
            checkClosed();
            return _server.peekPersisted(_transaction, object, activationDepthProvider().activationDepth(depth, ActivationMode.PEEK), committed);
        }
    }

    public void purge() {
        synchronized(lock()){
            checkClosed();
            _server.purge();
        }
    }

    public void purge(Object obj) {
        synchronized(lock()){
            checkClosed();
            _server.purge(_transaction, obj);
        }
    }

    public GenericReflector reflector() {
        synchronized(lock()){
            checkClosed();
            return _server.reflector();
        }
    }

    public void refresh(Object obj, int depth) {
        synchronized(lock()){
            checkClosed();
            _server.refresh(_transaction, obj, depth);
        }
    }

    public void releaseSemaphore(String name) {
    	checkClosed();
        _server.releaseSemaphore(_transaction, name);
    }

	public void store(Object obj, int depth) {
        synchronized(lock()){
            checkClosed();
            _server.store(_transaction, obj, (depth == Const4.UNSPECIFIED ? (UpdateDepth)updateDepthProvider().unspecified(NullModifiedObjectQuery.INSTANCE) : (UpdateDepth)updateDepthProvider().forDepth(depth)));
        }
    }

    public boolean setSemaphore(String name, int waitForAvailability) {
        checkClosed();
        return _server.setSemaphore(_transaction, name, waitForAvailability);
    }

    public StoredClass storedClass(Object clazz) {
        synchronized(lock()){
            checkClosed();
            return _server.storedClass(_transaction, clazz);
        }
   }

    public StoredClass[] storedClasses() {
        synchronized(lock()){
            checkClosed();
            return _server.storedClasses(_transaction);
        }
    }

    public SystemInfo systemInfo() {
        synchronized(lock()){
            checkClosed();
            return _server.systemInfo();
        }
    }

    public long version() {
        synchronized(lock()){
            checkClosed();
            return _server.version();
        }
    }
    
    public void activate(Object obj) throws Db4oIOException, DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            _server.activate(_transaction, obj);
        }
    }

    public void activate(Object obj, int depth) throws Db4oIOException, DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            _server.activate(_transaction, obj, activationDepthProvider().activationDepth(depth, ActivationMode.ACTIVATE));
        }
    }

	private ActivationDepthProvider activationDepthProvider() {
		return _server.activationDepthProvider();
	}

    public boolean close() throws Db4oIOException {
        synchronized(lock()){
            if(isClosed()){
                return false;
            }
            if(! _server.isClosed()){
                if(! _server.configImpl().isReadOnly()){
                    commit();
                }
            }
            _server.callbacks().closeOnStarted(this);
            _server.closeTransaction(_transaction, false, false);
            _closed = true;
            return true;
        }
    }

    public void commit() throws Db4oIOException, DatabaseClosedException,
        DatabaseReadOnlyException, UniqueFieldValueConstraintViolationException {
        synchronized(lock()){
            checkClosed();
            _server.commit(_transaction);
        }
    }

    public void deactivate(Object obj, int depth) throws DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            _server.deactivate(_transaction, obj, depth);
        }
    }
    
    public void deactivate(Object obj) throws DatabaseClosedException {
    	deactivate(obj, 1);
    }

    public void delete(Object obj) throws Db4oIOException, DatabaseClosedException,
        DatabaseReadOnlyException {
        synchronized(lock()){
            checkClosed();
            _server.delete(_transaction, obj);
        }
    }

    public ExtObjectContainer ext() {
        return (ExtObjectContainer)this;
    }

	public ObjectSet queryByExample(Object template) throws Db4oIOException, DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            return _server.queryByExample(_transaction, template);
        }
    }

    public Query query() throws DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            return _server.query(_transaction);
        }
    }

    public ObjectSet query(Class clazz) throws Db4oIOException, DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            return _server.query(_transaction, clazz);
        }
    }

    public ObjectSet query(Predicate predicate) throws Db4oIOException, DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            return _server.query(_transaction, predicate);
        }
    }

    public <T> ObjectSet<T> query(Predicate<T> predicate, QueryComparator<T> comparator) throws Db4oIOException,
        DatabaseClosedException {
        synchronized(lock()){
            checkClosed();
            return _server.query(_transaction, predicate, comparator);
        }
    }
    
    /**
     * @author Emil
     */
    public <R> R query(Db4oSbqlQuery<R> query) throws Db4oIOException,
    		DatabaseClosedException {
    	synchronized(lock()){
            checkClosed();
            return _server.querySbql(query, _transaction);
    	}
    }

    public void rollback() throws Db4oIOException, DatabaseClosedException,
        DatabaseReadOnlyException {
        synchronized(lock()){
            checkClosed();
            _server.rollback(_transaction);
        }
    }

	public void store(Object obj) throws DatabaseClosedException, DatabaseReadOnlyException {
        synchronized(lock()){
            checkClosed();
            _server.store(_transaction, obj);
        }
    }
    
    public ObjectContainerBase container(){
        return _server;
    }
    
    public Transaction transaction(){
        return _transaction;
    }
    
    public void callbacks(Callbacks cb){
        synchronized(lock()){
            checkClosed();
            _server.callbacks(cb);
        }
    }
    
    public Callbacks callbacks(){
        synchronized(lock()){
            checkClosed();
            return _server.callbacks();
        }
    }
    
    public final NativeQueryHandler getNativeQueryHandler() {
        synchronized(lock()){
            checkClosed();
            return _server.getNativeQueryHandler();
        }
    }
    
    public ClassMetadata classMetadataForReflectClass(ReflectClass reflectClass) {
        return _server.classMetadataForReflectClass(reflectClass);
    }

    public ClassMetadata classMetadataForName(String name) {
        return _server.classMetadataForName(name);
    }

    public ClassMetadata classMetadataForID(int id) {
        return _server.classMetadataForID(id);
    }

    public HandlerRegistry handlers(){
        return _server.handlers();
    }

    public Object syncExec(Closure4 block) {
    	return _server.syncExec(block);
    }

	public int instanceCount(ClassMetadata clazz, Transaction trans) {
		return _server.instanceCount(clazz, trans);
	}

    /**
     * @sharpen.ignore
     */
    @decaf.Ignore(decaf.Platform.JDK11)
    public ObjectSet query(Predicate predicate, Comparator comparator) throws Db4oIOException,
        DatabaseClosedException {
        return _server.query(_transaction, predicate, new JdkComparatorWrapper(comparator)); 
    }
    
    public boolean isClient(){
    	return true;
    }
    
	public void storeAll(Transaction transaction, Iterator4 objects){
		_server.storeAll(transaction, objects);
	}

	public UpdateDepthProvider updateDepthProvider() {
		return configImpl().updateDepthProvider();
	}
	
	public ObjectContainer openSession(){
		synchronized(lock()){
			return new ObjectContainerSession(_server);
		}
	}
	
	public EventRegistryImpl newEventRegistry(){
		return new EventRegistryImpl();
	}
	
	public <T> QLin<T> from(Class<T> clazz) {
		return new QLinRoot<T>(query(), clazz);
	}
}
