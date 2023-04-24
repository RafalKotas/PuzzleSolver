// react
import React, { useEffect } from "react"
import { useNavigate, useParams } from "react-router-dom"

import axios from "axios"

// (sub) components
import NonogramDisplay from "../NonogramDisplay/NonogramDisplay"
import NonogramActions from "./NonogramActions/NonogramActions"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faSquareCaretLeft, faSquareCaretRight } from "@fortawesome/free-solid-svg-icons"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"
import { findNextNonogramName, findPreviousNonogramName, selectedNonogramDetails, SetSelectedNonogram } from "../../../../../store/data/nonogram"

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
    setSelectedNonogram: (nonogram: selectedNonogramDetails | null) => dispatch(SetSelectedNonogram(nonogram))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type NonogramSolverViewProps = NonogramSolverViewPropsFromRedux & OwnNonogramSolverViewProps

const NonogramSolverView: React.FC<NonogramSolverViewProps> = ({ selectedNonogram, setSelectedNonogram, 
    previousNonogramFilename, nextNonogramFilename }) => {

    const navigate = useNavigate()
    const params = useParams()

    const nonogramPath = "../../resources/allNonogramsJSON/" + params.filename + ".json"

    console.log(useParams())

    useEffect(() => {

        axios.get(nonogramPath).then((response: { data: selectedNonogramDetails }) => {
            let nonogramFromResponse = response.data
            if (nonogramFromResponse) {
                console.log(nonogramFromResponse)
                if (params.filename) {
                    console.log("nonogram filename: " + params.filename)
                    nonogramFromResponse["filename"] = params.filename
                }
                console.log(nonogramFromResponse)
                setSelectedNonogram(nonogramFromResponse)
            }
        })

    }, [nonogramPath, setSelectedNonogram, params.filename])

    const renderCondition = () => selectedNonogram && params.filename && selectedNonogram.filename === params.filename

    return (
        <div id="selected-nonogram-view">
            {
                renderCondition() ?
                    <React.Fragment>
                        <div className={"change-puzzle-arrow-wrapper"}>
                            <FontAwesomeIcon 
                                className={"change-puzzle-arrow"}
                                icon={faSquareCaretLeft} 
                                onClick={() => {
                                    if(previousNonogramFilename) {
                                        navigate("../nonogram-solver/" + previousNonogramFilename)
                                    }
                                }}
                            />
                        </div>
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
                        <div className={"change-puzzle-arrow-wrapper"}>
                            <FontAwesomeIcon 
                                className={"change-puzzle-arrow"}
                                icon={faSquareCaretRight}
                                onClick={() => {
                                    if(nextNonogramFilename) {
                                        navigate("../nonogram-solver/" + nextNonogramFilename)
                                    }
                                }}
                            />
                        </div>
                        <NonogramActions />
                    </React.Fragment>
                    : <React.Fragment>

                    </React.Fragment>
            }
        </div>
    )
}

export default connector(NonogramSolverView)