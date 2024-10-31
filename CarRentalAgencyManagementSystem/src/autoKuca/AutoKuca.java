package autoKuca;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import enumeracijeInterfejsi.Gorivo;
import enumeracijeInterfejsi.Karoserija;
import korisnici.Agent;
import korisnici.Klijent;
import vozila.PutnickoVozilo;
import vozila.TeretnoVozilo;
import vozila.Vozilo;

public class AutoKuca {
	private String naziv;
	private String adresa;
	private List<Agent> agenti;
	private List<Klijent> klijenti;
	private List<Vozilo> vozila;
	private List<ZakupVozila> zakupljenaVozila;

	public AutoKuca(String naziv, String adresa) {
		this.naziv = naziv;
		this.adresa = adresa;
		agenti = new ArrayList<>();
		klijenti = new ArrayList<>();
		vozila = new ArrayList<>();
		zakupljenaVozila = new ArrayList<>();
	}

	public List<Agent> getAgenti() {
		return agenti;
	}

	public void setAgenti(List<Agent> agenti) {
		this.agenti = agenti;
	}

	public List<Klijent> getKlijenti() {
		return klijenti;
	}

	public void setKlijenti(List<Klijent> klijenti) {
		this.klijenti = klijenti;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public List<Vozilo> getVozila() {
		return vozila;
	}

	public void setVozila(List<Vozilo> vozila) {
		this.vozila = vozila;
	}

	public List<ZakupVozila> getZakupljenaVozila() {
		return zakupljenaVozila;
	}

	public void setZakupljenaVozila(List<ZakupVozila> zakupljenaVozila) {
		this.zakupljenaVozila = zakupljenaVozila;
	}

	@Override
	public String toString() {
		return "[naziv:" + naziv + ", adresa:" + adresa + "]";
	}

	// Uèitavanje podataka iz datoteka
	public void ucitajAgente() {
		String csv = "data/korisnici.csv";
		try {
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String red = null;
			while ((red = br.readLine()) != null) {
				String[] podaci = red.split(",");
				if (podaci[0].equals(Agent.class.getName())) {
					agenti.add(new Agent(podaci[1], podaci[2], podaci[3], podaci[4], this));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Greška pri uèitavanju podataka iz datoteke!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	public void ucitajKlijente() {
		String csv = "data/korisnici.csv";
		try {
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String red = null;
			while ((red = br.readLine()) != null) {
				String[] podaci = red.split(",");
				if (podaci[0].equals(Klijent.class.getName())) {
					klijenti.add(new Klijent(podaci[1], podaci[2], podaci[3], podaci[4], Long.parseLong(podaci[5]),
							new ArrayList<ZakupVozila>()));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Greška pri uèitavanju podataka iz datoteke!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	public void ucitajVozila() {
		String csv = "data/vozila.csv";
		try {
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String red = null;
			while ((red = br.readLine()) != null) {
				String[] podaci = red.split(",");

				String marka = podaci[1];
				String model = podaci[2];
				String brojSasije = podaci[3];
				int kilometraza = Integer.parseInt(podaci[4]);
				Gorivo gorivo = Gorivo.valueOf(podaci[5]);

				if (podaci[0].equals(TeretnoVozilo.class.getName())) {
					double kapacitet = Double.parseDouble(podaci[6]);
					vozila.add(new TeretnoVozilo(marka, model, brojSasije, kilometraza, gorivo, kapacitet));
				} else if (podaci[0].equals(PutnickoVozilo.class.getName())) {
					int brojSedista = Integer.parseInt(podaci[7]);
					Karoserija karoserija = Karoserija.valueOf(podaci[8]);
					vozila.add(new PutnickoVozilo(marka, model, brojSasije, kilometraza, gorivo, this, brojSedista,
							karoserija));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Greška pri uèitavanju podataka iz datoteke!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	public void ucitajZakupljenaVozila() {
		String csv = "data/zakupiVozila.csv";
		try {
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String red = null;
			while ((red = br.readLine()) != null) {
				String[] podaci = red.split(",");
				String dateFormat = "dd/MM/yyyy";

				String klijentKorisnickoIme = podaci[0];
				String klijentLozinka = podaci[1];
				Klijent klijent = nadjiKlijenta(klijentKorisnickoIme, klijentLozinka);

				String brojSasije = podaci[2];
				Vozilo vozilo = nadjiVozilo(brojSasije);

				Date zakupljenoOd = new SimpleDateFormat(dateFormat).parse(podaci[3]);
				Date zakupljenoDo = new SimpleDateFormat(dateFormat).parse(podaci[4]);

				ChronoUnit jedinicaVremena = ChronoUnit.valueOf(podaci[5].toUpperCase());

				String primioKorisnickoIme = podaci[6];
				String primioLozinka = podaci[7];
				Agent agentPrimio = nadjiAgenta(primioKorisnickoIme, primioLozinka);

				String izdaoKorisnickoIme = podaci[8];
				String izdaoLozinka = podaci[9];
				Agent agentIzdao = nadjiAgenta(izdaoKorisnickoIme, izdaoLozinka);

				Double vrijednostZakupa = null;
				if (!podaci[10].equals("null")) {
					vrijednostZakupa = Double.parseDouble(podaci[10]);
				}

				Osiguranje osiguranje = null;
				if (!podaci[11].equals("null")) {
					Double novcaniLimit = Double.parseDouble(podaci[11]);
					osiguranje = new Osiguranje(novcaniLimit);
				}

				Boolean prihvacenZakup = null;
				if (!podaci[12].equals("null")) {
					prihvacenZakup = Boolean.parseBoolean(podaci[12]);
				}

				ZakupVozila zakupljenoVozilo = new ZakupVozila(klijent, vozilo, zakupljenoOd, zakupljenoDo,
						jedinicaVremena, agentPrimio, agentIzdao, vrijednostZakupa, osiguranje, prihvacenZakup);
				zakupljenaVozila.add(zakupljenoVozilo);

				if (klijent != null && vozilo != null) {
					klijent.getZakupiVozila().add(zakupljenoVozilo);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Greška pri uèitavanju podataka iz datoteke!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	// Zapisivanje podataka u datoteke
	public void sacuvajKorisnike() {
		try {
			FileOutputStream fos = new FileOutputStream("data/korisnici.csv");
			for (Klijent klijent : getKlijenti()) {
				fos.write(klijent.uCSV().getBytes());
				fos.flush();
			}
			for (Agent agent : getAgenti()) {
				fos.write(agent.uCSV().getBytes());
				fos.flush();
			}
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Nije moguæe zapisivanje u fajl!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	public void sacuvajVozila() {
		try {
			FileOutputStream fos = new FileOutputStream("data/vozila.csv");
			for (Vozilo vozilo : vozila) {
				if (vozilo instanceof PutnickoVozilo) {
					fos.write(((PutnickoVozilo) vozilo).uCSV().getBytes());
				} else if (vozilo instanceof TeretnoVozilo) {
					fos.write(((TeretnoVozilo) vozilo).uCSV().getBytes());
				}
				fos.flush();
			}
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Nije moguæe zapisivanje u fajl!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	public void sacuvajZakupljenaVozila() {
		try {
			FileOutputStream fos = new FileOutputStream("data/zakupiVozila.csv");
			for (ZakupVozila zakupljeno : getZakupljenaVozila()) {
				fos.write(zakupljeno.uCSV().getBytes());
				fos.flush();
			}
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nije pronaðen fajl!");
		} catch (IOException e) {
			System.out.println("Nije moguæe zapisivanje u fajl!");
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}

	// Metode
	public Agent nadjiAgenta(String korisnickoIme, String lozinka) {
		if (korisnickoIme == null || lozinka == null || korisnickoIme.isEmpty())
			return null;

		for (Agent agent : getAgenti()) {
			if (agent.getKorisnickoIme().equals(korisnickoIme) && agent.getLozinka().equals(lozinka))
				return agent;
		}
		return null;
	}

	public Klijent nadjiKlijenta(String korisnickoIme, String lozinka) {
		if (korisnickoIme == null || lozinka == null || korisnickoIme.isEmpty())
			return null;

		for (Klijent klijent : getKlijenti()) {
			if (klijent.getKorisnickoIme().equals(korisnickoIme) && klijent.getLozinka().equals(lozinka))
				return klijent;
		}
		return null;
	}

	public Vozilo nadjiVozilo(String brojSasije) {
		for (Vozilo vozilo : vozila) {
			if (vozilo.getBrojSasije().equalsIgnoreCase(brojSasije.toLowerCase()))
				return vozilo;
		}
		return null;
	}

	// pomocna metoda za 5. dodavanje vozila - meni agent izbor 2
	public boolean brojSasijePronadjen(String brojSasije) {
		for (Vozilo v : getVozila()) {
			if (v.getBrojSasije().toLowerCase().equals(brojSasije.toLowerCase()))
				return true;
		}
		return false;
	}

	// pomocna metoda za 8. registracija klijenta - meni agent izbor 4
	public boolean klijentIdPronadjen(long id) {
		for (Klijent k : getKlijenti()) {
			if (k.getId() == id)
				return true;
		}
		return false;
	}

	// pomocna metoda za 8. registracija klijenta - meni agent izbor 4
	public boolean klijentKorisnickoPronadjeno(String korisnickoIme) {
		for (Klijent k : getKlijenti()) {
			if (k.getKorisnickoIme().equals(korisnickoIme))
				return true;
		}
		return false;
	}

}
