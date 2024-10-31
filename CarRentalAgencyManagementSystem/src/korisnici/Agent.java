package korisnici;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import autoKuca.AutoKuca;
import autoKuca.ZakupVozila;
import enumeracijeInterfejsi.Gorivo;
import enumeracijeInterfejsi.Karoserija;
import enumeracijeInterfejsi.PodaciCSV;
import vozila.PutnickoVozilo;
import vozila.TeretnoVozilo;
import vozila.Vozilo;

public class Agent extends Korisnik implements PodaciCSV<Agent> {
	private AutoKuca autoKuca;

	public Agent(String korisnickoIme, String lozinka, String ime, String prezime, AutoKuca autoKuca) {
		super(korisnickoIme, lozinka, ime, prezime);
		this.autoKuca = autoKuca;
	}

	public AutoKuca getAutoKuca() {
		return autoKuca;
	}

	public void setAutoKuca(AutoKuca autoKuca) {
		this.autoKuca = autoKuca;
	}

	@Override
	public String uCSV() {
		return Agent.class.getName() + "," + korisnickoIme + "," + lozinka + "," + ime + "," + prezime + ","
				+ autoKuca.getNaziv() + "\n";
	}

	public void vratiVozilo() {
		Scanner sc = new Scanner(System.in);
		Date datumVracanja = null;

		// Unos datuma vraæanja sa provjerom unosa
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite datum vraæanja [Format dd/MM/yyyy]: ");
			try {
				datumVracanja = new SimpleDateFormat("dd/MM/yyyy").parse(sc.next());
				break;
			} catch (ParseException e) {
				System.out.println("Greška pri parsiranju, pokušajte ponovo!");
			}
		}

		// Pronaði zakup koji treba završiti
		ZakupVozila zakupZaVracanje = null;
		System.out.println("--------------------------------------");
		System.out.print("Unesite broj šasije vozila koje vraæate: ");
		String unesenibrojSasije = sc.next();

		// Proveravamo da li postoji aktivan zakup sa tim vozilom
		for (ZakupVozila zakup : autoKuca.getZakupljenaVozila()) {
			if (zakup.getVozilo() != null && zakup.getVozilo().getBrojSasije().equals(unesenibrojSasije)
					&& zakup.isPrihvacenZakup() && zakup.getPrimioVozilo() != null) {
				zakupZaVracanje = zakup;
				break;
			}
		}

		if (zakupZaVracanje == null) {
			System.out.println("Nije pronaðen aktivan zakup za vozilo sa brojem šasije: " + unesenibrojSasije);
			return;
		}

		// Unos kilometraže
		System.out.println("--------------------------------------");
		System.out.print("Unesite kilometražu koju je vozilo prešlo tokom zakupa: ");
		int predjeniKilometri = 0;
		try {
			predjeniKilometri = sc.nextInt();
			int kilometraza = zakupZaVracanje.getVozilo().getKilometraza();
			zakupZaVracanje.getVozilo().setKilometraza(kilometraza + predjeniKilometri);
		} catch (InputMismatchException e) {
			System.out.println("Neispravan unos kilometraže.");
			return;
		}

		// Po formuli kao i u odobravanjuzakupavozila izraèunati novu vrednost zakupa
		Date datumPocetka = zakupZaVracanje.getZakupljenoOd();
		long razlikaUVremenu = datumVracanja.getTime() - datumPocetka.getTime(); // u milisekundama
		long razlikaUDanima = TimeUnit.MILLISECONDS.toDays(razlikaUVremenu);

		double koeficijentGoriva = 0;
		Gorivo gorivo = zakupZaVracanje.getVozilo().getGorivo();
		switch (gorivo.toString()) {
		case "BENZIN":
			koeficijentGoriva = 2.5;
			break;
		case "DIZEL":
			koeficijentGoriva = 1.98;
			break;
		case "GAS":
			koeficijentGoriva = 1.5;
			break;
		case "STRUJA":
			koeficijentGoriva = 1.0;
			break;
		case "HIBRID":
			koeficijentGoriva = 1.15;
			break;
		}

