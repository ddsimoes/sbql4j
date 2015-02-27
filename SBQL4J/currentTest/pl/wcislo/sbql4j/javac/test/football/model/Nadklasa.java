package pl.wcislo.sbql4j.javac.test.football.model;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

public class Nadklasa implements Serializable {
	 
	 
	// tu trzymamy ekstensje 
	public static Hashtable hash = new Hashtable();
 
	 
	public Nadklasa() {
		Vector ekstensja = null;
		Class klasa = this.getClass();  //Uniwersalno��

		if (hash.containsKey(klasa)) {
			ekstensja = (Vector) hash.get(klasa);
		} else {
			ekstensja = new Vector();
			hash.put(klasa, ekstensja);
		}

		ekstensja.add(this);
	}

	// Zapisywanie ekstensji do strumienia  --> trwa�os� ekstensji
	public static void Save(ObjectOutputStream stream)
			throws IOException {
		stream.writeObject(hash);
	}

	// Odczytywanie ekstensji ze strumienia  --> trwa�o�� ekstensji
	 
	public static void Load(ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		hash = (Hashtable) stream.readObject();
	}

	// Wy�wietlanie wszystkich ekstensji
	 
	public static void pokazEkstensje(Class klasa) throws Exception {

		Vector ekstensja = null;

		if (hash.containsKey(klasa)) {
			ekstensja = (Vector) hash.get(klasa);
		} else {
			throw new Exception("Brak klasy " + klasa);
		}

			int x=0;
		for (Object obiekt : ekstensja) { 
			System.out.print("Ekstensja klasy: " + klasa.getSimpleName() + " ");
			x = x+1;
		
			System.out.println("Numer: "+ x +" " +obiekt);
		}
	}

}