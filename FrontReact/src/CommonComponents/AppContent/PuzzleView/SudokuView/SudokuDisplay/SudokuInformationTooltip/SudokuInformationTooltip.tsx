// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"

//fontawesome
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

// (sub) components
import InfoCardTooltip from "../../../../../InfoCardTooltip/InfoCardTooltip"

interface OwnSudokuInformationTooltipProps {
    placement: "bottom" | "left" | "right" | "top" | "bottom-end" | "bottom-start" | "left-end" | "left-start" | "right-end" | "right-start" | "top-end" | "top-start" | undefined
}

const mapStateToProps = (state: AppState) => ({
    selectedSudoku: state.sudokuDataReducer.selectedSudoku
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuInformationTooltipPropsFromRedux = ConnectedProps<typeof connector>

type SudokuInformationTooltipProps = SudokuInformationTooltipPropsFromRedux & OwnSudokuInformationTooltipProps

const SudokuInformationTooltip : React.FC<SudokuInformationTooltipProps> = ({selectedSudoku, placement}) => {
    
    return (<InfoCardTooltip
        placement={placement}
        title={
            <React.Fragment>
                <b>{selectedSudoku.label}</b> <br />
                Source: {selectedSudoku.source} <br />
                Difficulty: {selectedSudoku.difficulty}
            </React.Fragment>
        }
    >
        <div style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center"
        }}>
            <FontAwesomeIcon icon={faCircleInfo} style={{
                fontSize: "15px",
                cursor: "help",
                height: "20px",
                width: "20px"
            }} />
        </div>
    </InfoCardTooltip>)
}

export default connector(SudokuInformationTooltip)