		double novaCenaZakupa = razlikaUDanima * 10 + koeficijentGoriva * 15;

		// Dodavanje vrijednosti u zavisnosti od tipa vozila
		if (zakupZaVracanje.getVozilo() instanceof PutnickoVozilo) {
			double koeficijentKaroserije = 0;
			Karoserija karoserija = ((PutnickoVozilo) zakupZaVracanje.getVozilo()).getKaroserija();
			switch (karoserija.toString()) {
			case "HECBEK":
				koeficijentKaroserije = 0.8;
				break;
			case "KABRIOLET":
				koeficijentKaroserije = 2.5;
				break;
			case "LIMUZINA":
				koeficijentKaroserije = 1.0;
				break;
			case "KARAVAN":
				koeficijentKaroserije = 1.3;
				break;
			case "SUV":
				koeficijentKaroserije = 1.6;
				break;
			case "PIKAP":
				koeficijentKaroserije = 1.5;
				break;
			case "MINIVAN":
				koeficijentKaroserije = 1.85;
				break;
			}
			novaCenaZakupa += koeficijentKaroserije * 8;
		} else if (zakupZaVracanje.getVozilo() instanceof TeretnoVozilo) {
			double kapacitet = ((TeretnoVozilo) zakupZaVracanje.getVozilo()).getKapacitet();
			novaCenaZakupa += kapacitet * 1.1;
		}

		// Ažuriranje informacija u zakupu
		zakupZaVracanje.setVrijednostZakupa(novaCenaZakupa);
		zakupZaVracanje.setZakupljenoDo(datumVracanja);
		zakupZaVracanje.setPrimioVozilo(this); // Agent postavlja sebe kao osobu koja je primila vozilo

		System.out.println("--------------------------------------");
		System.out.println("Vozilo je uspešno vraæeno.");
		System.out.println("Nova vrednost zakupa: " + novaCenaZakupa);

