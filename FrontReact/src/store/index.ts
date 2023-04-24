import { createStore, combineReducers } from "redux"

// data reducers (6x)
import { nonogramDataReducer } from "./data/nonogram"
import { sudokuDataReducer } from "./data/sudoku"
import { slitherlinkDataReducer } from "./data/slitherlink"
import { akariDataReducer } from "./data/akari"
import { hitoriDataReducer } from "./data/hitori"
import { architectDataReducer } from "./data/architect/reducer"

// filters reducers (6x)
import { nonogramFiltersReducer } from "./filters/nonogram"
import { sudokuFiltersReducer } from "./filters/sudoku"
import { slitherlinkFiltersReducer } from "./filters/slitherlink"
import { akariFiltersReducer } from "./filters/akari"
import { hitoriFiltersReducer } from "./filters/hitori"
import { architectFiltersReducer } from "./filters/architect"

// layout reducers (4-6x)
import { nonogramLayoutReducer } from "./layout/nonogram"
import { slitherlinkLayoutReducer } from "./layout/slitherlink"
import { akariLayoutReducer } from "./layout/akari"
import { hitoriLayoutReducer } from "./layout/hitori"
import { architectLayoutReducer } from "./layout/architect"

// logic reducers (6x)
import { nonogramLogicReducer } from "./puzzleLogic/nonogram"
import { sudokuLogicReducer } from "./puzzleLogic/sudoku"

// common
import  { commonReducer } from "./commons/"

// bootstrap
//import "bootstrap/dist/css/bootstrap.min.css"

import { displayReducer } from "./display"

import { composeWithDevTools } from "redux-devtools-extension"

export const rootReducer = combineReducers({
  commonReducer,
  nonogramDataReducer,
  nonogramFiltersReducer,
  nonogramLogicReducer,
  nonogramLayoutReducer,
  sudokuDataReducer,
  sudokuFiltersReducer,
  sudokuLogicReducer,
  slitherlinkDataReducer,
  slitherlinkFiltersReducer,
  slitherlinkLayoutReducer,
  akariDataReducer,
  akariFiltersReducer,
  akariLayoutReducer,
  hitoriDataReducer,
  hitoriFiltersReducer,
  hitoriLayoutReducer,
  architectDataReducer,
  architectFiltersReducer,
  architectLayoutReducer,
  displayReducer
})

export type AppState = ReturnType<typeof rootReducer>

export const store = createStore(rootReducer,
  composeWithDevTools( )
)