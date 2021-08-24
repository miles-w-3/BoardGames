import controller.BoardController;
import controller.ChessController;

public class BoardGames {

  public static void main(String[] args){
    BoardController controller;
    controller = new ChessController();
    controller.playGame();
  }

}


