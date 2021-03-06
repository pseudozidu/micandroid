 /*
 * Light Portal
 *
 * Copyright (c) 2009, Light Portal, Inc or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Light Portal, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */

package org.light.portal.distribute;

import java.io.Serializable;

/**
 * 
 * @author Jianmin Liu
 **/
public enum Event implements Serializable{
	CONNECTION("CONNECTION"),
	SYNC_DONE("SYNC_DONE"),
	INDEX_ALL("INDEX_ALL"),
	ENTITY_UPDATE("ENTITY_UPDATE"),
	ENTITY_DELETE("ENTITY_DELETE"),
	CACHE_PUT("CACHE_PUT"),
	CACHE_REMOVE("CACHE_REMOVE")
	;
	
	private String value;
	
	private Event(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	public String toString(){
		return getValue();
	}
}
