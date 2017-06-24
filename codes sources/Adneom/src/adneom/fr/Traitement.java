package adneom.fr;
/**
 * Classe regroupant des méthodes utilsées dans le traitement de liste.
 * Les méthodes de cette sont toutes statiques et permettent chacune de faire un traitement 
 * spécifique sur une liste d'entiers donnée.
 * 
 * @author tantely
 *
 */
public class Traitement {
	
	/**
	 * Permet de partitionner en sous liste une liste donnée en paramètre.
	 * La taille maximale des sous listes est déterminée par un autre paramètre taille.
	 * Le resultat est un tableau à deux dimension de nombres entiers qui comporte en ligne les sous-listes.
	 * @param liste La liste à partitionner
	 * @param taille Taille maximale des sous-listes 
	 * @return Une liste de sous-listes de nombres entiers (tableau à deux dimension)
	 * @throws Exception
	 */
	public static int[][] partition(int[] liste, int taille) throws Exception{
		if(liste == null){
			throw new Exception("Veuillez renseigner une liste non nulle.");
		}else if(liste.length == 0){
			throw new Exception("La liste est vide!");
		}else if(taille <= 0){
			throw new Exception("La taille est inférieure à 0");
		}
		
		int nbSousListe = 1;
		if(liste.length > taille){
			nbSousListe = liste.length / taille;
			if((liste.length % taille) > 0) nbSousListe++;
		}
		int[][] resultat = new int[nbSousListe][taille];
		int ligne = 0;
		int colonne = 0;
		for(int i=0; i < liste.length; i++){
			if(colonne >= taille){
				colonne = 0;
				ligne ++;
			}
			resultat[ligne][colonne] = liste[i];
			colonne++;
		}
		return resultat;
	}
	
	public static void main(String[] args) {
		int[] liste = {1,2,3,4,5};
		try {
			int[][] res = partition(liste, 2);
			for(int i=0; i < res.length; i++){
				for(int j=0; j < res[i].length; j++){
					System.out.print(res[i][j]);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	

}
