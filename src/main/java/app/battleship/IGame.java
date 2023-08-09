package app.battleship;


import java.io.IOException;

/**
 * Interface définissant les méthodes communes pour les classes de jeu ou le jeu sera joué.
 * Les classes implémentant cette interface doivent fournir des implémentations pour ces méthodes.
 */
public interface IGame {


    /**
     * Redémarre le jeu de bataille navale.
     * Réinitialise le plateau de jeu et les paramètres de la partie.
     * Cette méthode peut être appelée lorsque le joueur souhaite recommencer une partie.
     */
    void restartGame() throws IOException;

    /**
     * Active ou désactive le mode triche du jeu de bataille navale.
     * Le mode triche peut permettre au joueur de voir les positions des navires,
     * ce qui peut être utile pour le débogage ou les tests.
     * L'implémentation de cette méthode doit gérer le basculement entre les modes triche et normal.
     */
    void activateCheatMode();
    /**
     * Gère l'action lorsqu'on clique sur le bouton de retour.
     * Cette méthode permet de revenir au menu principal ou à l'écran précédent.
     * Elle doit être implémentée pour définir le comportement souhaité du bouton de retour.
     * @throws Exception Si une erreur se produit lors de la transition vers l'écran précédent.
     */
    void handleBackButtonClick() throws Exception;

    /**
     * Gère l'action lorsqu'on clique sur le bouton "Tirer".
     * Cette méthode doit être implémentée pour définir le comportement de tir du joueur ou de l'IA.
     * Elle vérifie si la position cible est valide, effectue le tir et affiche les résultats ou pour l'iA,
     * ell procède au tir
     */
    void handleFireButtonClick();

    /**
     * Gère la fin de la partie de bataille navale.
     * Cette méthode doit être implémentée pour définir les actions à effectuer lorsque la partie se termine.
     * Elle reçoit un paramètre indiquant si tous les navires adverses ont été coulés (true) ou non (false).
     * @param allShipsSunk true si tous les navires adverses ont été coulés, false sinon.
     */
    void endOfGame(boolean allShipsSunk);
}
