import { Reducer } from "redux"
import { ArchitectDataState } from "../../data/architect"
import { ArchitectLayoutActionTypes, ArchitectLayoutState, SET_ARCHITECT_CELL_SIZE } from "./types"

export const initialState: ArchitectLayoutState = {

    cellSize: 36,
    cellBorder: 2

}

export const architectLayoutReducer: Reducer<ArchitectLayoutState, ArchitectLayoutActionTypes> = (state = initialState, action : ArchitectLayoutActionTypes) => {
    switch (action.type) {
        case SET_ARCHITECT_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        default:
            return state
      }
}

export const calculateCellWithBorderSize = (state : ArchitectLayoutState) => {
    return (state.cellSize + state.cellBorder) 
}

export const calculateBoardDimensionInPx = (architectLayoutState: ArchitectLayoutState, architectDataState: ArchitectDataState, dimension: string) => {
    
    let currentArchitect = architectDataState.selectedArchitect
    
    let dimensionUnits = dimension === "height" ? currentArchitect.height : currentArchitect.width

    // only cells without borders
    let cellsOnly = architectLayoutState.cellSize * dimensionUnits
    
    // borders (cells + 1)
    let borders = (dimensionUnits + 1) * architectLayoutState.cellBorder

    return cellsOnly + borders
}