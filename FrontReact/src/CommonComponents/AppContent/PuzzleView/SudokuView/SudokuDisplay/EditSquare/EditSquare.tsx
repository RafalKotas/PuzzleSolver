// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { selectAvailableNumbersInCell } from "../../../../../../store/puzzleLogic/sudoku"
import { sudokuCellCoords } from "../../../../../../store/puzzleLogic/sudoku/types"

// (sub) components
import NumberField from "./NumberField/NumberField"

interface OwnEditSquareProps {
    squareCoords: sudokuCellCoords
}

const mapStateToProps = (state: AppState, ownProps : OwnEditSquareProps) => ({
    availableNumbers: selectAvailableNumbersInCell(state.sudokuLogicReducer, ownProps.squareCoords.rowIdx, ownProps.squareCoords.columnIdx)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type EditSquarePropsFromRedux = ConnectedProps<typeof connector>

type EditSquareProps = EditSquarePropsFromRedux & OwnEditSquareProps

const EditSquare : React.FC<EditSquareProps> = ({availableNumbers, squareCoords}) => {

    let availableNumbersExtendedTo9 = Array.from({ length: 9 }, (_, idx) => {
        return availableNumbers.includes(idx + 1) ? idx + 1 : 0
    }).reduce(
        (accumulator, _, index, array) => {
            return index % 3 === 0 ? [...accumulator, array.slice(index, index + 3)] : [...accumulator]
        }, [] as Array<Array<number>>
    )

    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            height: "45px",
            width: "45px"
        }}>
            {
                availableNumbersExtendedTo9.map((digitsRow, rowIndex) => {
                    return <div 
                    key={"digits-row-" + rowIndex}
                    style={{
                        height: "15px",
                        display: "flex",
                        flexDirection: "row"
                    }}>
                        {
                            digitsRow.map((digit, columnIndex) => {
                                return <NumberField
                                    key={"edit-square-r" + rowIndex + "-c" + columnIndex + "-d" + digit}
                                    coords={{rowIdx: squareCoords.rowIdx, columnIdx: squareCoords.columnIdx}} 
                                    digit={digit}
                                />
                            })
                        }
                    </div>
                })
            }
        </div>
    )
}

export default connector(EditSquare)