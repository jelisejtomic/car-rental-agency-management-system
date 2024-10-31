package aplikacija;

import java.util.InputMismatchException;
import java.util.Scanner;

import autoKuca.AutoKuca;
import korisnici.Agent;
import korisnici.Klijent;

public class Aplikacija {
	public static AutoKuca autoKuca;
	public static Scanner sc;

	private static void prijavaKorisnika() {
		sc = new Scanner(System.in);
		while (true) {
			System.out.println("----------Prijava korisnika-----------");

			System.out.print("Korisnièko ime: ");
			String korisnickoIme = sc.nextLine().trim();

			System.out.print("Lozinka: ");
			String lozinka = sc.nextLine().trim();

			Klijent klijent = autoKuca.nadjiKlijenta(korisnickoIme, lozinka);
			if (klijent != null) {
				meniKlijent(klijent);
				break;
			}

			Agent agent = autoKuca.nadjiAgenta(korisnickoIme, lozinka);
			if (agent != null) {
				meniAgent(agent);
				break;
			}

			System.out.println("--------------------------------------");
			System.out.println("Neispravno korisnièko ime ili lozinka!");
			System.out.println("--------------------------------------");
		}
	}

	private static void meniAgent(Agent agent) {
		sc = new Scanner(System.in);
		while (true) {
			System.out.println("--------------------------------------");
			System.out.println("------Uspješno prijavljen agent------");
			System.out.println("1. Vraæanje vozila");
			System.out.println("2. Dodavanje vozila");
			System.out.println("3. Brisanje vozila");
			System.out.println("4. Registracija klijenta");
			System.out.println("5. Odobravanje zakupa vozila");
			System.out.println("6. Odjavite se");
			System.out.println("--------------------------------------");
			System.out.print("Vaš izbor: ");
			try {
				int izbor = sc.nextInt();
				switch (izbor) {
				case 1:
					agent.vratiVozilo();
					break;
				case 2:
					agent.dodajVozilo();
					break;
				case 3:
					agent.brisiVozilo();
					break;
				case 4:
					agent.registrujKlijenta();
					break;
				case 5:
					agent.odobriZakupVozila();
					autoKuca.sacuvajZakupljenaVozila();
					autoKuca.sacuvajKorisnike();
					break;
				case 6:
					return; // odjavaAgenta
				default:
					System.out.println("Neispravan unos! Pokušajte ponovo.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Molimo unesite broj.");
				sc.nextLine();
			}
		}
	}

	private static void meniKlijent(Klijent klijent) {
		sc = new Scanner(System.in);
		while (true) {
			System.out.println("--------------------------------------");
			System.out.println("-----Uspješno prijavljen klijent------");
			System.out.println("1. Zahtevanje zakupa vozila");
			System.out.println("2. Produžavanje zakupa vozila");
			System.out.println("3. Odjavite se");
			System.out.println("--------------------------------------");
			System.out.print("Vaš izbor: ");
			try {
				int izbor = sc.nextInt();
				switch (izbor) {
				case 1:
					if (klijent.zahtevanjeZakupaVozila(autoKuca.getVozila(), autoKuca.getZakupljenaVozila())) {
						autoKuca.sacuvajZakupljenaVozila();
						autoKuca.sacuvajKorisnike();
					}
					break;
				case 2:
					klijent.produzavanjeZakupaVozila(autoKuca.getZakupljenaVozila());
					autoKuca.sacuvajZakupljenaVozila();
					autoKuca.sacuvajKorisnike();
					break;
				case 3:
					return; // odjavaKlijenta
				default:
					System.out.println("Neispravan unos! Pokušajte ponovo.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Neispravan unos! Molimo unesite broj.");
				sc.nextLine();
			}
		}
	}

	public static void main(String[] args) {
		try {
			autoKuca = new AutoKuca("Tomic", "Nikole Tesle 3");

			autoKuca.ucitajAgente();
			autoKuca.ucitajKlijente();
			autoKuca.ucitajVozila();
			autoKuca.ucitajZakupljenaVozila();

			sc = new Scanner(System.in);
			while (true) {
				System.out.println("--------------------------------------");
				System.out.println("1. Prijava korisnika");
				System.out.println("2. Izlaz iz aplikacije");
				System.out.println("--------------------------------------");
				System.out.print("Vaš izbor: ");
				try {
					int izbor = sc.nextInt();
					switch (izbor) {
					case 1:
						prijavaKorisnika();
						break;
					case 2:
						System.exit(0);
					default:
						System.out.println("Neispravan unos! Pokušajte ponovo.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Neispravan unos! Molimo unesite broj.");
					sc.nextLine();
				}
			}
		} catch (Exception e) {
			System.out.println("Greška: " + e);
		}
	}
}
