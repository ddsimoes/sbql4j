package pl.wcislo.sbql4j.java.model.compiletime;

public interface StaticEVNSType {
	public String getName();
	public NestedInfo getNestedInfo();
	public void setNestedInfo(NestedInfo nestedInfo);
	public void setPassedAsParameterToDiffContext(boolean isPassedAsParameterToDiffContext);
}
