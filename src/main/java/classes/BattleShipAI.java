package classes;

import java.util.*;

public class BattleShipAI {
    private int rows;
    private int cols;
    private int[][] enemyGrid;
    private boolean[][] targetGrid;
    private Random random;
    private List<Position> previousShots;

    private Map<Position, String> shotResults;
    /**
     * Construit un objet BattleShipAI pour représenter l'intelligence artificielle d'un joueur dans le jeu de Bataille Navale.
     *
     * @param enemyGrid La grille de l'adversaire représentée par une matrice d'entiers où chaque élément indique la présence d'un bateau.
     *                  La grille est représentée par une matrice de dimensions [rows][cols].
     *                  Les valeurs possibles pour chaque élément de la matrice sont :
     *                  - valeurs supérieures à 0 : bateau présent sur cette case
     *                  - 0 : case vide non ciblée auparavant
     *                  - -1 : tir effectué sur cette case, raté (tir dans l'eau) ou touché
     */
    public BattleShipAI(int[][] enemyGrid) {
        this.enemyGrid = enemyGrid;
        rows = enemyGrid.length;
        cols = enemyGrid[0].length;
        targetGrid = new boolean[rows][cols];
        random = new Random();
        previousShots = new ArrayList<>();
    }

    /**
     * Choix de la cible pour le prochain tir.
     *
     * @return La position cible sous forme de code lettre-numéro.
     */
    public String chooseTarget() {
        Position targetPosition;
        if (previousShots.isEmpty()) {
            targetPosition = convertPosition(chooseRandomTarget());
        } else {
            targetPosition = exploreAdjacentTarget();
            if (targetPosition == null) {
                targetPosition = convertPosition(chooseRandomTarget());
            }
        }

        previousShots.add(targetPosition);
        targetGrid[targetPosition.getRow()][targetPosition.getCol()] = true;

        return convertPositionToString(targetPosition);
    }

    /**
     * Choix d'une nouvelle cible aléatoire parmi les positions cibles disponibles dans la grille.
     *
     * @return La position cible sous forme de code lettre-numéro.
     */
    private String chooseRandomTarget() {
        List<Position> availableTargets = getAvailableTargets();

        // Choix aléatoire d'une position cible parmi celles disponibles
        int randomIndex = random.nextInt(availableTargets.size());
        Position targetPosition = availableTargets.get(randomIndex);

        // Marquer la position cible comme ciblée dans la grille
        targetGrid[targetPosition.getRow()][targetPosition.getCol()] = true;

        // Convertir la position cible en format lettre-numéro
        return convertPositionToString(targetPosition);
    }

    /**
     * Explore les positions adjacentes à la dernière position de tir pour trouver une nouvelle cible.
     *
     * @return Une nouvelle position cible adjacente non encore ciblée, ou null si aucune n'est disponible.
     */
    private Position exploreAdjacentTarget() {
        // Récupérer la dernière position de tir enregistrée
        Position lastShot = previousShots.get(previousShots.size() - 1);
        String lastShotResult = shotResults.get(lastShot);
        // On vérifie si le résultat du dernier tir a été positif.
        // Ou si lastShot n'est pas null.
        if (lastShotResult != null && lastShotResult.equalsIgnoreCase("touché")){
            // Obtenir les coordonnées de la dernière position de tir.
            int row = lastShot.getRow();
            int col = lastShot.getCol();

            // Créer une liste pour stocker les positions adjacentes
            List<Position> adjacentPositions = new ArrayList<>();

            // Ajouter les quatre positions adjacentes (en haut, en bas, à gauche et à droite) à la liste
            adjacentPositions.add(new Position(row - 1, col)); // Haut
            adjacentPositions.add(new Position(row + 1, col)); // Bas
            adjacentPositions.add(new Position(row, col - 1)); // Gauche
            adjacentPositions.add(new Position(row, col + 1)); // Droite

            Collections.shuffle(adjacentPositions);

            // Parcourir la liste des positions adjacentes
            for (Position position : adjacentPositions) {
                // Vérifier si la position adjacente est valide (à l'intérieur des limites de la grille)
                // et si elle n'a pas déjà été ciblée
                if (isValidPosition(position) && !targetGrid[position.getRow()][position.getCol()]) {
                    // Retourner la position adjacente non encore ciblée
                    return position;
                }
            }
        }

        // Sinon on renvoie null
        return null;
    }

    /**
     * Obtient la liste des positions cibles disponibles (non encore ciblées) dans la grille.
     *
     * @return Une liste contenant les positions cibles disponibles.
     */
    private List<Position> getAvailableTargets() {
        List<Position> availableTargets = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!targetGrid[row][col]) {
                    availableTargets.add(new Position(row, col));
                }
            }
        }

        return availableTargets;
    }

    /**
     * Vérifie si une position donnée est valide dans une grille de dimensions spécifiées.
     *
     * @param position La position à vérifier.
     * @return true si la position est valide (à l'intérieur des limites de la grille), sinon false.
     */
    private boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < rows
                && position.getCol() >= 0 && position.getCol() < cols;
    }

    //MEMORISATION DES RÉSULTATS DES TIRS
    /**
     * Mémorise les résultats du tir pour une position donnée.
     *
     * @param targetPosition La position cible du tir sous forme de code lettre-numéro (ex: "A1", "D5", etc.).
     * @param result         Le résultat du tir ("touché" ou "raté").
     */
    public void rememberShotResult(String targetPosition, String result) {
        Position shotPosition = convertPosition(targetPosition);
        if ("touché".equalsIgnoreCase(result)) {
            shotResults.put(shotPosition, "touché");
        } else {
            shotResults.put(shotPosition, "raté");
        }
    }



    public String optimizeShots() {
        // TODO: Implémenter l'optimisation des coups
        // Stratégie de tir optimisée pour maximiser les chances de couler les bateaux ennemis.
        // N'oublie pas de marquer la case ciblée dans targetGrid avant de retourner la position cible.
        return null;
    }



    //METHODE DE CONVERSION
    /**
     * Convertit une position sous forme de code lettre-numéro en objet Position.
     *
     * @param targetPosition La position cible
     * @return L'objet Position correspondant à la position cible.
     */
    private Position convertPosition(String targetPosition) {
        char rowLetter = targetPosition.charAt(0);
        int row = rowLetter - 'A';
        int col = Integer.parseInt(targetPosition.substring(1)) - 1;
        return new Position(row, col);
    }

    /**
     * Convertit une position en code lettre-numéro.
     *
     * @param position La position à convertir.
     * @return Le code lettre-numéro correspondant à la position.
     */
    private String convertPositionToString(Position position) {
        char rowLetter = (char) (position.getRow() + 'A');
        int col = position.getCol() + 1;
        return rowLetter + Integer.toString(col);
    }
}