import { Reducer } from "redux"
import { SlitherlinkDataState } from "../../data/slitherlink"
import { SlitherlinkLayoutActionTypes, SlitherlinkLayoutState, SET_SLITHERLINK_CELL_SIZE } from "./types"

export const initialState: SlitherlinkLayoutState = {

    cellSize: 48,
    edgeWidth: 12

}

export const slitherlinkLayoutReducer: Reducer<SlitherlinkLayoutState, SlitherlinkLayoutActionTypes> = (state = initialState, action : SlitherlinkLayoutActionTypes) => {
    switch (action.type) {
        case SET_SLITHERLINK_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        default:
            return state
      }
}

export const calculateBoardDimensionInPx = (slitherlinkLayoutState: SlitherlinkLayoutState, slitherlinkDataState: SlitherlinkDataState, dimension: string) => {
    
    let currentSlitherlink = slitherlinkDataState.selectedSlitherlink
    
    let dimensionUnits = dimension === "height" ? currentSlitherlink.height : currentSlitherlink.width

    // only cells without edges
    let cellsOnly = slitherlinkLayoutState.cellSize * dimensionUnits
    
    // borders (cells + 1)
    let edges = slitherlinkLayoutState.edgeWidth * (dimensionUnits + 1)

    return cellsOnly + edges
}

export const selectCellSize = (slitherlinkLayoutState: SlitherlinkLayoutState) => slitherlinkLayoutState.cellSize

export const selectEdgeWidth = (slitherlinkLayoutState: SlitherlinkLayoutState ) => slitherlinkLayoutState.edgeWidth

export const selectCellPlusEdgeHeight = (slitherlinkLayoutState : SlitherlinkLayoutState) => 
    selectCellSize(slitherlinkLayoutState) + selectEdgeWidth(slitherlinkLayoutState)