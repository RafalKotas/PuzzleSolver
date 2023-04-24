// react
import { useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"
import { AppState } from "../../../../../../../../store"
import { ModifySequence, ResetEditedSquare} from "../../../../../../../../store/data/nonogram"

interface OwnEditableSquareProps {
    section: "rowSequences" | "columnSequences",
    index: number,
    initialValue: number,
    cellNo: number
}

const mapStateToProps = (state: AppState) => ({
    cellSize: state.nonogramLayoutReducer.cellSize,
    cellBorder: state.nonogramLayoutReducer.cellBorder,
    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    resetEditedSquare: () => dispatch(ResetEditedSquare()),
    modifySequence: (value: number, section: "rowSequences" | "columnSequences", index: number, seqNo: number) => 
        dispatch(ModifySequence(value, section, index, seqNo))
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type EditableSquarePropsFromRedux = ConnectedProps<typeof connector>

type EditableSquareProps = EditableSquarePropsFromRedux & OwnEditableSquareProps

const EditableSquare : React.FC<EditableSquareProps> = ({section, index, cellNo, initialValue, 
    cellSize, cellBorder,
    modifySequence, resetEditedSquare}) => {

    const [ value, setValue ] = useState<number>(initialValue)

    const digits = Array.from({length: 10}, (_, digit) => digit.toString())

    const arrowKeys = ["ArrowUp", "ArrowRight", "ArrowLeft", "ArrowDown"]

    const allowedKeys = ["Enter", "Backspace", "Escape"].concat(digits).concat(arrowKeys)

    //2?
    const inputChangeHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
        let currentValue = event.target.value
        setValue( parseInt(currentValue) )
    }

    //1?
    const enterNewValue = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if(!allowedKeys.includes(event.key)) {
            event.preventDefault()
        } else {
            if(digits.includes(event.key)) {
                if(event.currentTarget.value.length === 2) {
                    event.preventDefault()
                }
            } else if(event.key === "Enter") {
                if(isNaN(value)) {
                    modifySequence( 0, section, index, cellNo)
                } else {
                    modifySequence( value, section, index, cellNo)
                }
            } else if(event.key === "Backspace") {

            } else if(event.key === "Escape") {
                setValue(0)
                resetEditedSquare()
            }
        }
    }

    //const borderAdditional = (sectionName : string) => (index % 5 === 4 && sectionName === "columnSequences") ? (bigSquareAdditionalBorder + "px solid black") : ""

    return (
        <input
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => {inputChangeHandler(event)}}
                onKeyDown={(event: React.KeyboardEvent<HTMLInputElement>) => {enterNewValue(event)}}
                //className={"non-arrow-number-input"}
                type="string" 
                style={{
                    width: cellSize + "px",
                    height: cellSize + "px",
                    fontSize: (0.8 * cellSize) + "px",
                    borderTop: cellBorder + "px solid black",
                    borderLeft: cellBorder + "px solid black",
                    //borderRight: borderAdditional("columnSequences"),
                    //borderBottom: borderAdditional("rowSequences"),
                    backgroundColor: "yellow"
                }}
        />

    )
}

export default connector(EditableSquare)