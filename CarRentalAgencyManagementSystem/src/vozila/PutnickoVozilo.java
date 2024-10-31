package vozila;

import autoKuca.AutoKuca;
import enumeracijeInterfejsi.Gorivo;
import enumeracijeInterfejsi.Karoserija;
import enumeracijeInterfejsi.PodaciCSV;

public class PutnickoVozilo extends Vozilo implements PodaciCSV<PutnickoVozilo> {
	private AutoKuca autoKuca;
	private int brojSedista;
	private Karoserija karoserija;

	public PutnickoVozilo(String marka, String model, String brojSasije, int kilometraza, Gorivo gorivo,
			AutoKuca autoKuca, int brojSedista, Karoserija karoserija) {
		super(marka, model, brojSasije, kilometraza, gorivo);
		this.autoKuca = autoKuca;
		this.brojSedista = brojSedista;
		this.karoserija = karoserija;
	}

	public AutoKuca getAutoKuca() {
		return autoKuca;
	}

	public void setAutoKuca(AutoKuca autoKuca) {
		this.autoKuca = autoKuca;
	}

	public int getBrojSedista() {
		return brojSedista;
	}

	public void setBrojSedista(int brojSedista) {
		this.brojSedista = brojSedista;
	}

	public Karoserija getKaroserija() {
		return karoserija;
	}

	public void setKaroserija(Karoserija karoserija) {
		this.karoserija = karoserija;
	}

	@Override
	public String toString() {
		return "PutnickoVozilo[" + super.toString() + ", autoKuca:" + autoKuca + ", brojSedista:" + brojSedista
				+ ", karoserija:" + karoserija + "]";
	}

	@Override
	public String uCSV() {
		return PutnickoVozilo.class.getName() + "," + marka + "," + model + "," + brojSasije + "," + kilometraza + ","
				+ gorivo + "," + autoKuca.getNaziv() + "," + brojSedista + "," + karoserija + "\n";
	}

}
