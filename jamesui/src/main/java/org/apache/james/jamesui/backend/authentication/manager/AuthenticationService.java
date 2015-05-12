
package org.apache.james.jamesui.backend.authentication.manager;

import org.apache.james.jamesui.backend.configuration.bean.JamesuiLoginUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom version of Spring security UserDetailsService class that provide user details (ie: user,password, ....) 
 * 
 * The current logic to authenticate a user is: 
 * 1 try to load user/password from a properties file named users.properties placed under TOMCAT_HOME/conf folder 
 *  (that file must be created by the user to override the default user settings contained in the same file placed inside the war)
 * 2 if that file is not present try look at the same file placed inside the war file (solution used during the development).
 * 
 * NOTE: To change the source type where user info are loaded (ie properties file by default), only change the implementation of UserDao methods  
 * 
 * @author fulvio
 *
 */
public class AuthenticationService implements UserDetailsService {
	
	private UserDao userDao;

	/**
	 * Constructor 
	 */
	public AuthenticationService() {
	
	}

	@Override
	public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
		
		JamesuiLoginUser jamesuiLoginUser = userDao.findByUsername(user);
		
		if(jamesuiLoginUser==null) {
	       throw new UsernameNotFoundException("No such user: " + user);
	    	
	    } else if (jamesuiLoginUser.getAuthorities().isEmpty()) {
	        throw new UsernameNotFoundException("User " + user  + " has no authorities");
	     }
		
		return jamesuiLoginUser;
	}
	
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


}
