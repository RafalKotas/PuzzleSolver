// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../../store"

// (sub) components
import SequenceSquare from "../SequenceSquare/SequenceSquare"

// others
import commonFunctions from "../../../../../../../functions"

// styles
import "../NonogramSequencesSection.css"
import { calculateBoardDimensionInPx, calculateSequencesSectionDimensionInPx } from "../../../../../../../store/layout/nonogram"

interface OwnNonogramColumnSequencesSectionProps {
    //sectionHeight: number,
    //sectionWidth: number
}


const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    cellSize: state.nonogramLayoutReducer.cellSize,
    cellBorder: state.nonogramLayoutReducer.cellBorder,
    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,

    sectionWidth: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "width"),
    sectionHeight: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "height")
})

const connector = connect(mapStateToProps)

type NonogramColumnSequencesSectionPropsFromRedux = ConnectedProps<typeof connector>

type NonogramColumnSequencesSectionProps = NonogramColumnSequencesSectionPropsFromRedux & OwnNonogramColumnSequencesSectionProps

const NonogramColumnSequencesSection : React.FC<NonogramColumnSequencesSectionProps> = ({selectedNonogram, 
    sectionHeight, sectionWidth, cellSize, cellBorder, bigSquareAdditionalBorder}) => {

    if(selectedNonogram) {
        let { columnSequences } = selectedNonogram
        let maxLength = Math.max(commonFunctions.maximumArrayLengthArrayOfArrays(selectedNonogram.columnSequences), 3)
    
        let widthInSquares = selectedNonogram.width

        return (
            <div 
                id="column-sequences-section"
                style={{
                    display: "flex",
                    flexDirection: "row",
                    width: sectionWidth,
                    height: sectionHeight
            }}>
                {
                    columnSequences.map((sequences, columnIndex) => {
                        return (
                                <div
                                    key={"column" + columnIndex + "-sequences-" + sequences.join("-")} 
                                    style={{
                                    display: "flex",
                                    flexDirection: "column",
                                    width: (cellSize + cellBorder) + (columnIndex % 5 === 4 ? bigSquareAdditionalBorder : 0) + "px", //cellSize
                                    height: sectionHeight + "px"
                                }}>
                                    {
                                        <React.Fragment>
                                            {
                                                Array.from(Array(maxLength - sequences.length).keys()).map((cellNo) => {
                                                    return (
                                                        <div
                                                            key={"non-sequence-square-c" + columnIndex + "-" + cellNo} 
                                                            className="single-number-cell" 
                                                            style={{
                                                                height: cellSize + "px",
                                                                fontSize: (0.8 * cellSize) + "px",
                                                                borderTop: cellBorder + "px solid black",
                                                                borderLeft: cellBorder + "px solid black",
                                                                borderRight: (columnIndex % 5 === 4 || columnIndex === widthInSquares - 1) ? (bigSquareAdditionalBorder + "px solid black") : ""
                                                            }}
                                                        >
    
                                                        </div>
                                                    )
                                                })
                                            }
                                            {
                                                Array.from(Array(sequences.length).keys()).map((cellNo) => {
                                                    return (
                                                        <SequenceSquare
                                                            key={"sequence-square-column-" + columnIndex + "-sequence-" + cellNo}
                                                            cellNo={cellNo}
                                                            index={columnIndex}
                                                            section={"columnSequences"}
                                                            initialValue={sequences[cellNo]}
                                                        />
                                                    )
                                                })
                                            }
                                        </React.Fragment>
                                    }
                                </div>
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

export default connector(NonogramColumnSequencesSection)