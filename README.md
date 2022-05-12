# Sudoku Solver
The program attempts to solve a sudoku, with less than 17 clues, counting the solutions in order to return the result. It works in parallel. <br>
A puzzle with less than 17 clues can have more than 1 solution, even milions.

The comment in the code are in Italian because the project was for the Italian Bachelor. The code is in the psmcProject folder.

## Algorithmic Approach
The program take in input the text file representing the sudoku 9x9 table and it creates a sudoku instance, next the all possible legal values for each row, column, region (the square with 9 cells of sudoku) and every single block are calculated, note that is equal to write in each cell (block in the code) with the pencil the possible solution during a sudoku match on a sheet of paper.<br>
At this time, one check if there are blocks with only one possible legal value and put this value as value for the block then we have an instance of sudoku with at least one block has at least 2 possible legal value or a completed instance of sudoku.

The parallel part of the program consists to identify the first empty block, i.e. a block with no assigned value, starting from the origin increasing each time the value of a column while the desired block occurs. Now we pick the possible legal values of this block and we suppose are “m”, we want to check the legality of each value, in order to do this we put them (one by one) in the block itself but in a copy of original matrix (sudoku table) then we are going to create m matrix (since we have m possible value to check). The legality of inserted value is verify checking that exists no block in the matrix having the cardinality of the legal values set equal to 0, if the test is successful a fork is performed on that matrix otherwise it is discarded as it is an illegal sudoku, this is performed for all and "m" values.<br> 
In the worst case we will have m legal values and therefore m-1 fork, the procedure described above is repeated for each "forked" matrix, checking before it is complete, in which case the value 1 is returned.

<div align="center">
  <img width="574" 
       align="middle"
       alt="Screenshot 2022-05-12 at 11 47 59" 
       src="https://user-images.githubusercontent.com/71827432/168043049-8208e32d-20b8-43e8-96d6-e8fa0c7a8d44.png">
</div>

The above photo shows the algorithm execution, in which the orange instances are the ones discarded as the value entered has led to an illegal sudoku instance, while the other two are the forks where the green instance is a complete sudoku which will return 1, while the algorithm will be run again on the other celestial one.
