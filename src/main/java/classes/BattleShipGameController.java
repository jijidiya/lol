package classes;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class BattleShipGameController {
    private int score = 0;
    private int shipsCounter = 0;
    private int cols = 10; // Valeur par défaut pour la largeur de la grille
    private int rows = 10; // Valeur par défaut pour la hauteur de la grille
    private boolean isVertical;
    private int[] shipLengths = {5, 4, 3, 3, 2};

    private  int[][] grid; // La grille du jeu
    private Rectangle[][] gridRectangles; // Les rectangles de la grille
    private Random random;
    private String[] shipsName= {"Mazono","Black Pearl", "Sunny", "Vogue Merry", "Jolly Roger", "Oxford",
                                "Hollandais volant", "Arcadia", "Psychoroïde","Queen Anne's Revenge",
                                "Santa Maria", "Club Med II", "Flying Cloud", "Bélem", "Vespucci", "Orange vert",
                                "Vendetta", "Crusoé", "Friday", "Red Force", "Wednesday"};
    // Liste de listes pour stocker les positions occupées par chaque bateau de la flotte
    private List<List<Integer>> shipsPositions = new ArrayList<>();

    //CONSTRUCTEUR
    /**
     * Constructeur de la classe BattleShipGameController.
     * Initialise la grille, le générateur de nombres aléatoires et
     * valeurs de vérités et les listes de positions pour chaque bateau.
     * Ce constructeur ne prend aucun paramètre.
     */
    public BattleShipGameController() {
        grid = new int[cols][rows]; // Crée une grille de jeu vide
        random = new Random();
        for (int shipLength : shipLengths) {
            shipsPositions.add(new ArrayList<>());
        }
    }
    /**
     * Constructeur de la classe BattleShipGameController.
     * Initialise les dimensions personnalisées de la grille,
     * les longueurs de chaque bateau qui indique aussi le nombre de bateaux qu'on aura.
     *
     * @param cols   La largeur de la grille de jeu.
     * @param rows  La hauteur de la grille de jeu.
     * @param shipLengths Un tableau d'entiers représentant les longueurs de chaque bateau.
     *                   La longueur d'un bateau est toujours comprise entre 1 et 6 cases.
     */
    public BattleShipGameController(int cols, int rows, int[] shipLengths) {
        this.cols = cols;
        this.rows = rows;
        this.shipLengths = shipLengths;
        this.grid = new int[rows][cols];
        this.random = new Random();
        for (int shipLength : shipLengths) {
            shipsPositions.add(new ArrayList<>());
        }
    }

    //PLACEMENT ALÉATOIRE DES BATEAUX
    /**
     * Place les bateaux aléatoirement sur la grille du jeu, en choisissant
     * une position et une orientation (verticale ou horizontale) aléatoires
     * pour chaque bateau, puis en vérifiant si le placement est valide.
     * Si le placement est valide, le bateau est placé sur la grille et les positions
     * qu'il occupe sont enregistrées dans la liste des positions de bateaux.
     */
    public void placeShipsRandomly() {
        for (int shipLength : shipLengths) {
            boolean isPlaced = false;
            while (!isPlaced) {
                int row = random.nextInt(rows);
                int col = random.nextInt(cols);
                boolean isVertical = random.nextBoolean();

                if (canPlaceShip(row, col, shipLength, isVertical)) {
                    placeShip(row, col, shipLength, isVertical);
                    // Enregistre les positions occupées par le bateau dans la liste correspondante
                    for (int i = 0; i < shipLength; i++) {
                        if (isVertical) {
                            // Calcul des positions verticales du bateau
                            // Enregistre l'index de la position dans la liste des positions de bateau
                            shipsPositions.get(shipsCounter).add((row + i) * cols + col);
                        } else {
                            // Calcul des positions horizontales du bateau
                            // Enregistre l'index de la position dans la liste des positions de bateau
                            shipsPositions.get(shipsCounter).add(row * cols + (col + i));
                        }
                    }
                    shipsCounter++;
                    isPlaced = true;
                }
            }
        }
    }

    /**
     * Vérifie si un bateau peut être placé à l'emplacement spécifié.
     *
     * @param startRow   Ligne de départ de la position du bateau
     * @param startCol   Colonne de départ de la position du bateau
     * @param length     Longueur du bateau
     * @param isVertical Indique si le bateau est orienté verticalement ou horizontalement
     * @return true si le bateau peut être placé à l'emplacement spécifié, false sinon
     */
    private boolean canPlaceShip(int startRow, int startCol, int length, boolean isVertical) {
        if (isVertical) {
            if (startRow + length > rows) {
                return false;
            }
            for (int row = startRow; row < startRow + length; row++) {
                if (grid[row][startCol] != 0 || isAdjacentOccupied(row, startCol)) {
                    // Vérifie si la case est déjà occupée par un autre bateau
                    return false;
                }
            }
        } else {
            if (startCol + length > cols) {
                return false;
            }
            for (int col = startCol; col < startCol + length; col++) {
                if (grid[startRow][col] != 0 || isAdjacentOccupied(startRow, col)) {
                    // Vérifie si la case est déjà occupée par un autre bateau
                    return false;
                }
            }
        }
        return true; // Le bateau peut être placé à l'emplacement spécifié
    }
    /**
     * Vérifie si les cases adjacentes d'une position spécifiée sont occupées par des bateaux.
     *
     * @param row Ligne de la position pour laquelle on vérifie les cases adjacentes
     * @param col Colonne de la position pour laquelle on vérifie les cases adjacentes
     * @return true si une case adjacente est occupée par un bateau, false sinon
     */
    private boolean isAdjacentOccupied(int row, int col) {
        // Vérifie si la liste des positions de bateaux n'est pas vide
        if (!shipsPositions.isEmpty()) {
            int dRow;
            int dCol;
            // Parcourt chaque bateau dans la liste des positions de bateaux
            for (List<Integer> shipPositions : shipsPositions) {
                // Parcourt chaque position occupée par le bateau actuel
                for (int position : shipPositions) {
                    // Calcule les lignes et colonnes des autres positions de bateaux
                    int otherShipsRow = position / cols;
                    int otherShipsCol = position % cols;

                    // Calcule les différences de distance entre les lignes et les colonnes
                    dRow = Math.abs(row - otherShipsRow);
                    dCol = Math.abs(col - otherShipsCol);

                    // Si la distance entre les positions est de 1 case ou moins dans chaque direction,
                    // alors la case est considérée comme adjacente et occupée
                    if (dRow <= 1 && dCol <= 1) {
                        return true; // Case adjacente occupée par un bateau
                    }
                }
            }
        }

        return false; // Aucune case adjacente n'est occupée par un bateau
    }
    /**
     * Place un bateau sur la grille du jeu.
     *
     * @param startRow   Ligne de départ de la position du bateau
     * @param startCol   Colonne de départ de la position du bateau
     * @param length     Longueur du bateau
     * @param isVertical Indique si le bateau est orienté verticalement ou horizontalement
     */
    public void placeShip(int startRow, int startCol, int length, boolean isVertical) {
        if (isVertical) {
            for (int row = startRow; row < startRow + length; row++) {
                // Marque la case de la grille avec la longueur du bateau
                grid[row][startCol] = length;
            }
        } else {
            for (int col = startCol; col < startCol + length; col++) {
                // Marque la case de la grille avec la longueur du bateau
                grid[startRow][col] = length;
            }
        }
    }







    // VISER UNE CIBLE DE LA GRILLE
    /**
     * Méthode de gestion pour le clic sur le bouton de tir.
     *
     * @param targetPosition La position cible du tir au format lettre-numéro.
     * @return Le résultat du tir, qui peut être l'une des valeurs suivantes :
     *           - "touché" si le tir a touché un bateau.
     *           - "Vous avez déjà tiré ici !" si la position cible a déjà été tirée auparavant.
     *           - "Dans l'eau, Plouf!!" si le tir est dans l'eau (aucun bateau n'a été touché).
     *           - "Position cible en dehors de la grille !" si la position cible est en dehors de la grille de jeu.
     *           - "Position cible invalide !" si la position cible n'est pas au bon format.
     */
    public String fireAtTargetPosition(String targetPosition) {
        String result;
        try {
            // Vérifier si la position cible est valide (lettre suivie d'un chiffre)
            if (isValidTargetPosition(targetPosition)) {
                // Incrémenter le score du joueur à chaque tir effectué
                score++;

                char rowLetter = targetPosition.charAt(0);
                int row = rowLetter - 'A';
                int col = Integer.parseInt(targetPosition.substring(1));

                // Vérifier si la position cible est dans la grille
                if (isInGrid(row, col)) {
                    if (grid[row][col] > 0) {
                        // Le tir a touché un bateau
                        // Marquer le tir comme "touché"
                        grid[row][col] = -1;
                        // Mettre à jour l'apparence du bouton avec une couleur spécifique
                        updateGridCell(row, col, Color.RED);
                        result = "touché";
                    } else if (grid[row][col] == -1) {
                        result ="Vous avez déjà tiré ici !";
                    } else {
                        // Le tir est dans l'eau (aucun bateau n'a été touché)
                        grid[row][col] = -1; // Marquer le tir comme "dans l'eau"
                        updateGridCell(row, col, Color.BLUE);
                        result = "Plouf!!";
                    }
                    return result;
                } else {
                    // La position cible est en dehors de la grille
                    return "Position cible en dehors de la grille !";
                }
            } else {
                // La position cible n'est pas au bon format
                return "Position cible invalide !";
            }
        } catch (NumberFormatException e) {
            // Si une exception NumberFormatException est levée lors de la conversion de la position cible en entier
            // cela signifie que la position cible n'est pas au bon format.
            return "Position cible invalide !";
        }
    }
    /**
     * Vérifie si la position cible est au format attendu.
     *
     * @param targetPosition La chaîne représentant la position cible, au format lettre suivie d'un chiffre.
     * @return true si la position cible est au format attendu, sinon false.
     */
    public boolean isValidTargetPosition(String targetPosition) {
        // La méthode matches() vérifie si la chaîne correspond au motif spécifié.
        return targetPosition.matches("[A-Z][0-9]+");
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
     * Met à jour l'apparence du bouton de la grille avec une couleur spécifique.
     */
    private void updateGridCell(int row, int col, Color color) {
        Rectangle rectangle = gridRectangles[row][col];
        rectangle.setFill(color);
    }
    /**
     * Vérifie si tous les bateaux ont été coulés.
     */
    public boolean checkAllShipsSunk() {
        for (int[] ints : grid) {
            for (int anInt : ints) {
                if (anInt > 0) {
                    // Il reste au moins un bateau non coulé
                    return false;
                }
            }
        }
        // Tous les bateaux ont été coulés
        return true;
    }


    //CALCUL DE LA DISTANCE DE MANHATTAN
    /**
     * Calcule les distances de Manhattan entre une position cible et les positions de chaque bateau de la flotte et
     * dans l'éventualité qu'un bateau soit deja coulé, elle renvoie la valeur max des int qui sera ensuite gérée pour
     * signaler que le bateau est coulé et sera ignoré dans getDistanceFromShips().
     *
     * @param targetPosition La position cible sous forme de code lettre-numéro.
     * @return Une liste d'entiers contenant les distances de Manhattan entre la position cible et les bateaux.
     * @throws IllegalArgumentException Si la position cible est invalide.
     */
    public List<Integer> manhattanDistance(String targetPosition) {
        if (!isValidTargetPosition(targetPosition)) {
            throw new IllegalArgumentException("Position cible invalide : " + targetPosition);
        }

        int targetRow = targetPosition.charAt(0) - 'A';
        int targetCol = Integer.parseInt(targetPosition.substring(1));

        //Si la cible fait mouche
        if(grid[targetRow][targetCol] > 0){
            return null;
        }

        List<Integer> distances = new ArrayList<>();
        for (List<Integer> shipPositions : shipsPositions) {
            // Initialise la distance minimale avec une valeur maximale possible
            int minDistance = Integer.MAX_VALUE;
            for (int position : shipPositions) {
                int row = position / cols;
                int col = position % cols;
                if(grid[row][col] > 0){
                    int distance = Math.abs(targetRow - row) + Math.abs(targetCol - col);
                    if (distance < minDistance) {
                        // Met à jour la distance minimale si une distance plus petite est trouvée
                        minDistance = distance;
                    }
                }
            }
            distances.add(minDistance); // Ajoute la distance minimale du bateau actuel à la liste
        }

        return distances;
    }

    /**
     * Calcule et renvoie les distances de Manhattan entre une position cible et les bateaux de la flotte.
     * Affiche la distance de chaque bateau par rapport à la position cible, en excluant les distances infinies
     * (lorsque la position cible est en dehors de la portée de tous les bateaux) et lorsque le bateau est deja
     * entièrement coulé.
     *
     * @param targetPosition La position cible sous forme de code lettre-numéro.
     * @return Une chaîne de caractères contenant les distances de chaque bateau par rapport à la position cible
     *         ou un message d'erreur si la position cible est invalide.
     */
    public String getDistanceFromShips(String targetPosition) {
        int maxInt = Integer.MAX_VALUE;
        StringBuilder result = new StringBuilder();
        try {
            List<Integer> distances = manhattanDistance(targetPosition);
            if (distances == null){
                result.append("vous avez fait mouche \nLa distance de Manhattan : 0");
                return result.toString();
            }else{
                int shipIndex = 0;
                for (Integer distance : distances) {
                    if (distance != maxInt){
                        result.append("Distance de ").append(distance).append(" cases du " +
                                "").append(shipsName[shipIndex]).append("\n");
                    }
                    shipIndex++;
                }
            }
        } catch (IllegalArgumentException e) {
            result.append(e.getMessage());
        }
        return result.toString();
    }


    //REDÉMARRER LE JEU
    /**
     * Redémarre le jeu en remettant à zéro les paramètres nécessaires.
     */
    public void restartGame() {
        grid = new int[rows][cols];
        shipsCounter =0;
        score =0;
        for(List<Integer> shipsPosition : shipsPositions){
            shipsPosition.clear();
        }
    }



    //CHARGEMENT D'UNE PARTIE À PARTIR D'UN FICHIER TXT
    /**
     * Charge une grille et un positionnement initial de la flotte à partir d'un fichier texte.
     * Le fichier doit être au format spécifié dans les consignes du cahier des charges.
     *
     * @param filePath Le chemin vers le fichier texte à charger.
     * @return Un tableau 2D représentant la grille chargée depuis le fichier.
     * @throws IOException En cas d'erreur lors de la lecture du fichier.
     * @throws IllegalArgumentException Si le format du fichier n'est pas valide.
     */
    public void  loadGameFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String line;
            int colCount = -1;

            // Compter le nombre de lignes et déterminer la longueur attendue pour les colonnes
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                if (colCount == -1) {
                    colCount = line.length(); // Première ligne, déterminer la longueur attendue
                } else {
                    // Vérifier si la longueur de la ligne est différente de la longueur attendue
                    if (line.length() != colCount) {
                        throw new IllegalArgumentException("Le format du fichier n'est pas valide. " +
                                "Toutes les lignes doivent avoir la même longueur.");
                    }
                }
            }

            int rowCount = lines.size();
            if (rowCount == 0) {
                throw new IllegalArgumentException("Le fichier est vide. Aucune grille n'a été trouvée.");
            }

            // Initialiser le tableau une fois que les dimensions sont connues
            grid = new int[rowCount][colCount];
            rows = rowCount;
            cols = colCount;

            int cpt = 0;
            // Remplir le tableau avec les valeurs de la grille à partir du fichier
            for (int row = 0; row < rows; row++) {
                line = lines.get(row);
                for (int col = 0; col < cols; col++) {
                    char cell = line.charAt(col);
                    if (cell == '.') {
                        grid[row][col] = 0; // Case vide
                    } else if (cell == 'X') {
                        grid[row][col] = 1;
                    } else {
                        throw new IllegalArgumentException("Le format du fichier n'est pas valide. " +
                                "Caractère inconnu : " + cell);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }
    public void initializeGrid(){
        for(int row = 0; row< rows -1; row++){
            for(int col = 0; col< cols -1; col++){
                if(grid[row][col] >0){
                    saveShipsPosition(row, col);
                }
            }
        }
    }
    public void saveShipsPosition(int row, int col){
        int shipLength = calculateShipLength(row, col);
        if(shipLength>0){
            if (shipsPositions.isEmpty()) {
                for (int i = 0; i < shipLength; i++) {
                    if (isVertical) {
                        // Calcul des positions verticales du bateau
                        shipsPositions.get(shipsCounter).add((row + i) * cols + col);
                    } else {
                        // Calcul des positions horizontales du bateau
                        shipsPositions.get(shipsCounter).add(row * cols + (col + i));
                    }
                }
                shipsCounter++;
            }else{
                if(shipsCounter == shipsPositions.size()){
                    // pour que shipsPositions s'adaptent au nombre porgressivement
                    // au nomre de bateau
                    shipsPositions.add(new ArrayList<>());
                }
                if (canSavePosition(row, col, shipLength)){
                    for (int i = 0; i < shipLength; i++) {
                        if (isVertical) {
                            // Calcul des positions verticales du bateau
                            shipsPositions.get(shipsCounter).add((row + i) * cols + col);
                        } else {
                            // Calcul des positions horizontales du bateau
                            shipsPositions.get(shipsCounter).add(row * cols + (col + i));
                        }
                    }
                    shipsCounter++;
                }
            }
        }

    }

    /**
     * Calcule la longueur du bateau à partir de la position donnée dans la grille.
     * Cette méthode prend en compte la direction du bateau et compte le nombre de cases occupées consécutivement.
     *
     * @param row  L'index de la ligne de départ du bateau.
     * @param col  L'index de la colonne de départ du bateau.
     * @return La longueur du bateau à partir de la position donnée dans la grille.
     */
    private int calculateShipLength( int row, int col) {
        // Vérifier si la case de départ est déjà occupée par un bateau
        if (grid[row][col] < 1) {
            return 0; // Pas de bateau à cette position
        }
        // Vérifier si le bateau est positionné horizontalement ou verticalement
        // Compter la longueur du bateau dans la direction correspondante
        int length = 0;
        isVertical = false;

        if (row < rows -1 && grid[row + 1][col] == 1) {
            isVertical = true;
        } else if (col < cols -1 && grid[row][col + 1] == 1) {
            // Le bateau est positionné horizontalement
        } else {
            // Le bateau occupe une seule case, donc sa longueur est de 1.
            return 1;
        }


        if (isVertical) {
            while (row < grid.length && grid[row][col] == 1) {
                length++;
                row++;
            }
        } else {
            while (col < grid[0].length && grid[row][col] == 1) {
                length++;
                col++;
            }
        }
        return length;
    }
    private boolean canSavePosition(int row, int col, int shipLength){
        for (List<Integer> shipPositions : shipsPositions) {
            for(int i = 0; i<shipLength; i++){
                for (int position : shipPositions) {

                    int otherShipsRow = position / cols;
                    int otherShipsCol = position % cols;

                    if(isVertical){
                        if (((row+i) - otherShipsRow) ==0 && (col - otherShipsCol) == 0){
                            return false;
                        }
                    }else {
                        if((row - otherShipsRow) == 0 && (((col+i) - otherShipsCol) == 0)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }



    // CAREER POUR LA GESTION DES TESTS
    /**
     * Récupère la valeur de la case spécifiée dans la grille du jeu.
     *
     * @param row La ligne de la case.
     * @param col La colonne de la case.
     * @return La valeur de la case dans la grille.
     * @throws IndexOutOfBoundsException Si la position spécifiée est en dehors des limites de la grille.
     */
    public int getGridValue(int row, int col) throws IndexOutOfBoundsException {
        if (!isInGrid(row, col)) {
            throw new IndexOutOfBoundsException("Position en dehors des limites de la grille !");
        }

        return grid[row][col];
    }


    //ACCESSEURS
    /**
     * Renvoie le score du jeu.
     *
     * @return le score
    */
    public int getScore(){
        return score;
    }

    /**
     * Définit les rectangles de la grille du jeu.
     *
     * @param rectangles Un tableau 2D de rectangles représentant visuellement la grille du jeu.
     */
    public void setGridRectangles(Rectangle[][] rectangles) {
        gridRectangles = rectangles;
    }

    /**
     * Renvoie la grille du jeu sous sa forme de tableau à 2 dimensions d'entiers.
     *
     * @return Un tableau 2D d'entiers représentant la grille du jeu
     */
    public int[][] getGrid() {
        return grid;
    }

}
