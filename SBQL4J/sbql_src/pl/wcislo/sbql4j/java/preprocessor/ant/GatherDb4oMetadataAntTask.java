package pl.wcislo.sbql4j.java.preprocessor.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ext.ExtObjectContainer;

import pl.wcislo.sbql4j.lang.db4o.indexes.IndexManager;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexMetadataXMLStore;

public class GatherDb4oMetadataAntTask extends MatchingTask {
	private File destFile;
	
	/**
	 * Used for embedded database configuration
	 */
	private File dbFile;
	
	/**
	 * Used for client-server database configuration
	 */
	private String dbUrl;
	private Integer dbPort;
	private String username;
	private String pass;
	
	@Override
	public void execute() throws BuildException {
		checkTaskParams();
		IndexManager im = new IndexManager(getConnection(), 
				new IndexMetadataXMLStore(destFile));
		super.execute();
	}
	
	private ExtObjectContainer getConnection() {
		if(dbFile != null) {
			return Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbFile.toString()).ext();
		} else if(dbUrl != null) {
//			Db4oClientServer.
			return Db4o.openClient(dbUrl, dbPort, username, pass).ext();
		} else {
			throw new BuildException("Either dbFile or dbUrl should be specified");
		}
	}
	
	private void checkTaskParams() throws BuildException {
		if(destFile == null) {
			throw new BuildException("destFile should be specified");
		}
		if(dbFile != null) {
			if(!dbFile.exists()) {
				throw new BuildException("dbFile should be proper db4o database file");
			}
		} else {
			if(dbUrl == null) {
				throw new BuildException("Either dbFile or dbUrl should be specified");
			} else {
				if(dbPort == null) {
					throw new BuildException("dbPort should be specified");
				}
			}
			if(username == null) {
				username = "";
			}
			if(pass == null) {
				pass = "";
			}
		}
	}

	public void setDestFile(File destFile) {
		this.destFile = destFile;
	}

	public void setDbFile(File dbFile) {
		this.dbFile = dbFile;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public void setDbPort(Integer dbPort) {
		this.dbPort = dbPort;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
