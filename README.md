# PuzzleSolver
at first a hobby project aimed at solving various types of logic puzzles, and more recently **a source of algorithms that are the subject of description and research in a master's thesis**

Technologies and languages used:
* Java(Spring)
* TypeScript(React + Redux).
* MySQL (logic puzzle parameters, i.e. file name, difficulty, source, size, etc.).

Logic puzzles included in the application:
1. Nonograms/Japanese puzzles
* the current version allows solving nonograms with heuristics consisting of a total of 9 sub-procedures analogous for rows and columns.
* in addition to the subprocedure-based logic itself, a trial-and-error method ("one-level recursion") has been implemented. This method checks whether a solution with a step
forward is invalid for one of the decisions ("X" or "O", a field "drawn"/"blank" or "colored").
* possible selection of nonograms using filters and sorting
* possible creation of nonograms in the CREATE section with the selection of basic features, then validation and saving to a file in .json format
2. Sudoku
* possible selection of sudoku type puzzles using filters and sorting
* created view for sudoku type puzzle (solving and creating puzzle)
* for the moment, validation only on the basis of the number of filled cells (there must be at least 17 digits in the diagram for an unambiguous solution)
* simple solver based on selecting 1 of 4 types of actions and the range of action execution
3. Akari
* possible selection of akari type puzzle using filters and sorting 
* created view for akari type puzzle
4. Hitori
* possible selection of hitori type puzzle using filters and sorting 
* created view for hitori type puzzle
5. Slitherlink
* possible selection of slitherlink type puzzle using filters and sorting
* created view for slitherlink type puzzle
6. Architect
* possible selection of architect type puzzle using filters and sorting
* created view for architect type puzzle

To be implemented in the future:
* heuristic or other algorithms to solve each of the 6 types of puzzles listed, or others as well
* refining the application's graphical interface
* create, validate and save puzzles to .json files with a specific, uniform structure