		autoKuca.sacuvajZakupljenaVozila();
		autoKuca.sacuvajKorisnike();
	}

	public void dodajVozilo() {
		Scanner sc = new Scanner(System.in);
		System.out.println("--------------------------------------");
		System.out.println("Unesite broj ispred vozila koje želite da dodate!");

		int izbor;
		while (true) {
			System.out.println("1. Putnièko vozilo\n2. Teretno vozilo");
			System.out.println("--------------------------------------");
			System.out.print("Vaš izbor: ");
			try {
				izbor = sc.nextInt();
				sc.nextLine();
				if (izbor == 1 || izbor == 2)
					break;
				else
					System.out.println("Neispravan unos! Pokušajte ponovo.");
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Molimo unesite broj.");
				sc.nextLine();
			}
		}

		// Unos jedinstvenog broja šasije
		String brojSasije;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite broj šasije (17 karaktera): ");
			brojSasije = sc.nextLine().trim();
			if (brojSasije.length() == 17) {
				if (!autoKuca.brojSasijePronadjen(brojSasije)) {
					break;
				} else {
					System.out.println("--------------------------------------");
					System.out.println("Vozilo sa brojem šasije " + brojSasije + " veæ postoji. Pokušajte ponovo.");
				}
			} else {
				System.out.println("Broj šasije mora imati taèno 17 slova i brojeva. Pokušajte ponovo.");
			}
		}

		// Unos atributa za vozilo
		System.out.println("--------------------------------------");
		System.out.print("Marka vozila: ");
		String marka = sc.nextLine().trim();

		System.out.println("--------------------------------------");
		System.out.print("Model vozila: ");
		String model = sc.nextLine().trim();

		int kilometraza;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Kilometraža: ");
			try {
				kilometraza = Integer.parseInt(sc.nextLine().trim());
				if (kilometraza >= 0)
					break;
				else
					System.out.println("Kilometraža ne može biti manja od 0. Pokušajte ponovo.");
			} catch (NumberFormatException e) {
				System.out.println("Unesite validan broj za kilometražu.");
			}
		}

		Gorivo gorivo = null;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.println("Gorivo:\n1. Benzin\n2. Dizel\n3. Gas\n4. Struja\n5. Hibrid");
			System.out.println("--------------------------------------");
			System.out.print("Unesite tip goriva: ");
			try {
				gorivo = Gorivo.valueOf(sc.next().toUpperCase());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Neispravan unos goriva! Pokušajte ponovo (npr. 'Benzin').");
			}
		}

		// Dodavanje vozila u zavisnosti od izbora tipa
		switch (izbor) {
		case 1:
			dodajPutnickoVozilo(sc, brojSasije.toUpperCase(), marka, model, kilometraza, gorivo);
			break;
		case 2:
			dodajTeretnoVozilo(sc, brojSasije.toUpperCase(), marka, model, kilometraza, gorivo);
			break;
		}

		// Èuvanje novog vozila
		autoKuca.sacuvajVozila();
	}

	private void dodajPutnickoVozilo(Scanner sc, String brojSasije, String marka, String model, int kilometraza,
			Gorivo gorivo) {
		int brojSedista = 0;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Broj sjedišta: ");
			try {
				brojSedista = sc.nextInt();
				if (brojSedista > 0)
					break;
				else
					System.out.println("Broj sjedišta mora biti pozitivan broj.");
			} catch (InputMismatchException e) {
				System.out.println("Unesite validan broj za sjedišta.");
				sc.nextLine();
			}
		}

		Karoserija karoserija = null;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.println(
					"Karoserija:\n1. Hecbek\n2. Kabriolet\n3. Limuzina\n4. Karavan\n5. Suv\n6. Pikap\n7. Minivan");
			System.out.println("--------------------------------------");
			System.out.print("Unesite tip karoserije: ");
			try {
				karoserija = Karoserija.valueOf(sc.next().toUpperCase());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Neispravan unos naziva karoserije! Pokušajte ponovo (npr. 'Hecbek').");
				System.out.println("--------------------------------------");
			}
		}

		PutnickoVozilo pv = new PutnickoVozilo(marka, model, brojSasije, kilometraza, gorivo, autoKuca, brojSedista,
				karoserija);
		autoKuca.getVozila().add(pv);
		System.out.println("--------------------------------------");
		System.out.println("Uspješno ste dodali putnièko vozilo!");
	}

	private void dodajTeretnoVozilo(Scanner sc, String brojSasije, String marka, String model, int kilometraza,
			Gorivo gorivo) {
		double kapacitet = 0.0;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Kapacitet (u tonama):");
			try {
				kapacitet = sc.nextDouble();
				if (kapacitet > 0)
					break;
				else
					System.out.println("Kapacitet mora biti pozitivan broj. Pokušajte ponovo.");
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Molimo unesite broj za kapacitet.");
				sc.nextLine();
			}
		}

		TeretnoVozilo tv = new TeretnoVozilo(marka, model, brojSasije, kilometraza, gorivo, kapacitet);
		autoKuca.getVozila().add(tv);
		System.out.println("--------------------------------------");
		System.out.println("Uspješno ste dodali teretno vozilo!");
	}

	public void brisiVozilo() {
		System.out.println("----------------------------------------------------------------------------");
		// Kreiranje liste brojeva šasija za trenutno zakupljena vozila
		List<String> zakupljenaVozila = new ArrayList<>();
		for (Klijent klijent : getAutoKuca().getKlijenti()) {
			for (ZakupVozila zakup : klijent.getZakupiVozila()) {
				zakupljenaVozila.add(zakup.getVozilo().getBrojSasije());
			}
		}

		// Prikazivanje samo vozila koja nisu zakupljena.
		List<String> nezakupljenaVozila = new ArrayList<>();
		int br = 1;
		for (Vozilo vozilo : getAutoKuca().getVozila()) {
			if (!zakupljenaVozila.contains(vozilo.getBrojSasije())) {
				System.out.println((br + 1) + ". " + vozilo.getClass().getSimpleName() + ", Marka:" + vozilo.getMarka()
						+ ", Model:" + vozilo.getModel() + ", Broj šasije:" + vozilo.getBrojSasije());
				nezakupljenaVozila.add(vozilo.getBrojSasije());
			}
		}

		if (nezakupljenaVozila.isEmpty()) {
			System.out.println("Nema dostupnih vozila za brisanje!");
			return;
		}

		// Unos broja šasije za vozilo koje æe se obrisati
		Scanner sc = new Scanner(System.in);
		String uneseniBrojSasije;
		while (true) {
			System.out.println("----------------------------------------------------------------------------");
			System.out.print("Unesite broj šasije vozila koje želite da želite da izbrišete: ");
			uneseniBrojSasije = sc.next();
			if (nezakupljenaVozila.contains(uneseniBrojSasije))
				break;
			else
				System.out.println("Neispravan unos. Pokušajte ponovo.");
		}

//		 Brisanje izvršiti i u datoteci.
		final String finalUnos = uneseniBrojSasije;
		if (autoKuca.getVozila().removeIf(vozilo -> (vozilo.getBrojSasije().equals(finalUnos)))) {
			autoKuca.sacuvajVozila();
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("Uspješno ste obrisali vozilo!");
		} else {
			System.out.println("Greška pri brisanju vozila!");
		}
	}

	public void registrujKlijenta() {
		System.out.println("----------Registracija klijenta-----------");
		Scanner sc = new Scanner(System.in);
		long id = 0;
		while (true) {
			try {
				System.out.print("Unesite ID klijenta: ");
				id = sc.nextLong();
				sc.nextLine();
				if (!autoKuca.klijentIdPronadjen(id))
					break;
				else
					System.out.println("ID u upotrebi. Pokušajte ponovo.");
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos ID-a. Molimo unesite broj.");
				sc.nextLine();
			}
		}

		String korisnickoIme; // mora biti jedinstveno zbog prijave korisnika
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite korisnièko ime: ");
			korisnickoIme = sc.nextLine();
			if (korisnickoIme.isEmpty())
				System.out.println("Neispravan unos! Pokušajte ponovo.");
			else if (!autoKuca.klijentKorisnickoPronadjeno(korisnickoIme))
				break;
			else
				System.out.println("Korisnièko ime u upotrebi. Pokušajte ponovo.");
		}

		String lozinka;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite lozinku: ");
			lozinka = sc.nextLine();
			if (lozinka.isEmpty())
				System.out.println("Neispravan unos! Pokušajte ponovo.");
			else
				break;
		}

		String ime;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite ime: ");
			ime = sc.nextLine();
			if (ime.isEmpty())
				System.out.println("Neispravan unos! Pokušajte ponovo.");
			else
				break;
		}

		String prezime;
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite prezime: ");
			prezime = sc.nextLine();
			if (prezime.isEmpty())
				System.out.println("Neispravan unos! Pokušajte ponovo.");
			else
				break;
		}

		Klijent klijent = new Klijent(korisnickoIme, lozinka, ime, prezime, id, null);
		autoKuca.getKlijenti().add(klijent);

		System.out.println("Uspješno ste registrovali klijenta!");

		autoKuca.sacuvajKorisnike();
	}

	public void odobriZakupVozila() {
		List<ZakupVozila> zahtjevi = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("--------------------------------------");

		for (ZakupVozila zakup : autoKuca.getZakupljenaVozila()) {
			if (zakup.getPrimioVozilo() == null && zakup.isPrihvacenZakup() == false) {
				System.out.println("Vozilo: " + zakup.getVozilo().getBrojSasije() + ", predao zahtjev: "
						+ zakup.getKlijent().getKorisnickoIme() + ", zakup od: "
						+ formatter.format(zakup.getZakupljenoOd()) + ", do: "
						+ formatter.format(zakup.getZakupljenoDo()));
				zahtjevi.add(zakup);
			}
		}

		if (zahtjevi.isEmpty()) {
			System.out.println("Nema zahtjeva koje je moguæe odobriti!");
			return;
		}

		ZakupVozila nadjenZahtjev = null;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unosom broja šasije birate zakup koji æete odobriti: ");
			String unijetiBrojSasije = sc.next();

			for (ZakupVozila zakup : zahtjevi) {
				if (zakup.getVozilo().getBrojSasije().equalsIgnoreCase(unijetiBrojSasije)) {
					nadjenZahtjev = zakup;
					break;
				}
			}
			if (nadjenZahtjev != null)
				break;
			System.out.println("Neispravan unos! Pokušajte ponovo.");
		}

		// Izraèunati procenu zakupa na osnovu formule
		Date d1 = nadjenZahtjev.getZakupljenoOd();
		Date d2 = nadjenZahtjev.getZakupljenoDo();

		long razlikaUVremenu = d2.getTime() - d1.getTime(); // u milisekundama
		long razlikaUDanima = TimeUnit.MILLISECONDS.toDays(razlikaUVremenu) % 365;

		double koeficijentGoriva = 0;
		Gorivo gorivo = nadjenZahtjev.getVozilo().getGorivo();
		switch (gorivo.toString()) {
		case "BENZIN":
			koeficijentGoriva = 2.5;
			break;
		case "DIZEL":
			koeficijentGoriva = 1.98;
			break;
		case "GAS":
			koeficijentGoriva = 1.5;
			break;
		case "STRUJA":
			koeficijentGoriva = 1.0;
			break;
		case "HIBRID":
			koeficijentGoriva = 1.15;
			break;
		}

		double cenaZakupa = razlikaUDanima * 10 + koeficijentGoriva * 15;

		double koeficijentKaroserije = 0;
		if (nadjenZahtjev.getVozilo() instanceof PutnickoVozilo) {
			Karoserija karoserija = ((PutnickoVozilo) nadjenZahtjev.getVozilo()).getKaroserija();
			switch (karoserija.toString()) {
			case "HECBEK":
				koeficijentKaroserije = 0.8;
				break;
			case "KABRIOLET":
				koeficijentKaroserije = 2.5;
				break;
			case "LIMUZINA":
				koeficijentKaroserije = 1.0;
				break;
			case "KARAVAN":
				koeficijentKaroserije = 1.3;
				break;
			case "SUV":
				koeficijentKaroserije = 1.6;
				break;
			case "PIKAP":
				koeficijentKaroserije = 1.5;
				break;
			case "MINIVAN":
				koeficijentKaroserije = 1.85;
				break;
			}
			cenaZakupa += koeficijentKaroserije * 8;
		} else if (nadjenZahtjev.getVozilo() instanceof TeretnoVozilo) {
			double kapacitet = ((TeretnoVozilo) nadjenZahtjev.getVozilo()).getKapacitet();
			cenaZakupa += kapacitet * 1.1;
		}

		nadjenZahtjev.setVrijednostZakupa(cenaZakupa);

		// Unos limita ukoliko je klijent odabrao osiguranje
		if (nadjenZahtjev.getOsiguranje() != null) {
			double novcaniLimit;
			while (true) {
				try {
					System.out.println("--------------------------------------");
					System.out.print("Unesite novèani limit koji osiguranje pokriva: ");
					novcaniLimit = sc.nextDouble();
					if (novcaniLimit > 0) {
						nadjenZahtjev.getOsiguranje().setNovcaniLimit(novcaniLimit);
						break;
					}
					System.out.println("Limit mora biti veæi od 0.");
				} catch (Exception e) {
					System.out.println("Nevažeæi unos. Molimo pokušajte ponovo.");
					sc.next();
				}
			}
		}

		System.out.println("--------------------------------------");
		System.out.println("Odobrili ste zakup!");
		System.out.println("--------------------------------------");
		nadjenZahtjev.getVozilo().unajmi(nadjenZahtjev, nadjenZahtjev.getKlijent(), d1, d2,
				nadjenZahtjev.getJedinicaVremena(), this, cenaZakupa, nadjenZahtjev.getOsiguranje());

		autoKuca.sacuvajZakupljenaVozila();
		autoKuca.sacuvajKorisnike();
	}
}
