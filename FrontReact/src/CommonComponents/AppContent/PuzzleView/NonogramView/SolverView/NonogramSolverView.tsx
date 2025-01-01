// react
import React, { useEffect } from "react"
import { useParams } from "react-router-dom"

import axios from "axios"

// (sub) components
import NonogramDisplay from "../NonogramDisplay/NonogramDisplay"
import NonogramActions from "./NonogramActions/NonogramActions"
import SelectNonogramArrow from "./SelectNonogramArrow/SelectNonogramArrow"
import { faSquareCaretLeft, faSquareCaretRight } from "@fortawesome/free-solid-svg-icons"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"
import { findNextNonogramName, findPreviousNonogramName, selectedNonogramDetails, SetSelectedNonogram } from "../../../../../store/data/nonogram"
import { InitializeSolverData } from "../../../../../store/puzzleLogic/nonogram"

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
    initializeSolverData: (rowsSequences: number[][], columnsSequences: number[][]) => dispatch(InitializeSolverData(rowsSequences, columnsSequences)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type NonogramSolverViewProps = NonogramSolverViewPropsFromRedux & OwnNonogramSolverViewProps

const NonogramSolverView : React.FC<NonogramSolverViewProps> = ({ selectedNonogram, previousNonogramFilename, nextNonogramFilename, 
    setSelectedNonogram, initializeSolverData
     }) => {

        useEffect(() => {

            //eslint-disable-next-line
        }, [selectedNonogram?.filename])

    const params = useParams()

    const nonogramPath = "../../resources/Nonograms/" + params.filename + ".json"

    console.log("selected nonogram: ")
    console.log(selectedNonogram)

    useEffect(() => {

        axios.get(nonogramPath).then((response: { data: selectedNonogramDetails }) => {
            let nonogramFromResponse = response.data
            if (nonogramFromResponse) {
                if (params.filename) {
                    nonogramFromResponse.filename = params.filename
                }
                setSelectedNonogram(nonogramFromResponse)
                initializeSolverData(nonogramFromResponse.rowSequences, nonogramFromResponse.columnSequences)
            }
        })

    }, [nonogramPath, setSelectedNonogram, params.filename])

    const renderCondition = () => selectedNonogram && params.filename && selectedNonogram.filename === params.filename

    return (
        <div id="selected-nonogram-view">
            {
                renderCondition() ?
                    <React.Fragment>
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
                    </React.Fragment>
                    : <React.Fragment>

                    </React.Fragment>
            }
        </div>
    )
}

export default connector(NonogramSolverView)