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
import { nonogramSolverActionsNames, nonogramActionsNames } from "../solverActions"
import CustomMUISlider from "../../../../../../PuzzleFiltersPanel/CustomMUISlider/CustomMUISlider"
import { ColourFieldsInColumnsRange, nonogramRelatedLogicData, SetNonogramLogicData } from "../../../../../../../../store/puzzleLogic/nonogram"

interface OwnNonogramActionVariantsProps {
    selectedActionName: nonogramActionsNames,
    order: string,
    rowsRange: number[],
    columnsRange: number[],
    passRangeToParent: (value: Array<number>) => void,
    passOrderToParent: (value: string) => void
}

const mapStateToProps = (state: AppState) => ({
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

const ActionVariants: React.FC<NonogramActionVariantsProps> = ({selectedActionName, order, passRangeToParent, passOrderToParent, maxRow, maxColumn}) => {

    return (
        <div id="nonogram-action-variants">
            {
                nonogramSolverActionsNames.includes(selectedActionName) && <React.Fragment>
                    <section id="adjust-row-column-range-section">
                        <Tabs value={order === "ROW" ? 0 : 1}
                            centered
                        >
                            <Tab
                                label={"ROW"}
                                onClick={() => passOrderToParent("ROW")}
                                disabled={false}
                            />
                            <Tab
                                label={"COLUMN"}
                                onClick={() => passOrderToParent("COLUMN")}
                                disabled={false}
                            />
                        </Tabs>
                        {order === "ROW" && <CustomMUISlider minValue={0} maxValue={maxRow} step={1} filterLabel={"Row"} onHandleChange={passRangeToParent} />}
                        {order === "COLUMN" && <CustomMUISlider minValue={0} maxValue={maxColumn} step={1} filterLabel={"Column"} onHandleChange={passRangeToParent} />}
                    </section>
                </React.Fragment>
            }
        </div>
    )
}

export default connector(ActionVariants)