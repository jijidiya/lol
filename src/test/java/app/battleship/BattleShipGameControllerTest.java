package app.battleship;

import static org.junit.jupiter.api.Assertions.*;


import classes.BattleShipGameController;
import classes.CombatZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.io.IOException;

public class BattleShipGameControllerTest {
    private BattleShipGameController gameController;

    @BeforeEach
    public void setUp() {
        // Instanciez un nouvel objet BattleShipGameController avant chaque test
        gameController = new BattleShipGameController(6, 6, new int[]{3, 2});
        CombatZone combatZone = new CombatZone(gameController.getGrid());
        gameController.setGridRectangles(combatZone.getGridRectangles());

    }

    @Test
    public void testFireAtTargetPosition_Hit() {
        // Placez manuellement les bateaux sur la grille pour ce test
        // Bateau 1 (3 cases) : (0,0), (0,1), (0,2)
        gameController.placeShip(0, 0, 3, false);
        // Bateau 2 (2 cases) : (3,3), (4,3)
        gameController.placeShip(3, 3, 2, true);

        // Définissez une position cible pour le test où il y a un bateau (hit)
        String targetPosition = "A1";

        // Appliquez le tir à la position cible
        gameController.fireAtTargetPosition(targetPosition);

        // Vérifiez que le tir a touché un bateau
        assertEquals(-1, gameController.getGridValue(0, 1));
    }

    @Test
    public void testFireAtTargetPosition_Miss() {
        // Placez manuellement les bateaux sur la grille pour ce test
        // Bateau 1 (3 cases) : (0,0), (0,1), (0,2)
        gameController.placeShip(0, 0, 3, false);
        // Bateau 2 (2 cases) : (3,3), (4,3)
        gameController.placeShip(3, 3, 2, true);

        // Définissez une position cible pour le test où il n'y a pas de bateau (miss)
        String targetPosition = "C2";

        // Appliquez le tir à la position cible
        gameController.fireAtTargetPosition(targetPosition);

        // Vérifiez que le tir est dans l'eau (aucun bateau n'a été touché)
        assertEquals(-1, gameController.getGridValue(2, 2));
    }

    @Test
    public void testFireAtTargetPosition_OutOfGrid() {
        // Placez manuellement les bateaux sur la grille pour ce test
        // Bateau 1 (3 cases) : (0,0), (0,1), (0,2)
        gameController.placeShip(0, 0, 3, false);
        // Bateau 2 (2 cases) : (3,3), (4,3)
        gameController.placeShip(3, 3, 2, true);

        // Définissez une position cible en dehors de la grille pour le test
        String targetPosition = "E7";

        // Appliquez le tir à la position cible (en dehors de la grille)
        gameController.fireAtTargetPosition(targetPosition);

        // Vérifiez que le tir est en dehors de la grille
        assertFalse(gameController.isValidTargetPosition(targetPosition));
    }

    @Test

    public void testManhattanDistance() {
        // Placez manuellement les bateaux sur la grille pour ce test
        // Bateau 1 (3 cases) : (0,0), (0,1), (0,2)
        gameController.placeShip(0, 0, 3, false);
        // Bateau 2 (2 cases) : (3,3), (4,3)
        gameController.placeShip(3, 3, 2, true);

        // Définissez une position cible pour le test
        String targetPosition = "C2"; // Cible au milieu de la grille

        // Obtenez les distances de Manhattan entre la position cible et les positions des bateaux
        List<Integer> distances = gameController.manhattanDistance(targetPosition);

        // Vérifiez que les distances calculées sont correctes
        // le bateau le plus proche de la cible est à une distance de 1
        // et le bateau le plus éloigné est à une distance 3
        assertEquals(1, distances.get(0));
        assertEquals(3, distances.get(1));
    }


    @Test
    public void testAllShipsSunk() {
        // Bateau 1 (3 cases) : (0,0), (0,1), (0,2)
        gameController.placeShip(0, 0, 3, false);
        // Bateau 2 (2 cases) : (3,3), (4,3)
        gameController.placeShip(3, 3, 2, true);

        // Appliquez des tirs pour couler tous les bateaux
        gameController.fireAtTargetPosition("A0");
        gameController.fireAtTargetPosition("A1");
        gameController.fireAtTargetPosition("A2");
        gameController.fireAtTargetPosition("D3");
        gameController.fireAtTargetPosition("E3");

        // Vérifiez que tous les bateaux ont été coulés
        assertTrue(gameController.checkAllShipsSunk());
    }
    @Test
    public void testLoadGameFromFile() throws IOException {
        // Chargez la grille depuis le fichier test.txt
        String filePath = "D:\\battleship\\src\\test\\resources\\test_grid.txt";
        gameController.loadGameFromFile(filePath);
        //gameController.saveShipsPosition(grid);

        // Vérifiez que la grille est correctement chargée dans le jeu
        assertEquals(-1, gameController.getGridValue(0, 0)); // Case occupée par un bateau
        assertEquals(-1, gameController.getGridValue(1, 0)); // Case occupée par un bateau
        assertEquals(-1, gameController.getGridValue(2, 0)); // Case occupée par un bateau
        assertEquals(0, gameController.getGridValue(0, 1));  // Case vide
        assertEquals(0, gameController.getGridValue(1, 1));  // Case vide
    }
}

