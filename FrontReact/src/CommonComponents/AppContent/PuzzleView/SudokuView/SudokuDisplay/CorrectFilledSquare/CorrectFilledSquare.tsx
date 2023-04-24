// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { InsertDigitIntoCell, sudokuCellCoords } from "../../../../../../store/puzzleLogic/sudoku"
import { useEffect } from "react"

// styles

interface OwnCorrectFilledSquareProps {
    coords: sudokuCellCoords
    digit: number
}

const mapStateToProps = (state: AppState, ownProps : OwnCorrectFilledSquareProps) => ({
    sudokuLogicBoard: state.sudokuLogicReducer.sudokuBoard
})

const mapDispatchToProps = (dispatch: Dispatch, ownProps : OwnCorrectFilledSquareProps) => ({
    insertDigitIntoCell: () => 
        dispatch(InsertDigitIntoCell(ownProps.digit, ownProps.coords)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type CorrectFilledSquarePropsFromRedux = ConnectedProps<typeof connector>

type CorrectFilledSquareProps = CorrectFilledSquarePropsFromRedux & OwnCorrectFilledSquareProps

const CorrectFilledSquare : React.FC<CorrectFilledSquareProps> = ({sudokuLogicBoard, digit, coords, insertDigitIntoCell}) => {
    
    let {columnIdx} = coords

    useEffect(() => {
        insertDigitIntoCell()
        //eslint-disable-next-line
    }, [])

    return (
        <div
            className="sudoku-square"
            style={{
                borderRight: ((columnIdx % 3 === 2 && columnIdx !== 8) ? "4px" : "1px") + " solid black",
                fontWeight: "bold",
                fontSize: "25px"
            }}>
            {digit}
        </div>
    )
}

export default connector(CorrectFilledSquare)