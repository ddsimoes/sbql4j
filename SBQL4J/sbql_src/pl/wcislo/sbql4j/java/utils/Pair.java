package pl.wcislo.sbql4j.java.utils;

public class Pair<E> {
	public String name;
	public E val;
	public Pair(String name, E val) {
		this.name = name;
		this.val = val;
	}
	
	@Override
	public String toString() {
		
		return "["+name+"="+val+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
