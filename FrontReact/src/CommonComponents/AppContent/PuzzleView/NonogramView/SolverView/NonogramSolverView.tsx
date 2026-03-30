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
    findNextNonogramName, 
    findPreviousNonogramName, 
    selectedNonogramDetails, 
    SetSelectedNonogram 
} from "store/data/nonogram"
import { SetNonogramRelatedLogicData } from "store/puzzleLogic/nonogram"

// (sub)component(s)
import NonogramDisplay from "CommonComponents/AppContent/PuzzleView/NonogramView/NonogramDisplay/NonogramDisplay"
import NonogramActions from "CommonComponents/AppContent/PuzzleView/NonogramView/SolverView/NonogramActions/NonogramActions"
import SelectNonogramArrow from "CommonComponents/AppContent/PuzzleView/NonogramView/SolverView/SelectNonogramArrow/SelectNonogramArrow"

// services
import NonogramLogicService from "services/nonogram/nonogram.logic.service"

// fortawesome
import { faSquareCaretLeft, faSquareCaretRight } from "@fortawesome/free-solid-svg-icons"

// axios
import axios from "axios"

// styles
import "./NonogramSolverView.css"

interface OwnNonogramSolverViewProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    previousNonogramFilename: findPreviousNonogramName(state.displayReducer, state.nonogramDataReducer, state.nonogramFiltersReducer),
    nextNonogramFilename: findNextNonogramName(state.displayReducer, state.nonogramDataReducer, state.nonogramFiltersReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setSelectedNonogram: (nonogram: selectedNonogramDetails | null) => dispatch(SetSelectedNonogram(nonogram)),
    setNonogramRelatedLogicData: (nonogramRelatedLogicData: any) => dispatch(SetNonogramRelatedLogicData(nonogramRelatedLogicData))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type NonogramSolverViewProps = NonogramSolverViewPropsFromRedux & OwnNonogramSolverViewProps

const NonogramSolverView : React.FC<NonogramSolverViewProps> = ({ selectedNonogram, previousNonogramFilename, nextNonogramFilename, 
    setSelectedNonogram, setNonogramRelatedLogicData
     }) => {

    const params = useParams()

    const nonogramPath = "/resources/Nonograms/" + params.filename + ".json"

    // TODO cache result
    useEffect(() => {
        axios.get(nonogramPath)
            .then((response: { data: selectedNonogramDetails }) => {
                const nonogramFromResponse = response.data

                if (params.filename && params.filename !== nonogramFromResponse.filename) {
                    nonogramFromResponse.filename = params.filename
                }

                setSelectedNonogram(nonogramFromResponse)
            })
            .catch((error) => {
                console.error("Error fetching nonogram file (NonogramSolverView):", error)
            });
        // TODO unsubscribe
    }, [nonogramPath, params.filename, setSelectedNonogram])

    // TODO (2 queries, useEffects - too many)
    useEffect(() => {
        if (!selectedNonogram) return;

        const initData = {
            filename: selectedNonogram.filename,
            rowSequences: selectedNonogram.rowSequences,
            columnSequences: selectedNonogram.columnSequences,
            height: selectedNonogram.height,
            width: selectedNonogram.width
        };

        NonogramLogicService.initializeNonogramLogic(initData)
            .then((initResponse) => {
                setNonogramRelatedLogicData(initResponse.data)
            })
            .catch((error) => {
                console.error("Error initializing nonogram logic:", error)
            });

    }, [selectedNonogram, setNonogramRelatedLogicData]);

    const renderCondition = !!selectedNonogram && !!selectedNonogram.rowSequences &&!!selectedNonogram.columnSequences &&
            selectedNonogram.filename === params.filename;


    return (
        <div id="selected-nonogram-view">
            {
                renderCondition &&
                    <>
                        <SelectNonogramArrow 
                            arrowIcon={faSquareCaretLeft}
                            fileName={previousNonogramFilename}
                        />
                        <div
                            id="puzzle-view-container"
                            style={{
                                overflowX: "auto",
                                overflowY: "auto",
                                flexDirection: "row"
                            }}
                        >
                            <NonogramDisplay />
                        </div>
                        <SelectNonogramArrow 
                            arrowIcon={faSquareCaretRight}
                            fileName={nextNonogramFilename}
                        />
                        <NonogramActions />
                    </>
            }
        </div>
    )
}

export default connector(NonogramSolverView)