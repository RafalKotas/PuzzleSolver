// react
import React from "react"

// (sub)component(s)
import SudokuInformationTooltip from "CommonComponents/AppContent/PuzzleView/SudokuView/SudokuDisplay/SudokuInformationTooltip/SudokuInformationTooltip"
import SudokuBoard from "CommonComponents/AppContent/PuzzleView/SudokuView/SudokuDisplay/SudokuBoard/SudokuBoard"

// styles
import "./SudokuDisplay.css"


const SudokuDisplay = () => {

    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center"
        }}>
            <div style={{
                minHeight: "30px"
            }}>
                <SudokuInformationTooltip placement="top"/>
            </div>
            <SudokuBoard />
        </div>
    )
}

export default SudokuDisplay