// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { SetEditMode} from "../../../../../../store/data/nonogram"

// (sub) components
import PreviewButton from "./PreviewButton/PreviewButton"
import CheckCorrectButton from "../../SolverView/NonogramActions/CheckCorrectButton/CheckCorrectButton"
import SaveSection from "./SaveSection/SaveSection"

// styles
import "./ValidateNonogramSection.css"

const mapStateToProps = (state: AppState) => ({
    correct: state.nonogramDataReducer.nonogramCorrect,
    editMode: state.nonogramDataReducer.editMode
})

const mapDispatchToProps = (dispatch : Dispatch) => ({
    setEditMode: (editMode : boolean) => dispatch(SetEditMode(editMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ValidateNonogramSectionPropsFromRedux = ConnectedProps<typeof connector>

type ValidateNonogramSectionProps = ValidateNonogramSectionPropsFromRedux

const ValidateNonogramSection : React.FC<ValidateNonogramSectionProps> = ({correct, editMode, setEditMode}) => {

    useEffect(() => {
        setEditMode(true)
        //eslint-disable-next-line
    }, [])

    const getEditModeFromChild = (editMode : boolean) => {
        setEditMode(editMode)
    }

    return (
        <section id="validate-nonogram-section">
            <PreviewButton passModeToParent={getEditModeFromChild}/>
            <CheckCorrectButton/>
            <SaveSection disabledCondition={correct !== 1 || editMode}/>
        </section>
    )
}

export default connector(ValidateNonogramSection)