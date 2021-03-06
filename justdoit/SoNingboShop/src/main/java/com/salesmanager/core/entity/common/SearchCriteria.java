/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.entity.common;

public class SearchCriteria {

	private int quantity = 20;
	private int startindex = 0;
	private int merchantId = 1;
	private int languageId = 1;

	private int startcount = 0;
	
	public void setStartcount(int startcount) {
		this.startcount = startcount;
	}

	public int getLowerLimit() {
		int lowerLimit = startindex * quantity;
		
		if(startcount>0) {
			lowerLimit = startcount;
		}
		
		return lowerLimit;
	}
	


	public int getUpperLimit(int count) {
		
			int upperLimit = 0;
			int countLeft = 0;
			
			if(startcount>0) {
	
				countLeft = count - startcount;
				
			} else {
			
				countLeft = count - startindex * quantity;
			}
			
			if (countLeft < 0) {
				upperLimit = quantity;
				//return quantity;
			}
			if (countLeft < quantity) {
				upperLimit = countLeft;
				//return countLeft;
			} else {
				upperLimit = quantity;
				//return quantity;
			}
		
				
			return upperLimit;
	}
	


	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

}
