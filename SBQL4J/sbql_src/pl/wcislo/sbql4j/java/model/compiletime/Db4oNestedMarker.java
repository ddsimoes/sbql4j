package pl.wcislo.sbql4j.java.model.compiletime;

public class Db4oNestedMarker implements StaticEVNSType {
	public Db4oNestedMarker() {
	}
	
	private NestedInfo ni;
	
	@Override
	public String getName() {
		return null;
	}
	@Override
	public NestedInfo getNestedInfo() {
		return ni;
	}
	@Override
	public void setNestedInfo(NestedInfo nestedInfo) {
		this.ni = nestedInfo;
	}
	
	@Override
	public void setPassedAsParameterToDiffContext(
			boolean isPassedAsParameterToDiffContext) {
		// TODO Auto-generated method stub
		
	}
}