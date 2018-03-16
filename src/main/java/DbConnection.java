import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection implements Serializable
{   
	
	private static final long serialVersionUID = 1426784925973463317L;
	private static Connection dbConn= null;
	private static Boolean IsDbConnected= false;
	
	

	/** Constructeur privé */
    private DbConnection(){
    	  
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	// Open DataBase
		try {
			DbConnection.dbConn = DriverManager.getConnection(Params.getDbUrl(), Params.getDbUser(), Params.getDbPwd());
			dbConn.setSchema(Params.getDbSchema());
			System.out.println("Catalog= " + dbConn.getCatalog());
			System.out.println("Schema= " + dbConn.getSchema());
			DbConnection.IsDbConnected= true;
		} catch (SQLException e) {
			System.out.println("DataBase Connexion error : ");
			e.printStackTrace();
			System.out.println("Program terminated on error.");
			System.exit(99);
		}
    }
 
    /** Instance unique pré-initialisée */
    private static DbConnection INSTANCE = new DbConnection();
     
    /** Point d'accès pour l'instance unique du singleton */
    public static DbConnection getInstance()
    {   return INSTANCE;
    }
 
    /** Sécurité anti-désérialisation */
    private Object readResolve() {
        return INSTANCE;
    }

    public static Connection getDbConn() {
		return dbConn;
    }
    
//    public static boolean initConnection() {
//    	boolean result= false;
//    	
//    	
//    	// Open DataBase
//		try {
//			DbConnection.dbConn = DriverManager.getConnection(Params.getDbUrl(), Params.getDbUser(), Params.getDbPwd());
//			result = true;
//		} catch (SQLException e) {
//			System.out.println("DataBase Connexion error");
//			e.printStackTrace();
//		}
//    	return result;
//    }

	public static Boolean isDbConnected() {
		return IsDbConnected;
	}

	
}
