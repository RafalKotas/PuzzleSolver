// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { ChangeColumnEndConsideredIndex, ChangeColumnStartConsideredIndex, ChangeRowEndConsideredIndex, 
    ChangeRowStartConsideredIndex, FillMarkedCells, MarkAction,
     ResetMarkedCells, SetRowsAndColumnsRanges } from "../../../../../../store/puzzleLogic/sudoku"

// mui
import { Button, Divider, FormControl, FormControlLabel, FormLabel, List, ListItemButton, ListItemText, Radio, RadioGroup, TextField } from "@mui/material"

// (sub) components

// styles
import "./SudokuActions.css"

const mapStateToProps = (state: AppState) => ({
    consideredRows: state.sudokuLogicReducer.consideredRows,
    consideredColumns: state.sudokuLogicReducer.consideredColumns
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    changeRowStartConsideredIndex: (updatedIndex: number) => dispatch(ChangeRowStartConsideredIndex(updatedIndex)),
    changeRowEndConsideredIndex: (updatedIndex: number) => dispatch(ChangeRowEndConsideredIndex(updatedIndex)),
    changeColumnStartConsideredIndex: (updatedIndex: number) => dispatch(ChangeColumnStartConsideredIndex(updatedIndex)),
    changeColumnEndConsideredIndex: (updatedIndex: number) => dispatch(ChangeColumnEndConsideredIndex(updatedIndex)),
    
    markCellsWithOnlyOnePossibleOption: (rowsRange: number[], columnsRange : number[]) => 
        dispatch(MarkAction({rowsRange, columnsRange}, "One possible option in cell")),
    markCellsWithOnlyOneAvailablePlaceForDigitInRows: (rowsRange: number[], columnsRange : number[]) => 
        dispatch(MarkAction({rowsRange, columnsRange}, "One available place for digit in row")),
    markCellsWithOnlyOneAvailablePlaceForDigitInColumns : (rowsRange: number[], columnsRange : number[]) => 
        dispatch(MarkAction({rowsRange, columnsRange}, "One available place for digit in column")),
    markCellsWithOnlyOneAvailablePlaceForDigitIn3x3Squares : (rowsRange: number[], columnsRange: number[]) => 
        dispatch(MarkAction({rowsRange, columnsRange}, "One available place for digit in square 3x3")),

    fillMarkedCells: () => dispatch(FillMarkedCells()),

    resetMarkedCells: () => dispatch(ResetMarkedCells()),
 
    disableConsideredColumns : (rowsRange: number[]) => dispatch(SetRowsAndColumnsRanges([0, 0], [-1, -1])),
    disableConsideredRows : (columnsRange: number[]) => dispatch(SetRowsAndColumnsRanges([-1, -1], [0, 0])),
    enableAllRowsAndColumns : () => dispatch(SetRowsAndColumnsRanges([0,8], [0,8]))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuActionsPropsFromRedux = ConnectedProps<typeof connector>

type SudokuActionsProps = SudokuActionsPropsFromRedux

const SudokuActions: React.FC<SudokuActionsProps> = ({ consideredColumns, consideredRows, 
   changeRowStartConsideredIndex, changeRowEndConsideredIndex, changeColumnStartConsideredIndex, changeColumnEndConsideredIndex,
   markCellsWithOnlyOnePossibleOption, markCellsWithOnlyOneAvailablePlaceForDigitInRows, 
   markCellsWithOnlyOneAvailablePlaceForDigitInColumns , markCellsWithOnlyOneAvailablePlaceForDigitIn3x3Squares, fillMarkedCells,
    disableConsideredColumns, disableConsideredRows, enableAllRowsAndColumns, resetMarkedCells}) => {

    const [selectedRange, setSelectedRange] = useState<string>("ROW")
    const [step, setStep] = useState<"mark" | "fill">("mark")

    interface sudokuBasicOption {
        optionName: string,
        color: string,
        onMark: (rowsRange: number[], columnsRange: number[]) => void, // markAll (row by row?)
    }

    const sudokuBasicOptions: sudokuBasicOption[] = [
        {
            optionName: "One possible option in cell",
            color: "#c0f988",
            onMark: markCellsWithOnlyOnePossibleOption
        },
        {
            optionName: "One available place for digit in row",
            color: "#85ea79",
            onMark: markCellsWithOnlyOneAvailablePlaceForDigitInRows
        },
        {
            optionName: "One available place for digit in column",
            color: "#37ef2e",
            onMark: markCellsWithOnlyOneAvailablePlaceForDigitInColumns
        },
        {
            optionName: "One available place for digit in square 3x3",
            color: "#29c231",
            onMark: markCellsWithOnlyOneAvailablePlaceForDigitIn3x3Squares
        }
    ]

    const [selectedOption, setSelectedOption] = useState<sudokuBasicOption>(sudokuBasicOptions[0])

    useEffect(() => {
        resetMarkedCells()
        //eslint-disable-next-line
    }, [selectedOption])

    interface rangeOption {
        optionName: "ROW" | "COLUMN" | "ALL" | "CUSTOM",
        value: number[],
        onSelect: () => void
    }

    const rangeOptions: rangeOption[] = [
        {
            optionName: "ROW",
            value: consideredRows,
            onSelect: () => disableConsideredColumns(consideredRows)
        },
        {
            optionName: "COLUMN",
            value: consideredColumns,
            onSelect: () => disableConsideredRows(consideredColumns)
        },
        {
            optionName: "ALL",
            value: [0, 8],
            onSelect: () => enableAllRowsAndColumns()
        }
    ]

    const sudokuBasicOptionsListStyle = {
        alignItems: "center",
        backgroundColor: "white",
        brangeRadius: "5px"
    }

    const onSudokuActionChange = (event: React.ChangeEvent<HTMLInputElement>, value: string) => {
        let consideredRowsAndColumnsChanger = rangeOptions.filter((rangeOption) => {
            return rangeOption.optionName === value
        })[0].onSelect
        consideredRowsAndColumnsChanger()
        setSelectedRange(value)
    }

    const changeStartConsideredIndex = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, optionName: string) => {
        let newIndex = parseInt(event.target.value)
        switch(optionName) { 
            case "ROW": {
               changeRowStartConsideredIndex(newIndex)
               break
            } 
            case "COLUMN": {
               changeColumnStartConsideredIndex(newIndex)
               break
            } 
            default: {
               break
            } 
        } 
    }

    const changeEndConsideredIndex = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, optionName: string) => {
        let newIndex = parseInt(event.target.value)
        switch(optionName) { 
            case "ROW": {
               changeRowEndConsideredIndex(newIndex)
               break
            } 
            case "COLUMN": {
               changeColumnEndConsideredIndex(newIndex)
               break
            } 
            default: {
               break
            } 
        } 
    }

    return (
        <div id="sudoku-actions">
            <h2>Basic actions</h2>
            {
                <List component="nav" sx={sudokuBasicOptionsListStyle} aria-label="mailbox folders">
                    {
                        sudokuBasicOptions.map((sudokuBasicOption, index) => {
                            let { optionName } = sudokuBasicOption
                            return (
                                <React.Fragment>
                                    <ListItemButton
                                        onClick={() => {
                                            setSelectedOption(sudokuBasicOption)
                                            setStep("mark")
                                        }}
                                        color={"success"}
                                        selected={selectedOption.optionName === optionName}
                                    >
                                        <ListItemText color={"success"} primary={optionName.toLocaleUpperCase()} />
                                    </ListItemButton>
                                    {(index !== sudokuBasicOptions.length - 1) && <Divider />}
                                </React.Fragment>
                            )
                        })
                    }
                </List>
            }
            {
                <FormControl>
                    <FormLabel style={{display: "flex"}} id="demo-radio-buttons-group-label">Select range</FormLabel>
                    <RadioGroup
                        aria-labelledby="demo-radio-buttons-group-label"
                        value={selectedRange}
                        onChange={(event: React.ChangeEvent<HTMLInputElement>, value: string) => onSudokuActionChange(event, value)}
                        name="radio-buttons-group"
                    >
                        {
                            rangeOptions.map((rangeOption) => {
                                let {optionName, value} = rangeOption
                                return <div key={"sudoku-action-range-" + rangeOption.optionName} className={"range-option"}>
                                    <FormControlLabel value={optionName} control={<Radio />} label={optionName} />
                                    <div>
                                        <TextField
                                            onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => 
                                            changeStartConsideredIndex(event, optionName)}
                                            type={"number"} 
                                            disabled={optionName === "ALL"} 
                                            value={value[0]} 
                                            style={{ width: "60px" }} 
                                        />
                                        <TextField
                                            onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => 
                                                changeEndConsideredIndex(event, optionName)}
                                            type={"number"}
                                            disabled={optionName === "ALL"} 
                                            value={value[1]} 
                                            style={{ width: "60px" }} 
                                        />
                                        <Button onClick={() => {
                                            if(step === "mark") {
                                                selectedOption.onMark(consideredRows, consideredColumns)
                                                setStep("fill")
                                            } else {
                                                fillMarkedCells()
                                                setStep("mark")
                                            }
                                            
                                        }} disabled={!(selectedRange === optionName)} variant={"contained"}>
                                            {step === "mark" ?  "MARK CELLS!" : "FILL MARKED!"}
                                        </Button>
                                    </div>
                                </div>
                            })
                        }
                    </RadioGroup>
                </FormControl>
            }
        </div>
    )
}

export default connector(SudokuActions)