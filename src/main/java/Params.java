import java.io.Serializable;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Params implements Serializable
{   
	//private final static String parametersJsonFilePath = "C:\\Users\\Utilisateur.UTILISA-1R91N3M\\Documents\\JavaPrograms\\DB_ConcucurenstAccessAndIntegrity\\parameters.json.txt";
	private final static String parametersJsonFilePath = ".\\parameters.json.txt";
	/** Instance unique pré-initialisée */
    private static Params INSTANCE = null;
     
    
	private static String env= "";
	private static String dbUrl= "";
	private static String dbUser= "";
	private static String dbPwd= "";
	private static String dbSchema= "";
	
	private static final long serialVersionUID = 1426784925973463317L;
	
	/** Constructeur privé */
    private Params(){
    	
    	JSONParser parser = new JSONParser();
        JSONObject data=null;
		try {
			data = (JSONObject) parser.parse(new FileReader(parametersJsonFilePath));
			env= (String) data.get("Environment");
			dbUrl= (String) data.get("dbUrl");	
	    	dbUser= (String) data.get("dbUser");	
    		dbPwd= (String) data.get("dbPwd");	
    		dbSchema= (String) data.get("dbSchema");	
	
		} catch (IOException | ParseException e) {
			System.out.println("Error when parsing parameters file " + parametersJsonFilePath);
			e.printStackTrace();
			System.out.println("Program terminated on error.");
			System.exit(99);
		}
    }	
    	 
    /** Point d'accès pour l'instance unique du singleton */
    public static Params getInstance()
    {   
    	return INSTANCE;
    }
 
    /** Sécurité anti-désérialisation */
    private Object readResolve() {
        return INSTANCE;
    }

	public static String getDbUrl() {
		return dbUrl;
	}

	public static String getDbUser() {
		return dbUser;
	}

	public static String getDbPwd() {
		return dbPwd;
	}

	public static String getEnv() {
		return env;
	}

	public static String getDbSchema() {
		return dbSchema;
	}

	@Override
	public String toString() {
		return "Params :\n\tEnv= " + env + "\n\tdbUrl= "+ dbUrl + "\n\tdbUser= " + dbUser + "\n\tdbPwd= " + dbPwd + "\n\tdbSchema= " + dbSchema;
	}

	public static void display() {
		System.out.println(Params.getInstance().toString());
	}
    
    

    
    
}
