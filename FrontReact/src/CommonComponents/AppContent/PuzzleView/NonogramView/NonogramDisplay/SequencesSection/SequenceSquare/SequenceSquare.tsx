// react
import React, { 
    useEffect, 
    useState 
} from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    ModifySequence, 
    nonogramSquareEdit, 
    SetEditedSquare 
} from "store/data/nonogram"

// (sub)component(s)
import EditableSquare from "CommonComponents/AppContent/PuzzleView/NonogramView/NonogramDisplay/SequencesSection/SequenceSquare/EditableSquare/EditableSquare"
import NotEditableSquare from "CommonComponents/AppContent/PuzzleView/NonogramView/NonogramDisplay/SequencesSection/SequenceSquare/NotEditableSquare/NotEditableSquare"

interface OwnSequenceSquareProps {
    section: "rowSequences" | "columnSequences",
    index: number,
    initialValue: number,
    cellNo: number
}

const mapStateToProps = (state: AppState) => ({
    editedSquareCoords : [ state.nonogramDataReducer.squareEdit.coords.no, state.nonogramDataReducer.squareEdit.coords.seqNo ],
    squareSection : state.nonogramDataReducer.squareEdit.squareSection,
    mode: state.displayReducer.mode
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setEditedSquare: (currrentEditedSquare: nonogramSquareEdit) => 
        dispatch(SetEditedSquare(currrentEditedSquare)),
    modifySequence: (value: number, section: "rowSequences" | "columnSequences", index: number, seqNo: number) => 
        dispatch(ModifySequence(value, section, index, seqNo))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SequenceSquarePropsFromRedux = ConnectedProps<typeof connector>

type SequenceSquareProps = SequenceSquarePropsFromRedux & OwnSequenceSquareProps

const SequenceSquare : React.FC<SequenceSquareProps> = ({section, index, cellNo, initialValue,
    editedSquareCoords, squareSection, mode}) => {

    const [ editable, setEditable ] = useState<boolean>(false)

    const editableConditionMet = () => { 
        let coordsCondition = editedSquareCoords[0] === index && editedSquareCoords[1] === cellNo
        let sectionCondition = section === squareSection

        return (coordsCondition && sectionCondition)
    }

    useEffect(() => {
        if (editableConditionMet()) {
            setEditable(true)
        } else {
            setEditable(false)
        }

        //eslint-disable-next-line
    }, [editedSquareCoords])

    return (
        (editable && mode === "CREATE") ? <EditableSquare 
            key={"col" + index + "-cell" + cellNo}
            section={section}
            index={index}
            initialValue={initialValue}
            cellNo={cellNo}
        /> : 
        <NotEditableSquare 
            section={section}
            index={index}
            initialValue={initialValue}
            cellNo={cellNo}
        />
    )
}

export default connector(SequenceSquare)