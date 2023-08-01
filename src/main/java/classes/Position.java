package classes;


import java.util.Objects;

/**
 * Classe qui représente les @targetPosition qui seront sauvegardées
 */
public class Position {
    private final int row; // Numéro de ligne
    private final int col; // Numéro de colonne
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
