// react
import React, { useEffect } from "react"

// react - router
import { useParams } from "react-router-dom"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    modes, 
    SetMode 
} from "store/display"
import { 
    selectedNonogramDetails, 
    SetSelectedNonogram, 
    transformNonogramInformationIntoNonogramDetails 
} from "store/data/nonogram"

// (sub)component(s)
import EditNonogramActions from "CommonComponents/AppContent/PuzzleView/NonogramView/EditView/EditNonogramActions/EditNonogramActions"
import ValidateNonogramSection from "CommonComponents/AppContent/PuzzleView/NonogramView/EditView/ValidateNonogramSection/ValidateNonogramSection"
import NonogramDisplay from "CommonComponents/AppContent/PuzzleView/NonogramView/NonogramDisplay/NonogramDisplay"

// styles
import "./NonogramEditView.css"

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    createdNonogramsList: state.nonogramDataReducer.createdNonogramsList
})

const mapDispatchToProps = (dispatch : Dispatch) => ({
    setMode: (mode : modes) => dispatch(SetMode(mode)),
    setSelectedNonogram: (nonogram: selectedNonogramDetails | null) => dispatch(SetSelectedNonogram(nonogram))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramEditViewPropsFromRedux = ConnectedProps<typeof connector>

type NonogramEditViewProps = NonogramEditViewPropsFromRedux

const NonogramEditView : React.FC<NonogramEditViewProps> = ({selectedNonogram, createdNonogramsList, setMode, setSelectedNonogram}) => {

    useEffect(() => {
        setMode("CREATE")
        //eslint-disable-next-line
    }, [])

    const params = useParams()

    const nonogramPath = "../../resources/Nonograms/" + params.filename + ".json"

    useEffect(() => {
        createdNonogramsList.forEach((nonogram) => {
            if (nonogram.filename === params.filename) {
                setSelectedNonogram( transformNonogramInformationIntoNonogramDetails(nonogram) )
            }
        })
    }, [createdNonogramsList, nonogramPath, setSelectedNonogram, params.filename])

    const renderCondition = () => selectedNonogram && params.filename && selectedNonogram.filename === params.filename

    return (
        <div id="selected-nonogram-view">
            {renderCondition() && <React.Fragment>
                <ValidateNonogramSection />
                <div
                    id="edit-view-container"  
                    style={{
                            overflowX: "auto",
                            overflowY: "auto",
                            flexDirection: "row"
                            }}
                >
                    <NonogramDisplay />
                </div>
                <EditNonogramActions key={"edit-nonogram-actions"}/>
            </React.Fragment>}
        </div>
    )
}

export default connector(NonogramEditView)