// mui
import { Button, FormControlLabel, Grid, Radio, RadioGroup, TextField } from "@mui/material"
import { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { AddColumns, AddRows, RemoveColumns, RemoveRows, 
    selectColumnsSequencesLength, selectRowsSequencesLength } from "../../../../../../store/data/nonogram"

// (sub) components
import SequencesModifier from "./SequencesModifier/SequencesModifier"

// styles
import "./EditNonogramActions.css"

// functions
import commonFunctions from "../../../../../../functions"

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    maxRowAddIndex: selectRowsSequencesLength(state.nonogramDataReducer),
    maxColumnAddIndex: selectColumnsSequencesLength(state.nonogramDataReducer),
    maxRowRemoveIndex: selectRowsSequencesLength(state.nonogramDataReducer) - 1,
    maxColumnRemoveIndex: selectColumnsSequencesLength(state.nonogramDataReducer) - 1
})

const mapDispatchToProps = (dispatch : Dispatch) => ({
    addRows: (startRowIdx: number, endRowIdx: number) => dispatch(AddRows(startRowIdx, endRowIdx)),
    removeRows: (startRowIdx: number, endRowIdx: number) => dispatch(RemoveRows(startRowIdx, endRowIdx)),
    addColumns: (startColumnIdx: number, endColumnIdx: number) => dispatch(AddColumns(startColumnIdx, endColumnIdx)),
    removeColumns: (startColumnIdx: number, endColumnIdx: number) => dispatch(RemoveColumns(startColumnIdx, endColumnIdx))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type EditNonogramActionsPropsFromRedux = ConnectedProps<typeof connector>

type EditNonogramActionsProps = EditNonogramActionsPropsFromRedux

const EditNonogramActions : React.FC<EditNonogramActionsProps> = ({maxRowAddIndex, maxColumnAddIndex, maxRowRemoveIndex, maxColumnRemoveIndex,
    addRows, removeRows, addColumns, removeColumns}) => {

    const [rowStartIndex, setRowStartIndex] = useState<number>(0)
    const [rowEndIndex, setRowEndIndex] = useState<number>(maxRowRemoveIndex)
    const [columnStartIndex, setColumnStartIndex] = useState<number>(0)
    const [columnEndIndex, setColumnEndIndex] = useState<number>(maxColumnRemoveIndex)

    const [action, setAction] = useState("add row(s)")

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let updatedActionValue = event.target.value

        setAction(updatedActionValue)
    }

    const onDispatchActionButtonClick = () => {
        switch(action) {
            case "add row(s)":
                addRows(rowStartIndex, rowEndIndex)
                break
            case "remove row(s)":
                removeRows(rowStartIndex, rowEndIndex)
                break
            case "add column(s)":
                addColumns(columnStartIndex, columnEndIndex)
                break
            case "remove column(s)":
                removeColumns(columnStartIndex, columnEndIndex)
                break
        }
    }

    useEffect(() => {
        switch(action) {
            case "remove row(s)":
                if(rowEndIndex > maxRowRemoveIndex) {
                    setRowEndIndex(maxRowRemoveIndex)
                }
                if(rowStartIndex > maxRowRemoveIndex) {
                    setRowStartIndex(0)
                }
                break
            case "remove column(s)":
                if(rowEndIndex > maxColumnRemoveIndex) {
                    setColumnEndIndex(maxColumnRemoveIndex)
                }
                if(columnStartIndex > maxColumnRemoveIndex) {
                    setColumnStartIndex(0)
                }
                break
            default:
                break
        }
        //eslint-disable-next-line
    }, [action])

    interface modifyBoardAction {
        value: string,
        label: string,
        //action: (startIndex: number, endIndex : number) => void
    }

    const modifyBoardActions : modifyBoardAction[] = [
        {
            value: "add row(s)",
            label: "Add row(s)"
        },
        {
            value: "remove row(s)",
            label: "Remove row(s)"
        },
        {
            value: "add column(s)",
            label: "Add column(s)"
        },
        {
            value: "remove column(s)",
            label: "Remove column(s)"
        }
    ]

    const modifyBoardActionsValues = modifyBoardActions.map((action) => {return action.value})
    const rowsActions = modifyBoardActions.filter((action) => {return action.value.includes("row")}).map((action) => {return action.value})
    const addActions = modifyBoardActions.filter((action) => {return action.value.includes("add")}).map((action) => {return action.value})

    const changeStartIndex = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        let updatedIndex = parseInt(event.target.value)
        if(rowsActions.includes(action) && updatedIndex <= rowEndIndex && commonFunctions.isValueInRange(updatedIndex, [0, maxRowAddIndex])) {
            setRowStartIndex(updatedIndex)
        } else if (updatedIndex <= columnEndIndex && commonFunctions.isValueInRange(updatedIndex, [0, maxColumnAddIndex])) {
            setColumnStartIndex(updatedIndex)
        }
    }

    const changeEndIndex = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        let updatedIndex = parseInt(event.target.value)
        if(rowsActions.includes(action) && updatedIndex >= rowStartIndex) {
            if(addActions.includes(action) && commonFunctions.isValueInRange(updatedIndex, [0, maxRowAddIndex])) {
                setRowEndIndex(updatedIndex)
            } else if (commonFunctions.isValueInRange(updatedIndex, [0, maxRowRemoveIndex])) {
                setRowEndIndex(updatedIndex)
            }
        } else if (updatedIndex >= columnStartIndex) {
            if(addActions.includes(action) && commonFunctions.isValueInRange(updatedIndex, [0, maxColumnAddIndex])) {
                setColumnEndIndex(updatedIndex)
            } else if (commonFunctions.isValueInRange(updatedIndex, [0, maxColumnRemoveIndex])) {
                setColumnEndIndex(updatedIndex)
            }
        }
    }

    return (
        <section id="edit-nonogram-actions">
            <h2>ACTIONS (SELECT ONE)</h2>
            <RadioGroup
                aria-labelledby="demo-radio-buttons-group-label"
                name="nonogram-edit-actions"
                value={action}
                onChange={handleChange}
            >
                {
                    modifyBoardActions.map((modifiyBoardAction) => {
                        let {value, label} = modifiyBoardAction
                        return (
                            <FormControlLabel value={value} control={<Radio />} label={label} />            
                        )
                    })
                }
            </RadioGroup>
            <Grid item xs={3}>
                <TextField
                    style={{width: "120px", marginTop: "5px"}}
                    label="Start Index"
                    disabled={!modifyBoardActionsValues.includes(action)}
                    type="number"
                    value={rowsActions.includes(action) ? rowStartIndex : columnStartIndex}
                    onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => changeStartIndex(event)}
                />
                <TextField 
                    style={{width: "120px", marginTop: "5px"}}
                    label="End Index"
                    disabled={!modifyBoardActionsValues.includes(action)}
                    type="number"
                    value={rowsActions.includes(action) ? rowEndIndex : columnEndIndex}
                    onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => changeEndIndex(event)}
                />
                <Button 
                    variant="contained"
                    color={"primary"}
                    style={{marginLeft: 10, marginRight: 10, marginTop: 15}}
                    onClick={onDispatchActionButtonClick}>
                    MAKE IT
                </Button>
            </Grid>
            <div style={{height: "5px", width: "100%", borderBottom: "1px solid black"}}></div>
            <SequencesModifier/>
        </section>
    )
}

export default connector(EditNonogramActions)