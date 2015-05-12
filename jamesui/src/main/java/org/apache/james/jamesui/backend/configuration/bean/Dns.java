
package org.apache.james.jamesui.backend.configuration.bean;

import java.util.List;

/**
 * Bean that represents the information stored in the file "dnsservice.xml"
 * @author fulvio
 *
 */
public class Dns {
	
	private List<Object> dnsServerList; 
	
	private boolean autodiscover;
	private boolean authoritative;
	private long maxcachesize;
	private boolean singleIPperMX;

	/**
	 * constructor
	 */
	public Dns() {
		
	}

	public List<Object> getDnsServerList() {
		return dnsServerList;
	}

	public void setDnsServerList(List<Object> servers) {
		this.dnsServerList = servers;
	}

	public boolean isAutodiscover() {
		return autodiscover;
	}

	public void setAutodiscover(boolean autodiscover) {
		this.autodiscover = autodiscover;
	}

	public boolean isAuthoritative() {
		return authoritative;
	}

	public void setAuthoritative(boolean authoritative) {
		this.authoritative = authoritative;
	}


	public boolean isSingleIPperMX() {
		return singleIPperMX;
	}

	public void setSingleIPperMX(boolean singleIPperMX) {
		this.singleIPperMX = singleIPperMX;
	}

	public long getMaxcachesize() {
		return maxcachesize;
	}

	public void setMaxcachesize(long maxcachesize) {
		this.maxcachesize = maxcachesize;
	}

	@Override
	public String toString() {
		return "Dns [dnsServerList size=" + dnsServerList.size() + ", autodiscover="
				+ autodiscover + ", authoritative=" + authoritative
				+ ", maxcachesize=" + maxcachesize + ", singleIPperMX="
				+ singleIPperMX + "]";
	}



}
