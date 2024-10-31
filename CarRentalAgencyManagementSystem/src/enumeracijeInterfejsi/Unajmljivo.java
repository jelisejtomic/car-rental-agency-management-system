package enumeracijeInterfejsi;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import autoKuca.Osiguranje;
import autoKuca.ZakupVozila;
import korisnici.Agent;
import korisnici.Klijent;

public interface Unajmljivo {
	boolean unajmi(ZakupVozila zakup, Klijent klijent, Date zakupljenoOd, Date zakupljenoDo, ChronoUnit jedinicaVremena,
			Agent izdaoVozilo, Double vrijednostZakupa, Osiguranje osiguranje);

	double vrati(ZakupVozila zakup, int predjeniKilometri);
}
