//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../store"
import { calculateBoardDimensionInPx } from "../../../../../../store/layout/nonogram"

// (sub) components
import BoardBigRow from "./BoardBigRow"

//styles
import "./NonogramBoard.css"

interface OwnNonogramBoardProps {

}


const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,

    cellSize: state.nonogramLayoutReducer.cellSize,
    cellBorder: state.nonogramLayoutReducer.cellBorder,
    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,

    sectionHeight: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "height"),
    sectionWidth: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "width")
})

const connector = connect(mapStateToProps)

type NonogramBoardPropsFromRedux = ConnectedProps<typeof connector>

type NonogramBoardProps = NonogramBoardPropsFromRedux & OwnNonogramBoardProps

const NonogramBoard : React.FC<NonogramBoardProps> = ({selectedNonogram, sectionWidth, sectionHeight}) => {

    if(selectedNonogram) {
        return (
            <div
                id="nonogram-board" 
                style={{
                    width: sectionWidth + "px",
                    height: sectionHeight + "px"
                }}
            >
                {
                    //draw board step 5-rows
                    Array.from(Array(selectedNonogram.height).keys()).reduce((prevValue, curValue) => {
                        if(curValue % 5 === 0) {
                            return [...prevValue, curValue]
                        } else {
                            return prevValue
                        }
                    }, [] as number[]).map((rowNo : number) => {
                        return <BoardBigRow
                            key={"board-big-row-" + rowNo} 
                            rowNo={rowNo}
                        />
                    })
                }
            </div>
    )
    } else {
        return <div>
            
        </div>
    }
}

export default connector(NonogramBoard)