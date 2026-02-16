package com.ecom.my_ecom.reviewsdao;

public class ordercreateddto {
   private	String referenceID;

   public ordercreateddto(String referenceID) {
	super();
	this.referenceID = referenceID;
}

   public String getReferenceID() {
	return referenceID;
   }

   public void setReferenceID(String referenceID) {
	this.referenceID = referenceID;
   }

}
