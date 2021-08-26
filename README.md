# BoardGames(Chess)
A project to build virtual board games in java, starting with chess. This is a side project I work on and will add features to in my downtime.
I became interested in this project because it offers a chance to practice implementation of the MVC design and
helps me remain familiar with java as I branch out into different languages. Additionally, I was drawn to chess because
the rules have been fully defined, allowing me to focus on programming without worrying about making game design choices.

## Progress log
Summer 2020 - Created model, controller and view
* Implemented model, created abstract piece and implemented objects for each piece with their programmed moves
* Implemented view, renders board with pieces as well as a selection indication
Summer 2021 - Revamped logic, added new information to interface
* Added info to the bottom of the screen with current turn, piece score, and room for an information message
  * Added score and current turn tracking with model and controller
* Simplified move logic with new currentTurn abstraction, improved movement in/out of check tracking
  * Added detection for checkmate and stalemate, including came end mode

## Needed Gameplay features
*(in order of importance)*
* Add castling
* Add queening
* Add en passant?
* Research other chess particularities that should be added?
  * One that comes to mind - if both sides move the same piece 5 times, I think a stalemate occurs?

## Future interface features
*once gameplay features have been finished - not in any particular order*
* Move counter ("Checkmated in 5 moves")
* Timer
* Captured pieces addon to score
* Toggle flipping the board based on turn

## In the future...
* Checkers?
