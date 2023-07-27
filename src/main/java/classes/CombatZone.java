package classes;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CombatZone {
    private int[][] grid;
    private Group zone;
    private Rectangle[][] gridRectangles; // pour facilement gérer les modifs visuelles
    private boolean tricheMode; // Variable pour indiquer si le mode triche est activé

    /**
     * Constructeur de la classe CombatZone. Initialise la zone de combat et génère les rectangles visuels de la grille.
     *
     * @param grid La grille d'entiers représentant les cases de la zone de combat.
     */
    public CombatZone(int[][] grid) {
        this.grid = grid;
        this.gridRectangles = rectangleArraySize(grid);
        this.zone = generateZone(grid);
        this.tricheMode = false; // Par défaut, le mode triche est désactivé
    }

    /**
     * Initialise un tableau 2D de rectangles pour stocker les rectangles visuels de la grille.
     *
     * @param grid La grille d'entiers représentant les cases de la zone de combat.
     * @return Un tableau 2D de rectangles initialisé avec les dimensions de la grille.
     */
    private Rectangle[][] rectangleArraySize(int[][] grid){
        int rows = grid.length;
        int cols = grid[0].length;
        Rectangle[][] gridRectangles = new Rectangle[rows][cols];
        return gridRectangles;
    }
    /**
     * Génère un groupe de rectangles pour représenter visuellement la grille de la zone de combat.
     *
     * @param grid  La grille d'entiers représentant les cases de la zone de combat.
     * @return Un groupe contenant les rectangles pour représenter la grille de la zone de combat.
     */
    public Group generateZone(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int rectangleSize = 35; // Taille des rectangles (50 pixels de longueur et de largeur choix personnel)

        Group rectangleGroup = new Group();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * rectangleSize;
                int y = row * rectangleSize;

                Rectangle rectangle = new Rectangle(x, y, rectangleSize, rectangleSize);
                rectangle.setFill(Color.LIGHTGRAY); // Couleur du rectangle :-)

                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1);

                rectangleGroup.getChildren().add(rectangle);

                gridRectangles[row][col] = rectangle;
            }
        }

        return rectangleGroup;
    }

    // MODE TRICHE
    /**
     * Active ou désactive le mode triche qui affiche explicitement le placement des bateaux sur la grille.
     *
     * @param enableTricheMode true pour activer le mode triche, false pour le désactiver.
     */
    public void setTricheMode(boolean enableTricheMode) {
        this.tricheMode = enableTricheMode;
        updateGridWithShips(); // Met à jour l'affichage de la grille pour prendre en compte le mode triche
    }

    /**
     * Met à jour l'affichage de la grille pour prendre en compte le mode triche.
     * Affiche explicitement le placement des bateaux sur la grille lorsque le mode triche est activé.
     */
    private void updateGridWithShips() {
        int rows = gridRectangles.length;
        int cols = gridRectangles[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle rectangle = gridRectangles[row][col];
                if (tricheMode && grid[row][col] > 0) {
                    rectangle.setFill(Color.GRAY); // Affiche les bateaux en gris lorsque le mode triche est activé
                } else {
                    rectangle.setFill(Color.LIGHTGRAY); // Sinon, affiche les cases normalement
                }
            }
        }
    }


    /**
     * Récupère les rectangles de la grille visuelle de la zone de combat.
     *
     * @return Un tableau 2D de rectangles représentant visuellement la grille de la zone de combat.
     */
    public Rectangle[][] getGridRectangles() {
        return gridRectangles;
    }

    /**
     * Récupère le groupe représentant la zone de combat.
     *
     * @return Le groupe contenant les éléments visuels de la zone de combat.
     */
    public Group getZone() {
        return zone;
    }
}
