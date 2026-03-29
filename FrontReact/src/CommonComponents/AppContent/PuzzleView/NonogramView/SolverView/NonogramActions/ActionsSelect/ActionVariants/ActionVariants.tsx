// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../../store"

// mui/material
import { Tab, Tabs } from "@mui/material"
/*import { makeStyles } from "@mui/material/core"*/

// others
import CustomMUISlider from "../../../../../../PuzzleFiltersPanel/CustomMUISlider/CustomMUISlider"
import { nonogramSolverActionsNames, nonogramActionsNames, nonogramRelatedLogicData,
    ColourFieldsInColumnsRange, SetNonogramRelatedLogicData } from "../../../../../../../../store/puzzleLogic/nonogram"

interface OwnNonogramActionVariantsProps {
    selectedActionName: nonogramActionsNames,
    order: string,
    rowsRange: number[],
    columnsRange: number[],
    passRangeToParent: (value: Array<number>) => void,
    passOrderToParent: (value: string) => void
}

const mapStateToProps = (state: AppState) => {
    const rules = state.nonogramLogicReducer.nonogramRelatedData?.nonogramRules;
    return {
        maxRow: rules?.height ?? 0,
        maxColumn: rules?.width ?? 0,
        nonogramRelatedLogicData: state.nonogramLogicReducer.nonogramRelatedData
    };
};


const mapDispatchToProps = (dispatch: Dispatch) => ({
    colourFieldsInColumnsRange: (columnBegin: number, columnEnd: number) =>
        dispatch(ColourFieldsInColumnsRange(columnBegin, columnEnd)),
    setNonogramRelatedLogicData: (nonogramRelatedLogicData: nonogramRelatedLogicData) =>
        dispatch(SetNonogramRelatedLogicData(nonogramRelatedLogicData))
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
                        {order === "ROW" && maxRow > 0 && (
                            <CustomMUISlider
                                minValue={0}
                                maxValue={maxRow}
                                step={1}
                                filterLabel={"Row"}
                                onHandleChange={passRangeToParent}
                            />
                        )}

                        {order === "COLUMN" && maxColumn > 0 && (
                            <CustomMUISlider
                                minValue={0}
                                maxValue={maxColumn}
                                step={1}
                                filterLabel={"Column"}
                                onHandleChange={passRangeToParent}
                            />
                        )}
                    </section>
                </React.Fragment>
            }
        </div>
    )
}

export default connector(ActionVariants)