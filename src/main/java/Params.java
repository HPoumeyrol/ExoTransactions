import java.io.Serializable;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Params implements Serializable
{   
	private static final long serialVersionUID = 1426784925973463317L;
	private static Params INSTANCE = null; /** Instance unique pré-initialisée */
    private final static String PARAMETERS_JSON_FILE_PATH = ".\\parameters.json.txt";
	
    
	private String env= "";
	private String dbUrl= "";
	private String dbUser= "";
	private String dbPwd= "";
	private String dbSchema= "";
	
	
	/** Constructeur privé */
    private Params(){
    	
    	JSONParser parser = new JSONParser();
        JSONObject data=null;
		try {
			data = (JSONObject) parser.parse(new FileReader(PARAMETERS_JSON_FILE_PATH));
			env= (String) data.get("Environment");
			dbUrl= (String) data.get("dbUrl");	
	    	dbUser= (String) data.get("dbUser");	
    		dbPwd= (String) data.get("dbPwd");	
    		dbSchema= (String) data.get("dbSchema");	
	
		} catch (IOException | ParseException e) {
			System.out.println("Error when parsing parameters file " + PARAMETERS_JSON_FILE_PATH);
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
		return INSTANCE.dbUrl;
	}

	
	public static String getDbUser() {
		return INSTANCE.dbUser;
	}

	public static String getDbPwd() {
		return INSTANCE.dbPwd;
	}

	public static String getEnv() {
		return INSTANCE.env;
	}

	public static String getDbSchema() {
		return INSTANCE.dbSchema;
	}

	@Override
	public String toString() {
		return "Params :\n\tEnv= " + env + "\n\tdbUrl= "+ dbUrl + "\n\tdbUser= " + dbUser + "\n\tdbPwd= " + dbPwd + "\n\tdbSchema= " + dbSchema;
	}

	public void display() {
		System.out.println(this.toString());
	}
    
    

    
    
}
