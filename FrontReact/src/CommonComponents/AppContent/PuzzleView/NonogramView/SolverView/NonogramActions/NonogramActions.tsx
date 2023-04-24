// react
import React from "react"
import { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { maxCellSize, minCellSize, SetCellSize } from "../../../../../../store/layout/nonogram"
import { correctnessIndicator } from "../../../../../../store/data/nonogram/types"
import { SetCorrectness, SetEditMode, ToggleMarksVisibility, ToggleXsVisibility } from "../../../../../../store/data/nonogram"

// libraries-components
import TextField from "@material-ui/core/TextField"
import { Tab, Tabs, ToggleButton, ToggleButtonGroup, Typography } from "@mui/material"
import CheckCorrectButton from "./CheckCorrectButton/CheckCorrectButton"

// (sub) components
import SolverActionsPanel from "./ActionsSelect/SolverActionsPanel"

// other functions
import commonFunctions from "../../../../../../functions"

// styles
import "./NonogramActions.css"

interface OwnNonogramActionsProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    mode: state.displayReducer.mode,
    correctIndicator: state.nonogramDataReducer.nonogramCorrect,
    marksVisible: state.nonogramDataReducer.marksVisible,
    xsVisible: state.nonogramDataReducer.xsVisible,

    cellSize: state.nonogramLayoutReducer.cellSize
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCorrectness: (correct: correctnessIndicator) => 
        dispatch(SetCorrectness(correct)),
    setCellSize: (newCellSize: number) =>
        dispatch(SetCellSize(newCellSize)),
    turnOffEditMode: () => dispatch(SetEditMode(false)),
    toggleMarksVisibility: (marksVisible : boolean) => dispatch(ToggleMarksVisibility(marksVisible)),
    toggleXsVisibility: (xsVisible : boolean) => dispatch(ToggleXsVisibility(xsVisible))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramActionsPropsFromRedux = ConnectedProps<typeof connector>

type NonogramActionsProps = NonogramActionsPropsFromRedux & OwnNonogramActionsProps

const NonogramActions : React.FC<NonogramActionsProps> = ({marksVisible, xsVisible, cellSize, setCellSize,
    selectedNonogram, setCorrectness, correctIndicator, turnOffEditMode, toggleMarksVisibility, toggleXsVisibility}) => {

    type nonogramsActionsTabs = "layout" | "solver"

    const [currentTab, setCurrentTab] = useState<nonogramsActionsTabs>("layout")

    useEffect(() => {
        turnOffEditMode()
        //eslint-disable-next-line
    }, [])

    useEffect(() => {
        setCorrectness(0)
        setCurrentTab("layout")

        //eslint-disable-next-line
    }, [selectedNonogram])

    const onActionTabChange = (event: React.SyntheticEvent<Element, Event>, value: nonogramsActionsTabs) => {
        setCurrentTab(value)
    }

    const changeCellSize = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        let newCellSize = parseInt(event.target.value)
        if(commonFunctions.isValueInRange(newCellSize, [minCellSize, maxCellSize])) {
            setCellSize(newCellSize)
        }
    }

    const handleMarksVisibiltyChange = (event: React.MouseEvent<HTMLElement>, visibility: string) => {
        if(visibility === "VISIBLE") {
            toggleMarksVisibility(true)
        } else {
            toggleMarksVisibility(false)
        }
    }

    const handleXsVisibiltyChange = (event: React.MouseEvent<HTMLElement>, visibility: string) => {
        if(visibility === "VISIBLE") {
            toggleXsVisibility(true)
        } else {
            toggleXsVisibility(false)
        }
    }

    return (<div id="nonogram-actions">
        <Tabs value={currentTab} 
            onChange={(event: React.SyntheticEvent<Element, Event>, value: nonogramsActionsTabs) => onActionTabChange(event, value)} 
            centered

            sx={{
                "& button": { marginX: 1, borderRadius: 2, color: "green !important", border: "1px solid black" },
                "& button:hover": { backgroundColor: "#e5e59a" },
                "& button:focus": {backgroundColor: "#99fb84"}
            }}
        >
            <Tab 
                label={"layout"}
                value={"layout"}
            />
            <Tab 
                label={"solver"}
                value={"solver"}
                disabled={correctIndicator !== 1}
            />
        </Tabs>
        {currentTab === "layout" && 
            <React.Fragment>
                <TextField
                    type="number"
                    name="cellSize"
                    label="Cell Size"
                    variant="outlined"
                    value={cellSize}
                    style={{ marginTop: 15 }}
                    onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => changeCellSize(event)}
                />
                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                    Sequence marks visibility:
                </Typography>

                <ToggleButtonGroup
                    orientation="horizontal"
                    value={marksVisible ? "VISIBLE" : "HIDDEN"}
                    exclusive
                    onChange={handleMarksVisibiltyChange}
                >
                    <ToggleButton value="VISIBLE" aria-label="VISIBLE">
                        VISIBLE
                    </ToggleButton>
                    <ToggleButton value="HIDDEN" aria-label="HIDDEN">
                        HIDDEN
                    </ToggleButton>
                </ToggleButtonGroup>

                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                    "X"s visibility:
                </Typography>

                <ToggleButtonGroup
                    orientation="horizontal"
                    value={xsVisible ? "VISIBLE" : "HIDDEN"}
                    exclusive
                    onChange={handleXsVisibiltyChange}
                >
                    <ToggleButton value="VISIBLE" aria-label="VISIBLE">
                        VISIBLE
                    </ToggleButton>
                    <ToggleButton value="HIDDEN" aria-label="HIDDEN">
                        HIDDEN
                    </ToggleButton>
                </ToggleButtonGroup>

                <CheckCorrectButton/>
            </React.Fragment>
        }
        {
            currentTab === "solver" && <SolverActionsPanel/>
        }
    </div>)
}

export default connect(mapStateToProps, mapDispatchToProps)(NonogramActions)