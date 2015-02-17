package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;


public class StructSignature extends Signature {
	private List<Signature> fields = new ArrayList<Signature>();
	
	
	public StructSignature() {
		super(null, SCollectionType.NO_COLLECTION);
	}

	/**
	 * Adds a new field to the struct mSignature.
	 * @param sig is the mSignature to be added. 
	 */	
	public void addField(Signature sig) {
		fields.add(sig);
	}

	/**
	 * Adds a new fields to the struct mSignature.
	 * @param sig is the array with signatures to be added. 
	 */	
	public void addFields(Signature[] sig) {
		for (Signature s : sig)
			fields.add(s);
	}

	/**
	 * Return the fields of the struct mSignature.
	 * @return is the array of subsignatures of the struct mSignature. 
	 */	
	public Signature[] getFields() {
		return fields.toArray(new Signature[fields.size()]);
	}

	/**
	 * @return the number of structure fields
	 */
	public int fieldsNumber(){
		return fields.size();
	}
	
	@Override
	public StructSignature clone() {
		StructSignature sc = new StructSignature();
		sc.setAssociatedExpression(this.getAssociatedExpression());
		List<Signature> clonedFields = new ArrayList<Signature>();
		for(Signature s : fields) {
			clonedFields.add(s.clone());
		}
		sc.fields = clonedFields;
		sc.setColType(this.getColType());
		sc.setResultName(this.getResultName());
		sc.setResultSource(this.getResultSource());
		return sc;
	}
	
	@Override
	protected void initNestedSigs(NestingExpression ne) {
		nestedSigs = new ArrayList();
		Signature[] rarr = this.getFields();
		for (int i = 0; i < rarr.length; i++) {
			List<? extends StaticEVNSType> nestedSigs = rarr[i].nested(ne);
			for(StaticEVNSType t : nestedSigs) {
				NestedInfo ni = new NestedInfo(this, i, ne);
				t.setNestedInfo(ni);
			}
			this.nestedSigs.addAll(nestedSigs);
		}
	}
	
	@Override
	public boolean isTypeCompatible(Signature sig) {
		return super.isTypeCompatible(sig) && isStructuralTypeCompatible(sig);
	}
	
	public boolean isStructuralTypeCompatible(Signature sig) {
		if(!(sig instanceof StructSignature)) {
			return false;
		}
		StructSignature ssig = (StructSignature) sig;
		if (fields.size() != ssig.fields.size())
			return false;
		Iterator<Signature> iter = fields.iterator();
		for (Signature i : ssig.getFields()) {
			if (!iter.next().isTypeCompatible(i)) {
				return false;
			}
		}	
		return true;
	}

	@Override
	public Signature getDerefSignature() {
		return this.clone();
	}
	
	@Override
	public Signature getDerefSignatureWithCardinality() {
		return this.clone();
	}

}
