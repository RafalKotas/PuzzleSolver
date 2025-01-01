// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"
import { AppState } from "../../../../../../../../store"
import { nonogramSquareEdit, SetEditedSquare } from "../../../../../../../../store/data/nonogram"
import { selectSequenceFulFilledConditionMet } from "../../../../../../../../store/puzzleLogic/nonogram"

interface OwnNotEditableSquareProps {
    section: string,
    index: number,
    initialValue: number,
    cellNo: number
}

const mapStateToProps = (state: AppState, ownProps : OwnNotEditableSquareProps) => ({
    editedSquareCoords : [ state.nonogramDataReducer.squareEdit.coords.no, state.nonogramDataReducer.squareEdit.coords.seqNo ],
    editing : state.nonogramDataReducer.squareEdit.editing,
    mode: state.displayReducer.mode,

    editMode: state.nonogramDataReducer.editMode,

    cellSize: state.nonogramLayoutReducer.cellSize,
    cellBorder: state.nonogramLayoutReducer.cellBorder,
    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,
    sequenceFulFilled: selectSequenceFulFilledConditionMet(state.nonogramLogicReducer, ownProps.cellNo, ownProps.index, ownProps.section)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setEditedSquare: (currrentEditedSquare: nonogramSquareEdit) => 
        dispatch(SetEditedSquare(currrentEditedSquare))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NotEditableSquarePropsFromRedux = ConnectedProps<typeof connector>

type NotEditableSquareProps = NotEditableSquarePropsFromRedux & OwnNotEditableSquareProps

const NotEditableSquare : React.FC<NotEditableSquareProps> = ({section, index, cellNo, initialValue, 
    cellSize, cellBorder, bigSquareAdditionalBorder,
    setEditedSquare, mode, editMode, editing, sequenceFulFilled}) => {

    const dispatchNewEditableSquareDetails = () => {

        let newSquareToEdit : nonogramSquareEdit = {
            editing: true,
            squareSection: section === "columnSequences" ? "columnSequences" : "rowSequences",
            coords: {
                no: index,
                seqNo: cellNo
            }
        }

        //!editing - no other square choosen before, editMode - edit+ preview-,  mode === "CREATE" - only when creating
        if(!editing && editMode && mode === "CREATE") {
            setEditedSquare(newSquareToEdit)
        }
    }

    return (
        <div onClick={() => dispatchNewEditableSquareDetails()} className="single-number-cell" style={{
                width: cellSize + "px",
                height: cellSize + "px",
                fontSize: (0.8 * cellSize) + "px",
                backgroundColor: sequenceFulFilled ? "#59ea61" : "",
                borderTop: cellBorder + "px solid black",
                borderLeft: cellBorder + "px solid black",
                borderRight: (index % 5 === 4 && section === "columnSequences") ? (bigSquareAdditionalBorder + "px solid black") : "",
                borderBottom: (index % 5 === 4 && section === "rowSequences") ? (bigSquareAdditionalBorder + "px solid black") : "",
                cursor: (editing || !editMode) ? "default" : "pointer"
            }}
        >
            {initialValue}
        </div>
    )
}

export default connector(NotEditableSquare)