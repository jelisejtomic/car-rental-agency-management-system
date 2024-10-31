package vozila;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import autoKuca.Osiguranje;
import autoKuca.ZakupVozila;
import enumeracijeInterfejsi.Gorivo;
import enumeracijeInterfejsi.Unajmljivo;
import korisnici.Agent;
import korisnici.Klijent;

public abstract class Vozilo implements Unajmljivo {
	protected String marka;
	protected String model;
	protected String brojSasije; // sastoji se od 17 brojeva i slova bez razmaka
	protected int kilometraza;
	protected Gorivo gorivo;

	public Vozilo(String marka, String model, String brojSasije, int kilometraza, Gorivo gorivo) {
		this.marka = marka;
		this.model = model;
		this.brojSasije = brojSasije;
		this.kilometraza = kilometraza;
		this.gorivo = gorivo;
	}

	public String getMarka() {
		return marka;
	}

	public void setMarka(String marka) {
		this.marka = marka;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrojSasije() {
		return brojSasije;
	}

	public void setBrojSasije(String brojSasije) {
		this.brojSasije = brojSasije;
	}

	public int getKilometraza() {
		return kilometraza;
	}

	public void setKilometraza(int kilometraza) {
		this.kilometraza = kilometraza;
	}

	public Gorivo getGorivo() {
		return gorivo;
	}

	public void setGorivo(Gorivo gorivo) {
		this.gorivo = gorivo;
	}

	@Override
	public String toString() {
		return "marka:" + marka + ", model:" + model + ", brojSasije:" + brojSasije + ", kilometraza:" + kilometraza
				+ ", gorivo:" + gorivo;
	}

	@Override
	public boolean unajmi(ZakupVozila zakup, Klijent klijent, Date zakupljenoOd, Date zakupljenoDo,
			ChronoUnit jedinicaVremena, Agent izdaoVozilo, Double vrijednostZakupa, Osiguranje osiguranje) {
		if (zakup == null || klijent == null || izdaoVozilo == null || zakupljenoOd == null || zakupljenoDo == null
				|| vrijednostZakupa == null || vrijednostZakupa <= 0)
			return false;

		if (zakupljenoDo.before(zakupljenoOd)) {
			System.out.println("Datum vraæanja ne može biti prije datuma unajmljivanja.");
			return false;
		}

		zakup.setKlijent(klijent);
		zakup.setZakupljenoOd(zakupljenoOd);
		zakup.setZakupljenoDo(zakupljenoDo);
		zakup.setJedinicaVremena(jedinicaVremena);
		zakup.setIzdaoVozilo(izdaoVozilo);
		zakup.setPrimioVozilo(null);
		zakup.setVrijednostZakupa(vrijednostZakupa);
		zakup.setOsiguranje(osiguranje);
		zakup.setPrihvacenZakup(true);
		return true;
	}

	@Override
	public double vrati(ZakupVozila zakup, int predjeniKilometri) {
		if (predjeniKilometri < 0) {
			System.out.println("Neispravan unos kilometraže.");
			return 0.0;
		}

		// Izraèunavanje cijene unajmljivanja na osnovu preðenih kilometara
		double cenaUnajmljivanja = zakup.getVrijednostZakupa() * predjeniKilometri;

		return cenaUnajmljivanja;
	}

}
