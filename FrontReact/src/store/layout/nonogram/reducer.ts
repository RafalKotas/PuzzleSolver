import { Reducer } from "redux"
import commonFunctions from "../../../functions"
import { NonogramDataState } from "../../data/nonogram"
import { NonogramLayoutActionTypes, NonogramLayoutState, SET_NONOGRAM_CELL_SIZE } from "./types"

export const initialState: NonogramLayoutState = {

    cellSize: 16,
    cellBorder: 2,
    bigSquareAdditionalBorder: 1,
    boardDimensions: {
        width: 0,
        height: 0
    },
    sequencesSectionDimensions: {
        width: 0,
        height: 0
    },

}

export const nonogramLayoutReducer: Reducer<NonogramLayoutState, NonogramLayoutActionTypes> = (state = initialState, action : NonogramLayoutActionTypes) => {
    switch (action.type) {
        case SET_NONOGRAM_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        default:
            return state
      }
}

export const calculateCellWithBorderSize = (state : NonogramLayoutState) => {
    return (state.cellSize + state.cellBorder) 
}

export const calculateBoardDimensionInPx = (layoutState: NonogramLayoutState, nonogramState: NonogramDataState, dimension: string) => {
    let currentNonogram = nonogramState.selectedNonogram

    let dimensionUnits = 0

    if(currentNonogram) {
        dimensionUnits = dimension === "height" ? currentNonogram.height : currentNonogram.width
    }

    //simple cells with their borders
    let cellsOnly = calculateCellWithBorderSize(layoutState) * dimensionUnits
    
    //bigRectanglesBorders
    let rectanglesCount = Math.ceil(dimensionUnits / 5)
    let bigRectanglesBorders = rectanglesCount * layoutState.bigSquareAdditionalBorder

    return cellsOnly + bigRectanglesBorders
}

export const calculateSequencesSectionDimensionInPx = (layoutState: NonogramLayoutState, nonogramState: NonogramDataState, section: string) => {

    if(nonogramState.selectedNonogram) {
        let { rowSequences , columnSequences } = nonogramState.selectedNonogram

        let seqMaxLen = section === "row" ? commonFunctions.minimumLengthOfNumber(rowSequences, 3) : commonFunctions.minimumLengthOfNumber(columnSequences, 3) 
    
        return seqMaxLen * calculateCellWithBorderSize(layoutState)
    } else {
        return 3 * calculateCellWithBorderSize(layoutState)
    }
}