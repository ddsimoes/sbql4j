package pl.wcislo.sbql4j.java.model.compiletime;

/**
 * @author Emil
 *
 * An Expression that binds something in static ENVS
 */
public interface BindExpression {
	public void setBoundSignature(StaticEVNSType b);
	public StaticEVNSType getBoundSignature();
}
