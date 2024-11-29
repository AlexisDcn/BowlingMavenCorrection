package bowling;

import java.util.HashMap;
import java.util.Map;

public class PartieMultiJoueurs implements IPartieMultiJoueurs{

	private Map<String, PartieMonoJoueur> partiesMultiJoueurs;
	private String[] nomsJoueurs;
	private int joueurCourant;
	private boolean partieEnCours;
	
	
	public PartieMultiJoueurs(){
		partiesMultiJoueurs = new HashMap<>();
		partieEnCours = false;
	}
	
	/**
	 * Démarre une nouvelle partie pour un groupe de joueurs
	 *
	 * @param nomsJoueurs un tableau des noms de joueurs (il faut au moins un joueur)
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 1, boule n° 1"
	 * @throws IllegalArgumentException si le tableau est vide ou null
	 */
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
	

	/**
	 * Enregistre le nombre de quilles abattues pour le joueur courant, dans le tour courant, pour la boule courante
	 *
	 * @param nombreDeQuillesAbattues : nombre de quilles abattue à ce lancer
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 5, boule n° 2",
	 * ou bien "Partie terminée" si la partie est terminée.
	 * @throws IllegalStateException si la partie n'est pas démarrée.
	 */
	@Override
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {if (!partieEnCours) {
		throw new IllegalStateException("La partie n'est pas démarrée.");
	}
		PartieMonoJoueur partieJoueurCourant = partiesMultiJoueurs.get(nomsJoueurs[joueurCourant]);
		boolean continuerTour = partieJoueurCourant.enregistreLancer(nombreDeQuillesAbattues);
		if (!continuerTour) {
			joueurCourant = (joueurCourant + 1) % nomsJoueurs.length;
		}
		if (partiesMultiJoueurs.values().stream().allMatch(PartieMonoJoueur::estTerminee)) {
			partieEnCours = false;
			return "Partie terminée";
		}
		return "Prochain tir : joueur " + nomsJoueurs[joueurCourant] + ", tour n° " +
			partieJoueurCourant.numeroTourCourant() + ", boule n° " + partieJoueurCourant.numeroProchainLancer(); // voir PartieMonoJoueur
	}

	/**
	 * Donne le score pour le joueur playerName
	 *
	 * @param nomDuJoueur le nom du joueur recherché
	 * @return le score pour ce joueur
	 * @throws IllegalArgumentException si nomDuJoueur ne joue pas dans cette partie
	 */
	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		if (!partiesMultiJoueurs.containsKey(nomDuJoueur)) { //containsKey() comme j'ai fait une Map
			throw new IllegalArgumentException("Joueur inconnu");
		}
		return partiesMultiJoueurs.get(nomDuJoueur).score();
	}
}
