## Code Review

Reviewed by: Yuxuan Yang u7078049

Reviewing code written by: Paul Won u7158520

Component: <the component being reviewed>
   - [getViablePiecePlacements()](https://gitlab.cecs.anu.edu.au/u7158520/comp1110-ass2-tue15j/-/blob/master/src/comp1110/ass2/FitGame.java#L461-490)
   - [scanPieceToAdd()](https://gitlab.cecs.anu.edu.au/u7158520/comp1110-ass2-tue15j/-/blob/master/src/comp1110/ass2/FitGame.java#L496-512)
   - [checkViablePlacement()](https://gitlab.cecs.anu.edu.au/u7158520/comp1110-ass2-tue15j/-/blob/master/src/comp1110/ass2/FitGame.java#L515-534)
### Comments 

<write your comments here>

 Best features: To finish task6, the code gets all possible top-left corner locations of a piece according to certain location that must be covered, then use isPlacementValid() to check the result.
 
 The codes are well-documented. Based on the comments in the code, I can easily understand his logic.
 
 The program decomposition is reasonable, it illustrates the main steps of achieving task6.
 
 The variable names and methods is pretty concise, the coding style is consistent throughout. 
 
 All in all, well done!


