package classes;


/**
 * Classe qui représentant les @targetPosition qui seront sauvegardées
 */
public class Position {
    private int row; // Numéro de ligne
    private int col; // Numéro de colonne
    private Position position;

    /**
     * Constructeur de la classe Position.
     *
     * @param row Le numéro de ligne de la position.
     * @param col Le numéro de colonne de la position.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
        this.position = new Position(row, col);
    }

    /**
     * Récupère le numéro de ligne de la position.
     *
     * @return Le numéro de ligne.
     */
    public int getRow() {
        return row;
    }

    /**
     * Récupère le numéro de colonne de la position.
     *
     * @return Le numéro de colonne.
     */
    public int getCol() {
        return col;
    }


    public Position getPosition() {
        return position;
    }
}