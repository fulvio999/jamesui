package org.apache.james.jamesui.backend.configuration.bean;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents a JamesUI login user account (NOTE: in the current version the user has only username and a password (ie the default information).
 * It has no others details like phone, email.... so that was not necessary create a class that implements
 * UserDetails, but using a default authentication-provider is ok).
 * 
 * The idea to implementing a custom UserDetails object was only to prepare the architecture in case of future improvements of JamesUI.
 * 
 * @author fulvio
 *
 */
public class JamesuiLoginUser implements UserDetails{
		
		private static final long serialVersionUID = 1L;
		
		private String userName;
		private String password;
		
		//add here additional fields about user information and provide get/set methods 
		
		private ArrayList<GrantedAuthority> grantedAuthority;

		/**
		 * Constructor
		 */
		public JamesuiLoginUser(String userName, String password, ArrayList<GrantedAuthority> ga) {
			
			this.userName = userName;
			this.password = password;	
			this.grantedAuthority = ga;
		}

	
		@Override
		public Collection<GrantedAuthority> getAuthorities() {		
			return this.grantedAuthority;
		}


		@Override
		public String getPassword() {			
			return this.password;
		}


		@Override
		public String getUsername() {			
			return this.userName;
		}


		@Override
		public boolean isAccountNonExpired() {			
			return true;
		}


		@Override
		public boolean isAccountNonLocked() {			
			return true;
		}


		@Override
		public boolean isCredentialsNonExpired() {			
			return true;
		}


		@Override
		public boolean isEnabled() {			
			return true;
		}


		@Override
		public String toString() {
			return "JamesUIuser [userName=" + userName + ", password="
					+ password + ", grantedAuthority=" + grantedAuthority + "]";
		}

}
