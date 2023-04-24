// react
import React from "react"

// (sub) components

import SudokuInformationTooltip from "./SudokuInformationTooltip/SudokuInformationTooltip"

// styles
import "./SudokuDisplay.css"
import SudokuBoard from "./SudokuBoard/SudokuBoard"

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