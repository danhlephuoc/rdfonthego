package deri.org.store;

import java.io.File;



import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleTupleKeyCreator;
import com.sleepycat.je.CacheMode;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class Configuration {
	public String appName;
	public Environment env;
	public static DatabaseConfig databaseConfig; 

	public Configuration(String appName){
		this.appName	=	appName;
		setEnvironment();
		setDbConfig();
		set2ndDbConfig();
	}
	
	public void close(){
		env.close();
	}
	
	private  File createFolder(){
		String state	=	android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)){
			String appFolder	=	android.os.Environment.getExternalStorageDirectory()+ "/Android/data/StoreManager/" + appName + "/file/";
			File	f =	new File(appFolder);
			f.mkdirs();
			return f;
		}else
			return null;
	}
		
	private EnvironmentConfig envConfig(){
		EnvironmentConfig environmentConfig	= new EnvironmentConfig();
		environmentConfig.setAllowCreate(true);
		environmentConfig.setTransactional(false);
		return environmentConfig;
	}
	
	private void setEnvironment(){	
			File f;
			f = createFolder();
			EnvironmentConfig envConfig = envConfig();
			env = new Environment(f, envConfig);
	}
	
	private void setDbConfig(){
			databaseConfig = new DatabaseConfig();
			databaseConfig.setTransactional(false);
			databaseConfig.setAllowCreate(true);
			databaseConfig.setTemporary(false); 
			databaseConfig.setDeferredWrite(true);
			databaseConfig.setSortedDuplicates(false);
			databaseConfig.setCacheMode(CacheMode.KEEP_HOT);
		
	}
	
	private static SecondaryConfig set2ndDbConfig(){
		SecondaryConfig sndDatabaseConfig = null;
		if (sndDatabaseConfig == null){
		sndDatabaseConfig=new SecondaryConfig();
		sndDatabaseConfig.setAllowCreate(true);
		sndDatabaseConfig.setTemporary(false); 
		sndDatabaseConfig.setDeferredWrite(true);
		sndDatabaseConfig.setSortedDuplicates(true);
		sndDatabaseConfig.setKeyCreator(createKey());}
		return sndDatabaseConfig;
	}
	
	
	public Database createDB(String dbName){
		Database db = env.openDatabase(null, dbName, databaseConfig);
		return db; 	   
	}
	
	public SecondaryDatabase create2ndDB(Database db,String dbName){
		return env.openSecondaryDatabase(null, dbName, db, set2ndDbConfig());
	}
	
	private static SecondaryKeyCreator createKey(){
		return new TupleTupleKeyCreator<Integer>(){
			@Override
			public boolean createSecondaryKey(TupleInput primaryKeyInput, TupleInput dataInput, TupleOutput indexKeyOutput){
				indexKeyOutput.writeInt(dataInput.readInt());
				return true;
			}
		};
	}

	public  void remove(String dbName){
		env.removeDatabase(null, dbName);
	}

}
