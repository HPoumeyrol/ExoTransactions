import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection implements Serializable
{   
	
	private static final long serialVersionUID = 1426783525973463317L;
	/** Instance unique pr�-initialis�e */
    private static DbConnection INSTANCE = new DbConnection();
    private Connection dbConn= null;
	private Boolean IsDbConnected= false;
	
	

	/** Constructeur priv� */
    private DbConnection(){
    	  
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	// Open DataBase
		try {
			this.dbConn = DriverManager.getConnection(Params.getDbUrl(), Params.getDbUser(), Params.getDbPwd());
			this.dbConn.setSchema(Params.getDbSchema());
			System.out.println("Catalog= " + dbConn.getCatalog());
			System.out.println("Schema= " + dbConn.getSchema());
			this.IsDbConnected= true;
		} catch (SQLException e) {
			System.err.println("DataBase Connexion error : ");
			e.printStackTrace();
			System.err.println("Program terminated on error.");
			System.exit(99);
		}
    }
 
     
    /** Point d'acc�s pour l'instance unique du singleton */
    public static DbConnection getInstance()
    {   return INSTANCE;
    }
 
    /** S�curit� anti-d�s�rialisation */
    private Object readResolve() {
        return INSTANCE;
    }

    public static Connection getDbConn() {
		return INSTANCE.dbConn;
    }
    

	public static Boolean isDbConnected() {
		return INSTANCE.IsDbConnected;
	}

	
	
	
	public static boolean setAutoCommit(Boolean autoCommitDesiredValue) {
		Boolean res= false;
		if(INSTANCE.IsDbConnected) {
			
			try {
				INSTANCE.dbConn.setAutoCommit(autoCommitDesiredValue);
				//System.out.println("AutoCommit set to " + autoCommitDesiredValue);
				res= true;
			} catch (SQLException e )
			{
				System.err.println("Error when setting AutoCommit");
				e.printStackTrace();
			}
		} else {
			System.err.println("Error : database in not connected !");
		}
		return res;
	}

	
	
	
	
	public static boolean commit(){
		Boolean res= false;
		if(INSTANCE.IsDbConnected) {
			try {
				INSTANCE.dbConn.commit();
				System.out.println("Transaction commited OK");
				res= true;
			} catch (SQLException e )
			{
				System.err.println("Error when commiting transaction");
				e.printStackTrace();
			}
			
		} else {
			System.err.println("Error : database in not connected !");
		}
		return res;
		
	}
	
	public static boolean rollback(){
		Boolean res= false;
		if(INSTANCE.IsDbConnected) {
			try {
				INSTANCE.dbConn.rollback();
				System.out.println("Transaction rollbacked OK");
				res= true;
			} catch (SQLException e )
			{
				System.err.println("Error when rollbacking transaction");
				e.printStackTrace();
			}
			
		} else {
			System.err.println("Error : database in not connected !");
		}
		return res;
	}
	
}
