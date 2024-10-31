package korisnici;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import autoKuca.Osiguranje;
import autoKuca.ZakupVozila;
import enumeracijeInterfejsi.Gorivo;
import enumeracijeInterfejsi.Karoserija;
import enumeracijeInterfejsi.PodaciCSV;
import vozila.PutnickoVozilo;
import vozila.TeretnoVozilo;
import vozila.Vozilo;

public class Klijent extends Korisnik implements PodaciCSV<Klijent> {
	private long id;
	private List<ZakupVozila> zakupiVozila;

	public Klijent(String korisnickoIme, String lozinka, String ime, String prezime, long id,
			List<ZakupVozila> zakupiVozila) {
		super(korisnickoIme, lozinka, ime, prezime);
		this.id = id;
		this.zakupiVozila = zakupiVozila;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<ZakupVozila> getZakupiVozila() {
		return zakupiVozila;
	}

	public void setZakupiVozila(List<ZakupVozila> zakupiVozila) {
		this.zakupiVozila = zakupiVozila;
	}

	@Override
	public String uCSV() {
		return Klijent.class.getName() + "," + korisnickoIme + "," + lozinka + "," + ime + "," + prezime + "," + id
				+ "\n";
	}

	public boolean zahtevanjeZakupaVozila(final List<Vozilo> svaVozila, final List<ZakupVozila> zakupljenaVozila) {
		int putnickoTeretno;
		Scanner sc = new Scanner(System.in);
		while (true) {
			try {
				System.out.println("Koji tip vozila želite zakupiti?");
				System.out.println("1. Putnièko vozilo \n2. Teretno vozilo");
				System.out.println("--------------------------------------");
				System.out.print("Vaš izbor: ");
				putnickoTeretno = sc.nextInt();
				if (putnickoTeretno == 1 || putnickoTeretno == 2)
					break;
				else
					System.out.println("Neispravan unos! Pokušajte ponovo.");
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Unesite broj 1 ili 2.");
				System.out.println("--------------------------------------");
				sc.nextLine();
			}
		}

		// Izbor goriva
		Gorivo gorivo = null;
		while (true) {
			System.out.println("Suzavanje pretrage po gorivu");
			System.out.println("1. Benzin\n2. Dizel\n3. Gas\n4. Struja\n5. Hibrid");
			System.out.println("--------------------------------------");
			System.out.print("Unesite tip goriva: ");
			try {
				gorivo = Gorivo.valueOf(sc.next().toUpperCase());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Neispravan unos! Pokušajte ponovo (npr. 'Benzin').");
				System.out.println("--------------------------------------");
			}
		}

		// Ako je putnicko vozilo, filtrira se po karoseriji
		Karoserija karoserija = null;
		if (putnickoTeretno == 1) {
			while (true) {
				System.out.println("--------------------------------------");
				System.out.println("Suzavanje pretrage po karoseriji");
				System.out.println("1. Hecbek\n2. Kabriolet\n3. Limuzina\n4. Karavan\n5. Suv\n6. Pikap\n7. Minivan");
				System.out.println("--------------------------------------");
				System.out.print("Unesite tip karoserije: ");
				try {
					karoserija = Karoserija.valueOf(sc.next().toUpperCase());
					break;
				} catch (IllegalArgumentException e) {
					System.out.println("Neispravan unos! Pokušajte ponovo (npr. 'Hecbek').");
					System.out.println("--------------------------------------");
				}
			}
		}

		// Prikaz vozila koja zadovoljavaju kriterijume pretrage
		List<Vozilo> pronadjenaVozila = new ArrayList<>();
		int i = 1;
		for (final Vozilo vozilo : svaVozila) {
			if ((putnickoTeretno == 1 && vozilo instanceof PutnickoVozilo && vozilo.getGorivo().equals(gorivo)
					&& ((PutnickoVozilo) vozilo).getKaroserija().equals(karoserija))
					|| (putnickoTeretno == 2 && vozilo instanceof TeretnoVozilo && vozilo.getGorivo().equals(gorivo))) {

				pronadjenaVozila.add(vozilo);
				System.out.println(i + ". " + vozilo.toString());
				i++;
			}
		}

		if (pronadjenaVozila.isEmpty()) {
			System.out.println("Nažalost ne postoji vozilo sa ovim filterom!");
			System.out.println("--------------------------------------");
			return false;
		}

		// Izbor zeljenog vozila unosom rednog broja
		System.out.println("--------------------------------------");
		Vozilo finalVozilo = null;
		while (true) {
			try {
				System.out.print("Vaš izbor: ");
				int zeljenoVozilo = sc.nextInt();
				if (zeljenoVozilo >= 1 && zeljenoVozilo <= pronadjenaVozila.size()) {
					finalVozilo = pronadjenaVozila.get(zeljenoVozilo - 1);
					break;
				} else {
					System.out.println("Neispravan unos! Pokušajte ponovo.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Unesite redni broj vozila.");
				System.out.println("--------------------------------------");
				sc.nextLine(); // ciscenje toka unosa
			}
		}

		// Klijent unosi od kada i do kada zeli zakupiti vozilo
		System.out.println("--------------------------------------");
		Date zakupiOd = null;
		Date zakupiDo = null;
		String dateFormat = "dd/MM/yyyy";
		while (zakupiOd == null || zakupiDo == null) {
			try {
				if (zakupiOd == null) {
					System.out.print("Od kada želite zakupiti vozilo?\n[Format dd/MM/yyyy]:");
					zakupiOd = new SimpleDateFormat(dateFormat).parse(sc.next());
				} else {
					System.out.print("Do kada želite zakupiti vozilo?\n[Format dd/MM/yyyy]:");
					zakupiDo = new SimpleDateFormat(dateFormat).parse(sc.next());
				}
			} catch (ParseException e) {
				System.out.println("Neispravan unos! Pokušajte ponovo (npr. '20/05/2001').");
			}
		}

		// Izbor jedinice za trajanje zakupa
		ChronoUnit jedinicaVremena = null;
		while (true) {
			System.out.println("Unesite jedinicu kojom se raèuna proteklo vrijeme zakupa (dani, mjeseci, godine): ");
			String jedinicaInput = sc.next().toUpperCase();
			switch (jedinicaInput) {
			case "DANI":
				jedinicaVremena = ChronoUnit.DAYS;
				break;
			case "MJESECI":
				jedinicaVremena = ChronoUnit.MONTHS;
				break;
			case "GODINE":
				jedinicaVremena = ChronoUnit.YEARS;
				break;
			default:
				System.out.println("Neispravan unos! Pokušajte ponovo.");
				continue;
			}
			break;
		}

		// Izbor osiguranja u periodu zakupa
		System.out.println("--------------------------------------");
		System.out.print("Želite li da vozilo bude osigurano u periodu zakupa? [da/ne]:");

		Osiguranje osiguranje = null;
		if (sc.next().equalsIgnoreCase("da"))
			osiguranje = new Osiguranje(0.0);

		// Kreiranje i dodavanje zakupa u listu
		ZakupVozila zakup = new ZakupVozila(this, finalVozilo, zakupiOd, zakupiDo, jedinicaVremena, null, null, 0.0,
				osiguranje, false);
		zakupiVozila.add(zakup);
		System.out.println("--------------------------------------");
		System.out.println("Uspješno ste podnijeli zahtjev za zakup vozila!");
		return true;
	}

	public void produzavanjeZakupaVozila(final List<ZakupVozila> zakupiVozila) {
		Date date = new Date();
		List<ZakupVozila> nadjeniZakupi = new ArrayList<>();

		for (ZakupVozila zakup : this.getZakupiVozila()) {
			// Provjerava da li zakup pripada klijentu, nije istekao i vozilo nije vraceno
			if (zakup.getKlijent().equals(this) && zakup.getZakupljenoDo().after(date)
					&& zakup.getPrimioVozilo() == null) {
				nadjeniZakupi.add(zakup);
			}
		}

		if (nadjeniZakupi.isEmpty()) {
			System.out.println("Nema dostupnih zakupa za produženje.");
			return;
		}

		// Prikaz zakupa koje korisnik moze da produzi
		for (int i = 0; i < nadjeniZakupi.size(); i++) {
			ZakupVozila zakup = nadjeniZakupi.get(i);
			System.out.println((i + 1) + ". " + zakup.getVozilo().toString());
			System.out.println("Zakup vozila traje do: " + zakup.getZakupljenoDo());
		}

		// Izbor zakupa za produzenje
		Scanner sc = new Scanner(System.in);
		int izborZakupa = 0;
		while (true) {
			try {
				System.out.println("--------------------------------------");
				System.out.print("Unesite broj zakupa koji želite da produžite: ");
				izborZakupa = sc.nextInt();
				if (izborZakupa >= 1 && izborZakupa <= nadjeniZakupi.size()) {
					break;
				} else {
					System.out.println("Neispravan unos! Pokušajte ponovo.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Pokušajte ponovo.");
				sc.nextLine();
			}

		}
		ZakupVozila odabraniZakup = nadjeniZakupi.get(izborZakupa - 1);

		// Unos novog datuma zavrsetka zakupa
		Date noviDatumDo = null;
		String dateFormat = "dd/MM/yyyy";
		while (true) {
			System.out.println("--------------------------------------");
			System.out.print("Unesite novi datum do kada želite da produžite zakup\n[Format dd/MM/yyyy]");
			try {
				noviDatumDo = new SimpleDateFormat(dateFormat).parse(sc.next());
				// Proveravamo da li je novi datum kasniji od trenutnog završetka zakupa
				if (noviDatumDo != null && noviDatumDo.after(odabraniZakup.getZakupljenoDo())) {
					break;
				} else {
					System.out.println("Novi datum mora biti poslije trenutnog datuma završetka zakupa.");
				}
			} catch (ParseException e) {
				System.out.println("Neispravan unos! Pokušajte ponovo.");
			}
		}

		odabraniZakup.setZakupljenoDo(noviDatumDo);
		System.out.println("Zakup je produžen do " + new SimpleDateFormat(dateFormat).format(noviDatumDo));
	}

}
