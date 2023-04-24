import { Reducer } from "redux"
import { HitoriDataState } from "../../data/hitori"
import { HitoriLayoutActionTypes, HitoriLayoutState, maxCellSizeBiggestPuzzle, minCellSizeBiggestPuzzle, 
    SET_HITORI_CELL_SIZE, SET_MAXIMUM_HITORI_CELL_SIZE, SET_MINIMUM_HITORI_CELL_SIZE } from "./types"

export const initialState: HitoriLayoutState = {
    minCellSize: minCellSizeBiggestPuzzle,
    maxCellSize: maxCellSizeBiggestPuzzle,
    frameBorder: 2,
    cellSize: maxCellSizeBiggestPuzzle,
    cellBorder: 2

}

export const hitoriLayoutReducer: Reducer<HitoriLayoutState, HitoriLayoutActionTypes> = (state = initialState, action : HitoriLayoutActionTypes) => {
    switch (action.type) {
        case SET_HITORI_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        case SET_MINIMUM_HITORI_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        case SET_MAXIMUM_HITORI_CELL_SIZE:
            return {
                ...state,
                maxCellSize: action.payload.cellSize
            }
        default:
            return state
      }
}

export const calculateCellWithBorderSize = (state : HitoriLayoutState) => {
    return (state.cellSize + state.cellBorder) 
}

export const calculateBoardDimensionInPx = (hitoriLayoutState: HitoriLayoutState, hitoriDataState: HitoriDataState, dimension: string) => {
    
    let currenthitori = hitoriDataState.selectedHitori
    
    let dimensionUnits = dimension === "height" ? currenthitori.height : currenthitori.width

    // only cells without borders
    let cellsOnly = hitoriLayoutState.cellSize * dimensionUnits
    
    // borders (cells + 1)
    let borders = hitoriLayoutState.cellBorder * (dimensionUnits + 1)

    return cellsOnly + borders
}