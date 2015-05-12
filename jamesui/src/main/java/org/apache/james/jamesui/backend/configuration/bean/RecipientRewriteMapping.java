package org.apache.james.jamesui.backend.configuration.bean;

import java.util.Set;

/**
 * Bean that represents a Recipient Rewrite mappings used in mapping Administration panel
 * 
 * @author fulvio
 *
 */
public class RecipientRewriteMapping {

	  private String userAndDomain; //eg: auser@domain
	  private Set<String> mappings; //a list of mapping assigned at the auser@domain

	  /**
	   * Constructor
	   */
	  public RecipientRewriteMapping() {
		  
	  }

	  /**
	   * Constructor
	   * Creates the mapping with the user and domain received and all the mappings
	   *
	   * @param userAndDomain
	   * @param mappings
	   */
	  public RecipientRewriteMapping(String userAndDomain, Set<String> mappings) {
	     this.userAndDomain = userAndDomain;
	     this.setMappings(mappings);
	  }

	  public String getId() {
	    return userAndDomain;
	  }

	  public void setId(String id) {
	    this.userAndDomain = id;
	  }

	  public String getUserAndDomain() {
	    return userAndDomain;
	  }

	  public void setUserAndDomain(String userAndDomain) {
	    this.userAndDomain = userAndDomain;
	  }

	  public Set<String> getMappings() {
	    return mappings;
	  }

	  public void setMappings(Set<String> mappings) {
	    this.mappings = mappings;
	  }


}
