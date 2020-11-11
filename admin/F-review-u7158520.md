## Code Review

Reviewed by: Paul Won, u7158520

Reviewing code written by: Chensheng Zhang u6615215

Component: <the component being reviewed>
- [isPlacementValid()](https://gitlab.cecs.anu.edu.au/u7158520/comp1110-ass2-tue15j/-/blob/master/src/comp1110/ass2/FitGame.java#L388-443)
- [rotate_90_Clockwise()](https://gitlab.cecs.anu.edu.au/u7158520/comp1110-ass2-tue15j/-/blob/master/src/comp1110/ass2/FitGame.java#L362-371)

### Comments 

The best features are that the code logic was easy to understand with distinct variable names and good use of annotations. Also, I can see it tried to simplify codes, for example, simple string split method or using an existing method (rotatePiece()) that can rotate the piece instead of creating a new method.
The codes are well-documented with logical flow of steps.
I think it had appropriate program decomposition. When looking at the structure, the method checks for possible false cases before proceeding further. Then, it initializes variables to be used for the function.
I can see switch function correctly rotate with all possible cases. Then, it checks for possible invalid results.
These all accomplish what needs to be done for task 5.
The coding style is mostly consistent with good choice of variable names. However, the same use of annotations could have been done for rotate_90_Clockwise().
Perhaps, if the given placement string was null, it can give an error such as NullPointerException.