package autoKuca;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import enumeracijeInterfejsi.PodaciCSV;
import korisnici.Agent;
import korisnici.Klijent;
import vozila.Vozilo;

public class ZakupVozila implements PodaciCSV<ZakupVozila> {
	private Klijent klijent;
	private Vozilo vozilo;
	private Date zakupljenoOd;
	private Date zakupljenoDo;
	private ChronoUnit jedinicaVremena;
	private Agent izdaoVozilo;
	private Agent primioVozilo;
	private Double vrijednostZakupa;
	private Osiguranje osiguranje;
	private Boolean prihvacenZakup;

	public ZakupVozila(Klijent klijent, Vozilo vozilo, Date zakupljenoOd, Date zakupljenoDo, ChronoUnit jedinicaVremena,
			Agent izdaoVozilo, Agent primioVozilo, double vrijednostZakupa, Osiguranje osiguranje,
			Boolean prihvacenZakup) {
		this.klijent = klijent;
		this.vozilo = vozilo;
		this.zakupljenoOd = zakupljenoOd;
		this.zakupljenoDo = zakupljenoDo;
		this.jedinicaVremena = jedinicaVremena;
		this.izdaoVozilo = izdaoVozilo;
		this.primioVozilo = primioVozilo;
		this.vrijednostZakupa = vrijednostZakupa;
		this.osiguranje = osiguranje;
		this.prihvacenZakup = prihvacenZakup;
	}

	public Klijent getKlijent() {
		return klijent;
	}

	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}

	public Vozilo getVozilo() {
		return vozilo;
	}

	public void setVozilo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}

	public Date getZakupljenoOd() {
		return zakupljenoOd;
	}

	public void setZakupljenoOd(Date zakupljenoOd) {
		this.zakupljenoOd = zakupljenoOd;
	}

	public Date getZakupljenoDo() {
		return zakupljenoDo;
	}

	public void setZakupljenoDo(Date zakupljenoDo) {
		this.zakupljenoDo = zakupljenoDo;
	}

	public Agent getIzdaoVozilo() {
		return izdaoVozilo;
	}

	public void setIzdaoVozilo(Agent izdaoVozilo) {
		this.izdaoVozilo = izdaoVozilo;
	}

	public Agent getPrimioVozilo() {
		return primioVozilo;
	}

	public void setPrimioVozilo(Agent primioVozilo) {
		this.primioVozilo = primioVozilo;
	}

	public double getVrijednostZakupa() {
		return vrijednostZakupa;
	}

	public void setVrijednostZakupa(double vrijednostZakupa) {
		this.vrijednostZakupa = vrijednostZakupa;
	}

	public Osiguranje getOsiguranje() {
		return osiguranje;
	}

	public void setOsiguranje(Osiguranje osiguranje) {
		this.osiguranje = osiguranje;
	}

	public ChronoUnit getJedinicaVremena() {
		return jedinicaVremena;
	}

	public void setJedinicaVremena(ChronoUnit jedinicaVremena) {
		this.jedinicaVremena = jedinicaVremena;
	}

	public Boolean isPrihvacenZakup() {
		return prihvacenZakup;
	}

	public void setPrihvacenZakup(Boolean prihvacenZakup) {
		this.prihvacenZakup = prihvacenZakup;
	}

	@Override
	public String toString() {
		return "ZakupVozila [klijent=" + klijent.toString() + ", vozilo=" + vozilo.toString() + ", zakupljenoOd=" + zakupljenoOd
				+ ", zakupljenoDo=" + zakupljenoDo + ", jedinicaVremena=" + jedinicaVremena + ", izdaoVozilo="
				+ izdaoVozilo + ", primioVozilo=" + primioVozilo + ", vrijednostZakupa=" + vrijednostZakupa
				+ ", osiguranje=" + osiguranje + ", prihvacenZakup=" + prihvacenZakup + "]";
	}

	@Override
	public String uCSV() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		String klijentKorisnickoIme = null;
		String klijentLozinka = null;
		String izdaoKorisnickoIme = null;
		String izdaoLozinka = null;
		String primioKorisnickoIme = null;
		String primioLozinka = null;

		if (getKlijent() != null) {
			klijentKorisnickoIme = getKlijent().getKorisnickoIme();
			klijentLozinka = getKlijent().getLozinka();
		}

		if (getIzdaoVozilo() != null) {
			izdaoKorisnickoIme = getIzdaoVozilo().getKorisnickoIme();
			izdaoLozinka = getIzdaoVozilo().getLozinka();
		}

		if (getPrimioVozilo() != null) {
			primioKorisnickoIme = getPrimioVozilo().getKorisnickoIme();
			primioLozinka = getPrimioVozilo().getLozinka();
		}

		// klijent(kor,loz),vozilo,od,do,jedVr,izdao(kor,loz),primio(kor,loz),vrZakupa,osiguranje,prihvaceno
		return klijentKorisnickoIme + "," + klijentLozinka + "," + vozilo.getBrojSasije() + ","
				+ formatter.format(zakupljenoOd) + "," + formatter.format(zakupljenoDo) + "," + jedinicaVremena + ","
				+ izdaoKorisnickoIme + "," + izdaoLozinka + "," + primioKorisnickoIme + "," + primioLozinka + ","
				+ (vrijednostZakupa == null ? "null" : vrijednostZakupa) + ","
				+ (osiguranje == null ? "null" : osiguranje.getNovcaniLimit()) + ","
				+ (prihvacenZakup == null ? "null" : prihvacenZakup.toString()) + "\n";
	}

}
