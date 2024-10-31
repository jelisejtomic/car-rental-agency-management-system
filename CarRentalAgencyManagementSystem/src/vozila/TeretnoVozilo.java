package vozila;

import enumeracijeInterfejsi.Gorivo;
import enumeracijeInterfejsi.PodaciCSV;

public class TeretnoVozilo extends Vozilo implements PodaciCSV<TeretnoVozilo> {
	private double kapacitet; // u tonama

	public TeretnoVozilo(String marka, String model, String brojSasije, int kilometraza, Gorivo gorivo,
			double kapacitet) {
		super(marka, model, brojSasije, kilometraza, gorivo);
		this.kapacitet = kapacitet;
	}

	public double getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(double kapacitet) {
		this.kapacitet = kapacitet;
	}

	@Override
	public String toString() {
		return "TeretnoVozilo[" + super.toString() + ", kapacitet:" + kapacitet + "t" + "]";
	}

	@Override
	public String uCSV() {
		return TeretnoVozilo.class.getName() + "," + marka + "," + model + "," + brojSasije + "," + kilometraza + ","
				+ gorivo + "," + kapacitet + "\n";
	}

}
