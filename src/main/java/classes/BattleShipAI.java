package classes;

import java.util.*;


public class BattleShipAI {
    private BattleShipGameController gameController;
    private int rows;
    private int cols;

    private boolean[][] targetGrid;
    private Random random;
    private List<Position> previousShots;
    private List<Position> potentialTargets;

    private Map<Position, String> shotResults;
    /**
     * Construit un objet BattleShipAI pour représenter l'intelligence artificielle d'un joueur dans le jeu de Bataille Navale.
     *
     * @param gameController Le contrôleur du jeu de Bataille Navale contenant les informations sur la grille,
     */
    public BattleShipAI(BattleShipGameController gameController) {
        this.gameController = gameController;
        rows = gameController.getGrid().length;
        cols = gameController.getGrid()[0].length;
        targetGrid = new boolean[rows][cols];
        random = new Random();
        previousShots = new ArrayList<>();
        shotResults = new HashMap<>();
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
     * Choisi une position cible aléatoire parmi les positions disponibles.
     * Si la liste des positions potentielles (potentialTargets) est vide, la méthode sélectionne une
     * position cible parmi toutes les positions non encore ciblées dans la grille (targetGrid).
     * Sinon, elle choisit une position cible parmi les positions potentielles.
     * La position cible choisie est marquée comme ciblée dans la grille (targetGrid) et retournée sous la forme
     * d'une chaîne de caractères au format lettre-numéro (ex: "A3").
     *
     * @return La position cible aléatoire au format lettre-numéro.
     */
    private String chooseRandomTarget() {
        Position targetPosition;
        if(potentialTargets == null ||potentialTargets.isEmpty()){
            // Si la liste des positions potentielles est vide ou nulle,
            // On obtient toutes les positions disponibles.
            List<Position> availableTargets = getAvailableTargets();

            // Choix aléatoire d'une position cible parmi celles disponibles.
            int randomIndex = random.nextInt(availableTargets.size());
            targetPosition = availableTargets.get(randomIndex);
        }else{
            // Si des positions potentielles sont disponibles.
            int randomIndex = random.nextInt(potentialTargets.size());
            targetPosition = potentialTargets.get(randomIndex);
        }
        // Convertir la position cible en format lettre-numéro
        return convertPositionToString(targetPosition);
    }

    /**
     * Explore les positions adjacentes à la dernière position de tir réussie pour trouver une nouvelle cible.
     * La méthode recherche les positions adjacentes (en haut, en bas, à gauche et à droite) à la dernière position de
     * tir qui a été marquée comme "touchée" dans les résultats des tirs (shotResults). Elle retourne une nouvelle
     * position cible adjacente non encore ciblée dans la grille (targetGrid).
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
            //uniquement si la distance de manhattan de la cible la plus proche est de 1.
            if (findMinDistance(gameController.manhattanDistance(convertPositionToString(lastShot)))==1){
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
     * Vérifie si une position donnée est valide dans la grille.
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
     * @param targetPosition La position cible du tir.
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

    /**
     * Optimise les coups en générant une liste de positions potentielles à cibler en fonction de la position cible
     * et de la plus petite distance de Manhattan des bateaux par rapport à cette position.
     *
     * @param targetStringPosition La position cible (ex: "A3").
     * @param manhattanDistance    La liste des distances de Manhattan des bateaux par rapport à la position cible.
     */
    public void optimizeShots(String targetStringPosition, List<Integer> manhattanDistance) {
        potentialTargets = new ArrayList<>();
        int minDistance = findMinDistance(manhattanDistance);
        Position targetPosition = convertPosition(targetStringPosition);

        //On vérifie que le jeu n'est pas fini dans un souci de performance
        if (minDistance != 0) {
            // Récupère les coordonnées de la position cible
            int targetCol = targetPosition.getCol();
            int targetRow = targetPosition.getRow();

            // Parcourir les lignes dans une plage qui correspond à la distance de Manhattan
            for (int row = targetRow - minDistance; row <= targetRow + minDistance; row++) {
                // Calcule la distance en lignes (dRow) entre la ligne actuelle et la ligne de la position cible
                int dRow = Math.abs(row - targetRow);

                // Calcule la distance en colonnes (dCol) pour que la distance de Manhattan soit égale à minDistance
                // Donc, pour obtenir la distance donnée (minDistance), nous devons réduire la distance verticale
                // (dRow) de cette valeur, car la distance horizontale (dCol) sera égale à minDistance - dRow.
                int dCol = minDistance - dRow;

                // ColOffset contient les deux valeurs possibles pour le décalage horizontal : -dCol et dCol.
                // Cela nous permet de parcourir les deux positions adjacentes à gauche et à droite de la position cible,
                // sans avoir à répéter le code pour chaque côté.
                for (int colOffset : new int[]{-dCol, dCol}) {
                    int col = targetCol + colOffset;

                    // Vérifie si la position adjacente est valide
                    // et si elle n'a pas déjà été ciblée
                    if (isInGrid(row, col) && !targetGrid[row][col]) {
                        potentialTargets.add(new Position(row, col));
                    }
                }
            }
        }
    }

    /**
     * Vérifie si la position est dans la grille de jeu.
     *
     * @param row Le numéro de ligne de la position.
     * @param col Le numéro de colonne de la position.
     * @return true si la position est dans la grille, sinon false.
     */
    private boolean isInGrid(int row, int col) {
        // Vérifier si la ligne et la colonne sont dans la plage valide de la grille.
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Compare les différentes distances de manhattan pour trouver la plus petite.
     *
     * @param manhattanDistances les distances de manhattan séparant les bateaux de la position cible.
     * @return                   la distance de manhattan du plus proche bateau ou 0 si le jeu est fini.
     */
    public int findMinDistance(List<Integer> manhattanDistances){
        int minDistance = Integer.MAX_VALUE;
        for (int distance: manhattanDistances) {
            if(distance < minDistance){
                minDistance = distance;
            }
        }
        // Cette condition sera respectée uniquement si toutes les cibles sont touchées,
        // dans le souci d'éviter un surplus de calcul inutile, nous renvoyons 0.
        if (minDistance == Integer.MAX_VALUE){
                return 0; //on dirait pas du C :-);
        }
        return minDistance;
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
        int col = Integer.parseInt(targetPosition.substring(1));
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
        int col = position.getCol();
        return rowLetter + Integer.toString(col);
    }
}