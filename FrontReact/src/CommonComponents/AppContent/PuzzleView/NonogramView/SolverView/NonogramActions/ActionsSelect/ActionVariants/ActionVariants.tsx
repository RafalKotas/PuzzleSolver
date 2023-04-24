// react
import React, { useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../../store"

// material-ui
import { Button, Tab, Tabs } from "@mui/material"
/*import { makeStyles } from "@material-ui/core"*/

// others
import { nonogramActionsNames } from "../solverActions"
import CustomMUISlider from "../../../../../../PuzzleFiltersPanel/CustomMUISlider/CustomMUISlider"
import { ColourFieldsInColumnsRange, nonogramRelatedLogicData, SetNonogramLogicData } from "../../../../../../../../store/puzzleLogic/nonogram"
import NonogramLogicService from "../../../../../../../../services/nonogram/nonogram.logic.service"

interface OwnNonogramActionVariantsProps {
    selectedActionName: nonogramActionsNames,
}

const mapStateToProps = (state: AppState) => ({
    selectedNonogramName: state.nonogramDataReducer.selectedNonogram ? state.nonogramDataReducer.selectedNonogram.filename : "" ,
    maxRow: state.nonogramLogicReducer.nonogramRelatedData.rowsSequences.length,
    maxColumn: state.nonogramLogicReducer.nonogramRelatedData.columnsSequences.length,
    nonogramRelatedLogicData: state.nonogramLogicReducer.nonogramRelatedData
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    colourFieldsInColumnsRange: (columnBegin: number, columnEnd: number) =>
        dispatch(ColourFieldsInColumnsRange(columnBegin, columnEnd)),
    setNonogramLogicData: (nonogramLogicData: nonogramRelatedLogicData) =>
        dispatch(SetNonogramLogicData(nonogramLogicData))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramActionVariantsPropsFromRedux = ConnectedProps<typeof connector>

type NonogramActionVariantsProps = NonogramActionVariantsPropsFromRedux & OwnNonogramActionVariantsProps

const ActionVariants: React.FC<NonogramActionVariantsProps> = ({selectedNonogramName, maxRow, maxColumn, nonogramRelatedLogicData, selectedActionName,
    setNonogramLogicData }) => {

    const [order, setOrder] = useState<string>("ROW")
    const [rowsRange, setRowsRange] = useState<number[]>([0, 0])
    const [columnsRange, setColumnsRange] = useState<number[]>([0, 0])

    const updateRows = (rowsRange: Array<number>) => {
        setRowsRange(rowsRange)
    }

    const updateColumns = (columnsRange: Array<number>) => {
        setColumnsRange(columnsRange)
    }

    const dispatchSelectedAction = (name: nonogramActionsNames) => {
        switch (name) {
            case "COLOUR":
                switch (order) {
                    case "ROW":
                        NonogramLogicService.colourFieldsInRowsRange(nonogramRelatedLogicData, rowsRange[0], rowsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                    case "COLUMN":
                        NonogramLogicService.colourFieldsInColumnsRange(nonogramRelatedLogicData, columnsRange[0], columnsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                }
                break
            case "PLACE_X":
                switch (order) {
                    case "ROW":
                        NonogramLogicService.placeXinRowsRange(nonogramRelatedLogicData, rowsRange[0], rowsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                    case "COLUMN":
                        NonogramLogicService.placeXinColumnsRange(nonogramRelatedLogicData, columnsRange[0], columnsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                    default:
                        break
                }
                break
            case "MARK":
                switch (order) {
                    case "ROW":
                        NonogramLogicService.markFieldsInRowsRange(nonogramRelatedLogicData, rowsRange[0], rowsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                    case "COLUMN":
                        NonogramLogicService.markFieldsInColumnsRange(nonogramRelatedLogicData, columnsRange[0], columnsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                }
                break
            case "CUSTOM SOLVER":
                NonogramLogicService.testCustomSolution(nonogramRelatedLogicData, selectedNonogramName)
                .then((response) => {
                    setNonogramLogicData(response.data)
                }).catch(() => {

                })
                break
            case "SAVE SOLUTION":
                NonogramLogicService.saveSolution(nonogramRelatedLogicData, selectedNonogramName)
                .then((response) => {

                }).catch(() => {

                })
                break
            case "COMPARE WITH SOLUTION":
                NonogramLogicService.compareWithSolution(nonogramRelatedLogicData, selectedNonogramName)
                .then((response) => {
                    setNonogramLogicData(response.data)
                }).catch(() => {

                })
                break
            case "CORRECT RANGES":
                switch (order) {
                    case "ROW":
                        NonogramLogicService.correctRowsRanges(nonogramRelatedLogicData, rowsRange[0], rowsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                    case "COLUMN":
                        NonogramLogicService.correctColumnsRanges(nonogramRelatedLogicData, columnsRange[0], columnsRange[1])
                            .then((response) => {
                                setNonogramLogicData(response.data)
                            }).catch(() => {

                            })
                        break
                }
                break
            default:
                break
        }
    }

    return (
        <div>
            {
                <Button variant="contained" onClick={() => dispatchSelectedAction(selectedActionName)}>
                    {selectedActionName}
                </Button>
            }
            <Tabs value={order === "ROW" ? 0 : 1}
                centered
            >
                <Tab
                    label={"ROW"}
                    onClick={() => setOrder("ROW")}
                    disabled={false}
                />
                <Tab
                    label={"COLUMN"}
                    onClick={() => setOrder("COLUMN")}
                    disabled={false}
                />
            </Tabs>
            {order === "ROW" && <CustomMUISlider minValue={0} maxValue={maxRow} step={1} filterLabel={"Row"} onHandleChange={updateRows} />}
            {order === "COLUMN" && <CustomMUISlider minValue={0} maxValue={maxColumn} step={1} filterLabel={"Column"} onHandleChange={updateColumns} />}
        </div>
    )
}

export default connector(ActionVariants)