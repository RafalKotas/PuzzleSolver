//react
import React, { useEffect, useState } from "react"

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
import { actionsProps } from "./solverActions"

// css
import "./ActionsSelect.css"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { InitializeSolverData, nonogramBoardMarks, ResetLogicDataInitialized, SetCurrentNonogramMark } from "../../../../../../../store/puzzleLogic/nonogram"

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

interface OwnSolverActionsPanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    correctIndicator: state.nonogramDataReducer.nonogramCorrect,
    logicDataInitialized: state.nonogramLogicReducer.logicDataInitialized
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCorrectness: (correct: correctnessIndicator) =>
        dispatch(SetCorrectness(correct)),
    setCurrentNonogramMark: (mark : nonogramBoardMarks) =>
        dispatch(SetCurrentNonogramMark(mark)),
    initializeSolverData: (rowsSequences: number[][], columnsSequences: number[][]) => dispatch(InitializeSolverData(rowsSequences, columnsSequences)),
    resetLogicDataInitializationMark: () => dispatch(ResetLogicDataInitialized())
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SolverActionsPanelPropsFromRedux = ConnectedProps<typeof connector>

type SolverActionsPanelProps = SolverActionsPanelPropsFromRedux & OwnSolverActionsPanelProps

const SolverActionsPanel: React.FC<SolverActionsPanelProps> = ({selectedNonogram, correctIndicator, logicDataInitialized, 
    setCurrentNonogramMark, initializeSolverData, resetLogicDataInitializationMark }) => {

    const classes = useStyles()

    const [selectedActionTypeIdx, setSelectedActionTypeIdx] = useState<number>(0)

    const onActionTabChange = (event: React.SyntheticEvent<Element, Event>, value: number) => {
        setSelectedActionTypeIdx(value)
    }

    const initializeNonogramLogicData = () => {
        if(selectedNonogram) {
            let { rowSequences, columnSequences } = selectedNonogram
            initializeSolverData(rowSequences, columnSequences)
        }
    }

    useEffect(() => {
        resetLogicDataInitializationMark()
        //eslint-disable-next-line
    }, [])
    
    return (
        <div id="nonogram-actions-select">
            {!logicDataInitialized && <Button variant="contained" onClick={() => initializeNonogramLogicData()}>INITIALIZE DATA</Button>}
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
            <br></br>
            <ActionVariants selectedActionName={actionsProps[selectedActionTypeIdx].name}/>
        </div>
    )
}

export default connector(SolverActionsPanel)