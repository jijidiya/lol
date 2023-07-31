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
    private int gridWidth = 10; // Valeur par défaut pour la largeur de la grille
    private int gridHeight = 10; // Valeur par défaut pour la hauteur de la grille
    private int numShips;
    private int[] shipLengths = {5, 4, 3, 3, 2};

    private  int[][] grid; // La grille du jeu
    private Rectangle[][] gridRectangles; // Les rectangles de la grille
    private Random random;
    private String[] shipsName= {"Mazono","Black Pearl", "Sunny", "Vogue Merry", "Jolly Roger", "Oxford"};
    // Liste de listes pour stocker les positions occupées par chaque bateau de la flotte
    private List<List<Integer>> shipsPositions = new ArrayList<>();

    //CONSTRUCTEUR
    /**
     * Construit un objet BattleShipGameController et initialise la grille, le générateur de nombres
     * aléatoires et valeurs de vérités et les listes de positions pour chaque bateau.
     */
    public BattleShipGameController() {
        grid = new int[gridWidth][gridHeight]; // Crée une grille de jeu vide
        random = new Random(); // Initialise le générateur de nombres aléatoires et valeurs de vérités
        for (int shipLength : shipLengths) {
            shipsPositions.add(new ArrayList<>());
        }
    }
    /**
     * Constructeur de la classe BattleShipGameController.
     * Initialise le jeu de bataille navale avec les dimensions de la grille personnalisées,
     * le nombre de bateaux et les longueurs de chaque bateau.
     *
     * @param gridWidth   La largeur de la grille de jeu.
     * @param gridHeight  La hauteur de la grille de jeu.
     * @param shipLengths Un tableau d'entiers représentant les longueurs de chaque bateau.
     *                   La longueur d'un bateau est toujours comprise entre 1 et 6 cases.
     */
    public BattleShipGameController(int gridWidth, int gridHeight, int[] shipLengths) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.numShips = 6;
        this.shipLengths = shipLengths;
        this.grid = new int[gridWidth][gridHeight];
        this.random = new Random();
        for (int shipLength : shipLengths) {
            shipsPositions.add(new ArrayList<>());
        }
    }
    /**
     * Constructeur de la classe BattleShipGameController.
     * Initialise le jeu de bataille navale avec les dimensions de la grille personnalisées,
     * le nombre de bateaux et les longueurs de chaque bateau.
     *
     * @param gridWidth   La largeur de la grille de jeu.
     * @param gridHeight  La hauteur de la grille de jeu.
     * @param numShips    Le nombre total de bateaux dans la flotte.
     * @param shipLengths Un tableau d'entiers représentant les longueurs de chaque bateau.
     *                   La longueur d'un bateau est toujours comprise entre 1 et 6 cases.
     */
    public BattleShipGameController(int gridWidth, int gridHeight, int numShips, int[] shipLengths) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.numShips = numShips;
        this.shipLengths = shipLengths;
        this.grid = new int[gridWidth][gridHeight];
        this.random = new Random();
        for (int shipLength : shipLengths) {
            shipsPositions.add(new ArrayList<>());
        }
    }

    //PLACEMENT ALÉATOIRE DES BATEAUX
    /**
     * Place les bateaux aléatoirement sur la grille du jeu.
     */
    public void placeShipsRandomly() {
        int cpt = 0;
        for (int shipLength : shipLengths) {
            boolean isPlaced = false;
            while (!isPlaced) {
                int row = random.nextInt(gridHeight);
                int col = random.nextInt(gridWidth);
                boolean isVertical = random.nextBoolean();
                if (canPlaceShip(row, col, shipLength, isVertical)) {
                    placeShip(row, col, shipLength, isVertical);
                    // Enregistre les positions occupées par le bateau dans la liste correspondante
                    for (int i = 0; i < shipLength; i++) {
                        if (isVertical) {
                            shipsPositions.get(cpt).add((row + i) * gridWidth + col);
                        } else {
                            shipsPositions.get(cpt).add(row * gridWidth + (col + i));
                        }
                    }
                    cpt++;
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
            if (startRow + length > gridHeight) {
                return false;
            }
            for (int row = startRow; row < startRow + length; row++) {
                if (grid[startCol][row] != 0) { // Vérifie si la case est déjà occupée par un autre bateau
                    return false;
                }
            }
        } else {
            if (startCol + length > gridWidth) {
                return false;
            }
            for (int col = startCol; col < startCol + length; col++) {
                if (grid[col][startRow] != 0) { // Vérifie si la case est déjà occupée par un autre bateau
                    return false;
                }
            }
        }
        return true; // Le bateau peut être placé à l'emplacement spécifié
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
                grid[startCol][row] = length; // Marque la case de la grille avec la longueur du bateau
            }
        } else {
            for (int col = startCol; col < startCol + length; col++) {
                grid[col][startRow] = length; // Marque la case de la grille avec la longueur du bateau
            }
        }
    }



    // VISER UNE CIBLE DE LA GRILLE
    /**
     * Méthode de gestion pour le clic sur le bouton de tir.
     *
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
                char rowLetter = targetPosition.charAt(0);
                int row = rowLetter - 'A';
                int col = Integer.parseInt(targetPosition.substring(1));

                // Vérifier si la position cible est dans la grille
                if (isInGrid(row, col)) {
                    if (grid[row][col] > 0) {
                        // Le tir a touché un bateau
                        grid[row][col] = -1; // Marquer le tir comme "touché"
                        updateGridButton(row, col, Color.RED); // Mettre à jour l'apparence du bouton avec une couleur spécifique
                        System.out.println("Bateau touché");
                        result = "touché";
                    } else if (grid[row][col] == -1) {
                        System.out.println("Vous avez déjà tiré ici !");
                        result ="Vous avez déjà tiré ici !";
                    } else {
                        // Le tir est dans l'eau (aucun bateau n'a été touché)
                        grid[row][col] = -1; // Marquer le tir comme "dans l'eau"
                        updateGridButton(row, col, Color.BLUE);
                        System.out.println("Dans l'eau, Plouf!!");
                        result = "Dans l'eau, Plouf!!";
                    }

                    // Vérifier si tous les bateaux ont été coulés
                    if (checkAllShipsSunk()) {
                        System.out.println("Tous les bateaux ont été coulés. Partie terminée !");
                        // TODO : Gérer la fin du jeu ici
                    }
                    return result;
                } else {
                    System.out.println("Position cible en dehors de la grille !");
                    return "Position cible en dehors de la grille !";
                }
            } else {
                System.out.println("Position cible invalide !");
                return "Position cible invalide !";
            }
        } catch (NumberFormatException e) {
            System.out.println("Position cible invalide !");
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
        return row >= 0 && row < gridHeight && col >= 0 && col < gridWidth;
    }
    /**
     * Met à jour l'apparence du bouton de la grille avec une couleur spécifique.
     */
    private void updateGridButton(int row, int col, Color color) {
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
     * signaler que le bateau est coulé.
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

        List<Integer> distances = new ArrayList<>();
        for (List<Integer> shipPositions : shipsPositions) {
            int minDistance = Integer.MAX_VALUE; // Initialise la distance minimale avec une valeur maximale possible
            for (int position : shipPositions) {
                int row = position / gridWidth;
                int col = position % gridWidth;
                if(grid[row][col] > 0){
                    int distance = Math.abs(targetRow - row) + Math.abs(targetCol - col);
                    if (distance < minDistance) {
                        minDistance = distance; // Met à jour la distance minimale si une distance plus petite est trouvée
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
            int shipIndex = 0;
            for (Integer distance : distances) {
                if (distance != maxInt){
                    result.append("Distance de ").append(distance).append(" cases du ").append(shipsName[shipIndex]).append("\n");
                }
                shipIndex++;
            }
        } catch (IllegalArgumentException e) {
            result.append(e.getMessage());
        }
        return result.toString();
    }



    //CHARGEMENT D'UNE PARTIE À PARTIR D'UN FICHIER TXT
    /**
     * Charge une grille et un positionnement initial de la flotte à partir d'un fichier texte.
     * Le fichier doit être au format spécifié dans les consignes du cahier des charges.
     *
     * @param filePath Le chemin vers le fichier texte à charger.
     * @throws IOException En cas d'erreur lors de la lecture du fichier.
     */
    public int[][] loadGameFromFile(String filePath) throws  IOException{
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
                        throw new IllegalArgumentException("Le format du fichier n'est pas valide. Toutes les lignes doivent avoir la même longueur.");
                    }
                }
            }

            int rowCount = lines.size();

            // Initialiser le tableau une fois que les dimensions sont connues
            int[][] grid = new int[rowCount][colCount];

            for (int row = 0; row < rowCount; row++) {
                line = lines.get(row);
                for (int col = 0; col < colCount; col++) {
                    char cell = line.charAt(col);
                    if (cell == '.') {
                        grid[row][col] = 0; // Case vide
                    } else if (cell == 'X') {
                        grid[row][col] = 1; // Case occupée par un bateau
                    } else {
                        throw new IllegalArgumentException("Le format du fichier n'est pas valide. Caractère inconnu : " + cell);
                    }
                }
            }

            if (rowCount == 0) {
                throw new IllegalArgumentException("Le fichier est vide. Aucune grille n'a été trouvée.");
            }
            return grid;

            // Utilisez le tableau 'grid' comme vous en avez besoin à partir de maintenant
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return null;
        }
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
