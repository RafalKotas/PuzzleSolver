// redux
import { Tooltip } from "@mui/material"
import { useEffect } from "react"
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"
import commonFunctions from "../../../../../../../functions"

// redux - store
import { AppState } from "../../../../../../../store"
import { AddMarkedCell, InsertDigitIntoCell, InsertDigitIntoCellEditMode, RemoveDigitFromCellEditMode, selectOccurencesInColumn, selectOccurencesInRow, selectOccurencesInSquare3x3, selectOnlyAvailableInCell, sudokuCellCoords } from "../../../../../../../store/puzzleLogic/sudoku"

// styles
import "./NumberField.css"

interface OwnNumberFieldProps {
    coords: sudokuCellCoords,
    digit: number
}

const mapStateToProps = (state: AppState, ownProps : OwnNumberFieldProps) => ({
    mode: state.displayReducer.mode,

    sudokuBoardEditMode: state.sudokuLogicReducer.sudokuBoardEditMode,
    availableInCell: state.sudokuLogicReducer.availableNumbers[ownProps.coords.rowIdx][ownProps.coords.columnIdx],

    consideredRows: state.sudokuLogicReducer.consideredRows,
    consideredColumns: state.sudokuLogicReducer.consideredColumns,
    markOption: state.sudokuLogicReducer.markOption,
    occurencesInRow: selectOccurencesInRow(state.sudokuLogicReducer, ownProps.coords.rowIdx, ownProps.digit),
    occurencesInColumn: selectOccurencesInColumn(state.sudokuLogicReducer, ownProps.coords.columnIdx, ownProps.digit),
    occurrencesIn3x3Square: selectOccurencesInSquare3x3(state.sudokuLogicReducer, ownProps.coords, ownProps.digit),
    onlyAvailableInCell: selectOnlyAvailableInCell(state.sudokuLogicReducer, ownProps.coords.rowIdx, ownProps.coords.columnIdx, ownProps.digit)
})

const mapDispatchToProps = (dispatch: Dispatch, ownProps : OwnNumberFieldProps) => ({
    insertDigitIntoCell: () => dispatch(InsertDigitIntoCell(ownProps.digit, ownProps.coords)),
    insertDigitIntoCellEditMode: () => dispatch(InsertDigitIntoCellEditMode(ownProps.digit, ownProps.coords)),
    removeDigitFromCellEditMode: () => dispatch(RemoveDigitFromCellEditMode(ownProps.digit, ownProps.coords)),
    addMarkedCell: () => dispatch(AddMarkedCell(ownProps.digit, ownProps.coords))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NumberFieldPropsFromRedux = ConnectedProps<typeof connector>

type NumberFieldProps = NumberFieldPropsFromRedux & OwnNumberFieldProps

const NumberField : React.FC<NumberFieldProps> = ({mode, availableInCell, sudokuBoardEditMode, digit, coords, 
    consideredRows, consideredColumns, markOption, 
    occurencesInRow, occurencesInColumn, occurrencesIn3x3Square, onlyAvailableInCell, 
    insertDigitIntoCell, insertDigitIntoCellEditMode, removeDigitFromCellEditMode, addMarkedCell}) => {

    useEffect(() => {
        //only for "READ" - solver mode
        if(matchesSelection()) {
            addMarkedCell()
        }
    })

    let { rowIdx, columnIdx } = coords

    const numberFieldClickHandler = () => {
        if(digit !== 0) {
            if(mode === "READ") {
                insertDigitIntoCell()
            } else {
                if(sudokuBoardEditMode[rowIdx][columnIdx] === digit && availableInCell.length === 1) {
                    removeDigitFromCellEditMode()
                } else {
                    insertDigitIntoCellEditMode()
                }
            }
        }
    }

    const matchesSelection = () => {
        if(mode === "CREATE" || !matchesCoords()) {
            return false
        }
        switch(markOption) {
            case ("One possible option in cell"):
                return onlyAvailableInCell
            case ("One available place for digit in row"):
                return occurencesInRow === 1
            case ("One available place for digit in column"):
                return occurencesInColumn === 1
            case ("One available place for digit in square 3x3"):
                return occurrencesIn3x3Square === 1
            default:
                return false

        }
    }

    const matchesCoords = () => {
        let {rowIdx, columnIdx} = coords
        return commonFunctions.isValueInRange(rowIdx, consideredRows) || commonFunctions.isValueInRange(columnIdx, consideredColumns)
    }

    const selectedInEditMode = () => {
        return (mode === "CREATE" && digit !==0 && sudokuBoardEditMode[ rowIdx ][ columnIdx ] === digit)
    }

    return (
        <Tooltip title={"in row: " + occurencesInRow + " in column: " + occurencesInColumn}>
            <div style={{
                height: "15px",
                width: "15px",
                fontSize: "10px",
                backgroundColor: matchesSelection() ? "green" : (selectedInEditMode() ? "#faf2ba" : ""),
                cursor: digit !== 0 ? "pointer" : "normal"
            }}
                //className={digit !== 0 ? "edit-square-number": ""}
                onClick={(event: React.MouseEvent<HTMLDivElement, MouseEvent>) => numberFieldClickHandler()}
            >
                {digit !== 0 && digit}
            </div>
        </Tooltip>
    )
}

export default connector(NumberField)