
package org.apache.james.jamesui.backend.authentication.manager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.james.jamesui.backend.configuration.bean.JamesuiLoginUser;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.frontend.JamesUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Dao that load user e password from a source (DB, properties file....). This implementation load them from properties file.
 * 
 * The current logic is: 
 * - try to load user/pw from a properties file named users.properties placed under TOMCAT_HOME/conf folder (that file must be created by the user
 *   to overrride the default user settings contained in the same file placed inside the war)
 * - if that file is not present try look at the same file placed inside the war file (solution used during the development).
 *  
 * Future versions of JamesUI could load User information from a different source (eg: a Database).
 * In that case change only the implementation logic of the two methods findByUsername and getUserRole
 * 
 * @author fulvio
 *
 */
public class UserDao {

	private final static Logger LOG = LoggerFactory.getLogger(UserDao.class);
	
	private static final String USERS_FILE_NAME = "users.properties";
	
	/**
	 * Constructor
	 */
	public UserDao() {
		
	}	
	
	/**
	 * Load the User informations
	 * @param username The login name of the user
	 * @return A JamesUIuser
	 */
	public JamesuiLoginUser findByUsername(String userName) {				
		
		ArrayList<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();	    
		
		String username = null;
		String password = null;	
		
		JamesuiLoginUser jamesuiLoginUser = null;
	    
		//The pattern is: user=user01,ROLE_USER
		Properties usersConfig = null;
		
		try {			
			//1) Look for TOMCAT_CONF/user.properties (the user defined one)
			String path = EnvironmentConfigurationReader.getCatalinaHomeFolder()+File.separator+"conf"+File.separator+"/"+USERS_FILE_NAME;
						
			usersConfig = new Properties();
			usersConfig.load(new FileReader(new File(path)));
			
			String fullRow = (String) usersConfig.get(userName);
			String[] tokens = fullRow.split(",");
			
			username = userName;
			password = tokens[0];			
					
			ga.add(new SimpleGrantedAuthority(tokens[1]));				
			jamesuiLoginUser = new JamesuiLoginUser(username,password,ga);			
			
		} catch (IOException e) {
			
			LOG.info("File "+USERS_FILE_NAME+" NOT found under Tomcat/conf folder, looking for default one inside the war..."); 
						
			try {
				usersConfig = new Properties();
				usersConfig.load(this.getClass().getResourceAsStream("/"+USERS_FILE_NAME));
				String fullRow = (String) usersConfig.get(userName);
				String[] tokens = fullRow.split(",");
				
				username = userName;
				password = tokens[0];			
						
				ga.add(new SimpleGrantedAuthority(tokens[1]));					
				jamesuiLoginUser = new JamesuiLoginUser(username,password,ga);
				
			} catch (IOException ex) {
			    LOG.error("ERROR looking for user.properties file, not found anywhere !!");
			}
		}  
				
		return jamesuiLoginUser;	
	 }
	
	
	/**
	 * NOT USED with users.properties solution.
	 * Use it in case of Database solution to execute a custom query with userName as parameter
	 * 
	 * @param userName
	 * @return
	 */
//	private ArrayList<GrantedAuthority> getUserRole(String userName){
//		
//	    ArrayList<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();	    
//	    ga.add(new SimpleGrantedAuthority("ROLE_USER"));	
//	
//	    return ga;	 
//   }

}
