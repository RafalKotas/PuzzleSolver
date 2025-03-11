//react
import React, { useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { correctnessIndicator } from "../../../../../../../store/data/nonogram/types"
import { SetCorrectness } from "../../../../../../../store/data/nonogram"

// (sub)components
import ActionVariants from "./ActionVariants/ActionVariants"

// material-ui
import { Button, Tab, Tabs, Tooltip } from "@mui/material"
import { makeStyles } from "@material-ui/core"

// others
import { actionsProps, nonogramActionsNames } from "./solverActions"
import NonogramLogicService from "../../../../../../../services/nonogram/nonogram.logic.service"
import { nonogramRelatedLogicData, SetNonogramLogicData } from "../../../../../../../store/puzzleLogic/nonogram"

// css
import "./SolverActionsPanel.css"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { InitializeSolverData, nonogramBoardMarks, SetCurrentNonogramMark } from "../../../../../../../store/puzzleLogic/nonogram"

const useStyles = makeStyles((theme) => ({
    listItemRoot: {
      "&.Mui-selected": {
          backgroundColor: "#721717",
          color: "#7EEAEC",
          borderRadius: "5px"
      }
    },
    tabsContainer: {
        "&.MuiTabs-flexContainer": {
            flexWrap: "wrap"
        }
    }
  }))

// interface OwnSolverActionsPanelProps {

// }

const mapStateToProps = (state: AppState) => ({
    selectedNonogramName: state.nonogramDataReducer.selectedNonogram ? state.nonogramDataReducer.selectedNonogram.filename : "" ,
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    correctIndicator: state.nonogramDataReducer.nonogramCorrect,
    nonogramRelatedLogicData: state.nonogramLogicReducer.nonogramRelatedData
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCorrectness: (correct: correctnessIndicator) =>
        dispatch(SetCorrectness(correct)),
    setCurrentNonogramMark: (mark : nonogramBoardMarks) =>
        dispatch(SetCurrentNonogramMark(mark)),
    setNonogramLogicData: (nonogramLogicData: nonogramRelatedLogicData) =>
            dispatch(SetNonogramLogicData(nonogramLogicData)),
    initializeSolverData: (rowsSequences: number[][], columnsSequences: number[][]) => dispatch(InitializeSolverData(rowsSequences, columnsSequences))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SolverActionsPanelPropsFromRedux = ConnectedProps<typeof connector>

type SolverActionsPanelProps = SolverActionsPanelPropsFromRedux//& OwnSolverActionsPanelProps

const SolverActionsPanel: React.FC<SolverActionsPanelProps> = ({selectedNonogramName, correctIndicator, nonogramRelatedLogicData,
    setCurrentNonogramMark, setNonogramLogicData}) => {

    const classes = useStyles()

    const [selectedActionTypeIdx, setSelectedActionTypeIdx] = useState<number>(0)
    const [order, setOrder] = useState<string>("ROW")
    const [rowsRange, setRowsRange] = useState<number[]>([0, 0])
    const [columnsRange, setColumnsRange] = useState<number[]>([0, 0])

    const onActionTabChange = (_event: React.SyntheticEvent<Element, Event>, value: number) => {
        setSelectedActionTypeIdx(value)
    }

    const handleRangeChange = (updatedRange: Array<number>) => {
        if (order === "ROW") {
            setRowsRange(updatedRange)
        } else {
            setColumnsRange(updatedRange)
        }
    }

    const handleOrderChange = (updatedOrder: string) => {
        setOrder(updatedOrder)
    }

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
        <div id="nonogram-actions-select">
            <div id="nonogram-actions-icons">
                <Tabs value={selectedActionTypeIdx}
                    classes={{flexContainer: classes.tabsContainer}}
                    onChange={(event: React.SyntheticEvent<Element, Event>, value: number) => onActionTabChange(event, value)}
                    centered
                    sx={{maxWidth: "300px", padding: "5px"}}
                    TabIndicatorProps={{style: {backgroundColor: "transparent"}}}
                >
                    {
                        actionsProps.map((actionProps) => {
                            let {icon, name, mark} = actionProps
                            return  <Tooltip key={"nonogram-action-" + name} title={name.toUpperCase()} placement="top">
                                        <Tab
                                                classes={{root: classes.listItemRoot}}
                                                label={<FontAwesomeIcon icon={icon}/>}
                                                onClick={() => setCurrentNonogramMark(mark)}
                                                disabled={!correctIndicator}
                                                sx={{display: "flex", width: "100px", flexDirection: "column", flexWrap: "wrap"}}
                                        />
                                    </Tooltip>
                            })
                        }
                </Tabs>
                {
                    <Button  variant="contained" onClick={() => dispatchSelectedAction(actionsProps[selectedActionTypeIdx].name)}>
                        {actionsProps[selectedActionTypeIdx].name}
                    </Button>
                }
                <br></br>
            </div>
            <section id="nonogram-actions-choose-section">
                <ActionVariants selectedActionName={actionsProps[selectedActionTypeIdx].name}
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