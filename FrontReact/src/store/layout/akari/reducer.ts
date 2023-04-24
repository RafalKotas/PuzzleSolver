import { Reducer } from "redux"
import { AkariDataState } from "../../data/akari"
import { minCellSize } from "../akari"
import { AkariLayoutActionTypes, AkariLayoutState, SET_AKARI_CELL_SIZE } from "./types"

export const initialState: AkariLayoutState = {

    cellSize: minCellSize,
    cellBorder: 2

}

export const akariLayoutReducer: Reducer<AkariLayoutState, AkariLayoutActionTypes> = (state = initialState, action : AkariLayoutActionTypes) => {
    switch (action.type) {
        case SET_AKARI_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        default:
            return state
      }
}

export const calculateCellWithBorderSize = (state : AkariLayoutState) => {
    return (state.cellSize + state.cellBorder) 
}

export const calculateBoardDimensionInPx = (akariLayoutState: AkariLayoutState, akariDataState: AkariDataState, dimension: string) => {
    
    let currentAkari = akariDataState.selectedAkari
    
    let dimensionUnits = dimension === "height" ? currentAkari.height : currentAkari.width

    // only cells without borders
    let cellsOnly = akariLayoutState.cellSize * dimensionUnits
    
    // borders (cells + 1)
    let borders = akariLayoutState.cellBorder * (dimensionUnits + 1)

    return cellsOnly + borders
}