package classes;



public class AIStats {
    public static void main(String[] args){
        //int numGames = Integer.parseInt(args[0]);
        int numGames = 9575;
        int sumScore = 0;
        BattleShipGameController gameController;
        CombatZone combatZone;
        BattleShipAI aIPlayer;


        for(int i= 0; i < numGames; i++){

            gameController = new BattleShipGameController();
            gameController.placeShipsRandomly();
            combatZone = new CombatZone(gameController.getGrid());
            gameController.setGridRectangles(combatZone.getGridRectangles());
            aIPlayer = new BattleShipAI(gameController);
            boolean allShipsSunk = false;
            while(!allShipsSunk){
                // l'IA effectue un tir.
                String targetPosition = aIPlayer.chooseTarget();

                //Procède au tir
                String result = gameController.fireAtTargetPosition(targetPosition);
                aIPlayer.rememberShotResult(targetPosition, result);
                aIPlayer.optimizeShots(targetPosition, gameController.manhattanDistance(targetPosition));

                if(gameController.checkAllShipsSunk()){
                    sumScore += gameController.getScore();
                    allShipsSunk = gameController.checkAllShipsSunk();
                }

            }
        }




        double averageTurns = (double) sumScore / numGames;

        System.out.println("Nombre total de coups effectués : " + sumScore);
        System.out.println("Moyenne de coups par partie : " + averageTurns);
    }
}
