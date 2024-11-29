package bowling;

import java.util.HashMap;
import java.util.Map;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {

	private Map<String, PartieMonoJoueur> partiesMultiJoueurs;
	private String[] nomsJoueurs;
	private int joueurCourant;
	private boolean partieEnCours;

	public PartieMultiJoueurs() {
		partiesMultiJoueurs = new HashMap<>();
		partieEnCours = false;
	}

	@Override
	public String demarreNouvellePartie(String[] nomsJoueurs) throws IllegalArgumentException {
		if (nomsJoueurs == null || nomsJoueurs.length == 0) {
			throw new IllegalArgumentException("Le tableau des noms de joueurs ne peut pas être vide ou null");
		}
		this.nomsJoueurs = nomsJoueurs;
		partiesMultiJoueurs.clear();
		for (String nom : nomsJoueurs) {
			partiesMultiJoueurs.put(nom, new PartieMonoJoueur());
		}
		joueurCourant = 0;
		partieEnCours = true;
		return "Prochain tir : joueur " + nomsJoueurs[joueurCourant] + ", tour n° 1, boule n° 1";
	}

	@Override
	public String enregistreLancer(int quillesAbattues) throws IllegalStateException {
		if (!partieEnCours) {
			throw new IllegalStateException("La partie n'est pas démarrée.");
		}

		String vjoueurCourant = nomsJoueurs[joueurCourant];
		PartieMonoJoueur partie = partiesMultiJoueurs.get(vjoueurCourant);

		boolean continuerTour = partie.enregistreLancer(quillesAbattues);

		if (!continuerTour) {
			// Passer au joueur suivant
			joueurCourant = (joueurCourant + 1) % nomsJoueurs.length;
			vjoueurCourant = nomsJoueurs[joueurCourant];
			partie = partiesMultiJoueurs.get(vjoueurCourant);
		}

		if (partiesMultiJoueurs.values().stream().allMatch(PartieMonoJoueur::estTerminee)) {
			partieEnCours = false;
			return "Partie terminée";
		}

		return "Prochain tir : joueur " + vjoueurCourant + ", tour n° " + partie.numeroTourCourant() +
			", boule n° " + partie.numeroProchainLancer();
	}

	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		if (!partiesMultiJoueurs.containsKey(nomDuJoueur)) {
			throw new IllegalArgumentException("Joueur inconnu");
		}
		return partiesMultiJoueurs.get(nomDuJoueur).score();
	}

	public Map<String, PartieMonoJoueur> getPartiesMultiJoueurs() {
		return partiesMultiJoueurs;
	}

	public String[] getNomsJoueurs() {
		return nomsJoueurs;
	}

	public int getJoueurCourant() {
		return joueurCourant;
	}

	public boolean estEnCours() {
		return partieEnCours;
	}
}
