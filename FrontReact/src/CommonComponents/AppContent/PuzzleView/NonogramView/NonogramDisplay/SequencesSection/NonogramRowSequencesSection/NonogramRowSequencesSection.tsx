//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"

// (sub)components
import SequenceSquare from "../SequenceSquare/SequenceSquare"

// styles
import "../NonogramSequencesSection.css"

// others
import commonFunctions from "../../../../../../../functions"
import { calculateBoardDimensionInPx, calculateSequencesSectionDimensionInPx } from "../../../../../../../store/layout/nonogram"

interface OwnNonogramRowSequencesSectionProps {
    
}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    cellSize: state.nonogramLayoutReducer.cellSize,
    cellBorder: state.nonogramLayoutReducer.cellBorder,
    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,

    sectionHeight: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "height"),
    sectionWidth: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "row") 
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramRowSequencesSectionPropsFromRedux = ConnectedProps<typeof connector>

type NonogramRowSequencesSectionProps = NonogramRowSequencesSectionPropsFromRedux & OwnNonogramRowSequencesSectionProps

const NonogramRowSequencesSection : React.FC<NonogramRowSequencesSectionProps> = ({selectedNonogram,
     sectionHeight, sectionWidth, cellSize, cellBorder, bigSquareAdditionalBorder}) => {

    //at least 3 cells to make difficulty stars visible
    if(selectedNonogram) {
    let maxLength = Math.max(commonFunctions.maximumArrayLengthArrayOfArrays(selectedNonogram.rowSequences), 3)
    let { rowSequences } = selectedNonogram

    return (
        <div
            id="rowSequencesSection" 
            style={{
                display: "flex",
                flexDirection: "column",
                width: sectionWidth,
                height: sectionHeight + 1 //TODO obliczyć jeszcze raz
            }}
        >
            {
                rowSequences.map((sequence, rowIndex) => {
                    return (
                        <React.Fragment>
                            <div id={"seq-row-" + rowIndex} 
                                style={{
                                display: "flex",
                                flexDirection: "row",
                                width: sectionWidth + "px",
                                height: (cellSize + cellBorder + ( (rowIndex % 5 === 4) ? bigSquareAdditionalBorder : 0)) + "px"
                            }}>
                                {
                                    <React.Fragment>
                                    {
                                        Array.from(Array(maxLength - sequence.length).keys()).map((cellNo) => {
                                            return (
                                                <div
                                                    className="single-number-cell"
                                                    style={{
                                                        width: cellSize + "px",
                                                        fontSize: (0.8 * cellSize) + "px",
                                                        borderTop: cellBorder + "px solid black",
                                                        borderLeft: cellBorder + "px solid black",
                                                        borderBottom: (rowIndex % 5 === 4) ? (bigSquareAdditionalBorder + "px solid black") : ""
                                                }}>
                                                    
                                                </div>
                                            )
                                        })
                                    }
                                    {
                                        Array.from(Array(sequence.length).keys()).map((cellNo) => {
                                            return (
                                                <SequenceSquare
                                                    cellNo={cellNo}
                                                    index={rowIndex}
                                                    section={"rowSequences"}
                                                    initialValue={sequence[cellNo]}
                                                />
                                            )
                                        })
                                    }
                                    </React.Fragment>
                                }
                            </div>
                        </React.Fragment>
                    )
                })
            }
        </div>
    )
    } else {
        return <div>
            
        </div>
    }
}

export default connector(NonogramRowSequencesSection)
