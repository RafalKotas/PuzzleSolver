// react
import React, { useState } from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { correctnessIndicator } from "@store/data/nonogram/types"
import { SetCorrectness } from "store/data/nonogram"
import { 
    nonogramRelatedLogicData, 
    SetNonogramRelatedLogicData, 
    nonogramActionsNames, 
    nonogramBoardMarks, 
    SetCurrentNonogramMark, 
    InitializeSolverData 
} from "store/puzzleLogic/nonogram"

// (sub)component(s)
import ActionVariants from "./ActionVariants/ActionVariants"

// services
import NonogramLogicService from "services/nonogram/nonogram.logic.service"

// mui - components
import { Button, Tab, Tabs, Tooltip } from "@mui/material"

// fortawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

// others
import { actionsProps } from "CommonComponents/AppContent/PuzzleView/NonogramView/SolverView/NonogramActions/ActionsSelect/solverActions"

// styles
import "./SolverActionsPanel.css"

const mapStateToProps = (state: AppState) => ({
    selectedNonogramName: state.nonogramDataReducer.selectedNonogram?.filename ?? "",
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    correctIndicator: state.nonogramDataReducer.nonogramCorrect,
    nonogramRelatedLogicData: state.nonogramLogicReducer.nonogramRelatedData
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCorrectness: (correct: correctnessIndicator) =>
        dispatch(SetCorrectness(correct)),
    setCurrentNonogramMark: (mark: nonogramBoardMarks) =>
        dispatch(SetCurrentNonogramMark(mark)),
    setNonogramRelatedLogicData: (data: nonogramRelatedLogicData) =>
        dispatch(SetNonogramRelatedLogicData(data)),
    initializeSolverData: (rows: number[][], cols: number[][]) =>
        dispatch(InitializeSolverData(rows, cols))
})

const connector = connect(mapStateToProps, mapDispatchToProps)
type Props = ConnectedProps<typeof connector>

const SolverActionsPanel: React.FC<Props> = ({
    selectedNonogramName, correctIndicator, nonogramRelatedLogicData,
    setCurrentNonogramMark, setNonogramRelatedLogicData
}) => {
    const [selectedActionTypeIdx, setSelectedActionTypeIdx] = useState<number>(0)
    const [order, setOrder] = useState<"ROW" | "COLUMN">("ROW")
    const [rowsRange, setRowsRange] = useState<number[]>([0, 0])
    const [columnsRange, setColumnsRange] = useState<number[]>([0, 0])

    const onActionTabChange = (_event: React.SyntheticEvent, value: number) => {
        setSelectedActionTypeIdx(value)
    }

    const handleRangeChange = (updated: number[]) => {
        order === "ROW" ? setRowsRange(updated) : setColumnsRange(updated)
    }

    const handleOrderChange = (updated: string) => {
        if (updated === "ROW" || updated === "COLUMN") {
            setOrder(updated)
        }
    }

    const isLogicDataValid = (): boolean => {
        const { rowSequencesLengths, columnSequencesLengths } = nonogramRelatedLogicData?.nonogramRules || {}
        const board = nonogramRelatedLogicData?.nonogramSolutionBoard

        return (
            Array.isArray(rowSequencesLengths) &&
            rowSequencesLengths.length > 0 &&
            Array.isArray(columnSequencesLengths) &&
            columnSequencesLengths.length > 0 &&
            Array.isArray(board) &&
            board.length > 0
        )
    }

    const dispatchSelectedAction = (name: nonogramActionsNames) => {
        const data = nonogramRelatedLogicData
        if (!data) return

        const update = (promise: Promise<any>) =>
            promise.then((res) => setNonogramRelatedLogicData(res.data)).catch(console.error)

        const withOrder = (rowFn: Function, colFn: Function) =>
            order === "ROW"
                ? update(rowFn(data, rowsRange[0], rowsRange[1]))
                : update(colFn(data, columnsRange[0], columnsRange[1]))

        switch (name) {
            case "COLOUR":
                return withOrder(NonogramLogicService.colourFieldsInRowsRange, NonogramLogicService.colourFieldsInColumnsRange)
            case "PLACE_X":
                return withOrder(NonogramLogicService.placeXinRowsRange, NonogramLogicService.placeXinColumnsRange)
            case "MARK":
                return withOrder(NonogramLogicService.markFieldsInRowsRange, NonogramLogicService.markFieldsInColumnsRange)
            case "CORRECT RANGES":
                return withOrder(NonogramLogicService.correctRowsRanges, NonogramLogicService.correctColumnsRanges)
            case "CUSTOM SOLVER":
                return update(NonogramLogicService.testCustomSolution(data, selectedNonogramName))
            case "COMPARE WITH SOLUTION":
                return update(NonogramLogicService.compareWithSolution(data, selectedNonogramName))
            case "SAVE SOLUTION":
                return NonogramLogicService.saveSolution(data, selectedNonogramName)
                    .then((res) => {
                        const result = res.data;
                        if (result.verifiedAgainstOriginal === "PASS") {
                            console.log("Correct solution saved to file %s", selectedNonogramName);
                        } else {
                            console.warn("Solution incorrect. Can't save.");
                        }
                    })
                    .catch((err) => {
                        console.error("Something went wrong during saving solution:", err);
                    });
            default:
                return
            }
    }


    return (
        <div id="nonogram-actions-select">
            <div id="nonogram-actions-icons">
                <Tabs
                    value={selectedActionTypeIdx}
                    onChange={onActionTabChange}
                    centered
                    sx={{ maxWidth: "300px", padding: "5px" }}
                    TabIndicatorProps={{ style: { backgroundColor: "transparent" } }}
                >
                    {actionsProps.map(({ icon, name, mark }) => (
                        <Tooltip key={name} title={name.toUpperCase()} placement="top">
                            <Tab
                                label={<FontAwesomeIcon icon={icon} />}
                                onClick={() => setCurrentNonogramMark(mark)}
                                disabled={!correctIndicator}
                                sx={{
                                    display: "flex", width: "100px",
                                    flexDirection: "column", flexWrap: "wrap"
                                }}
                            />
                        </Tooltip>
                    ))}
                </Tabs>

                <Button
                    variant="contained"
                    onClick={() => dispatchSelectedAction(actionsProps[selectedActionTypeIdx].name)}
                    disabled={!isLogicDataValid()}
                >
                    {actionsProps[selectedActionTypeIdx].name}
                </Button>
                <br />
            </div>

            <section id="nonogram-actions-choose-section">
                <ActionVariants
                    selectedActionName={actionsProps[selectedActionTypeIdx].name}
                    order={order}
                    rowsRange={rowsRange}
                    columnsRange={columnsRange}
                    passRangeToParent={handleRangeChange}
                    passOrderToParent={handleOrderChange}
                />
            </section>
        </div>
    )
}

export default connector(SolverActionsPanel)
