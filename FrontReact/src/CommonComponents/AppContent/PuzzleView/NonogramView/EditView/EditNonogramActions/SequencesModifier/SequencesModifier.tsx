// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { ModifyColumn, ModifyRow, selectColumnsSequencesLength, selectRowsSequencesLength } from "../../../../../../../store/data/nonogram"

// mui
import { Button, TextField, ToggleButton, ToggleButtonGroup, Tooltip } from "@mui/material"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faBroom } from "@fortawesome/free-solid-svg-icons"


const mapStateToProps = (state: AppState) => ({
  maxRowModifyIndex: selectRowsSequencesLength(state.nonogramDataReducer) - 1,
  maxColumnModifyIndex: selectColumnsSequencesLength(state.nonogramDataReducer) - 1,
  currentHeight: state.nonogramDataReducer.selectedNonogram ? state.nonogramDataReducer.selectedNonogram.height : null,
  currentWidth: state.nonogramDataReducer.selectedNonogram ? state.nonogramDataReducer.selectedNonogram.width : null
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
  modifyRow: (rowIndex: number, rowSequences: Array<number>) => dispatch(ModifyRow(rowIndex, rowSequences)),
  modifyColumn: (columnIndex: number, columnSequences: Array<number>) => dispatch(ModifyColumn(columnIndex, columnSequences))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SequencesModifierPropsFromRedux = ConnectedProps<typeof connector>

type SequencesModifierProps = SequencesModifierPropsFromRedux

const options = ["row", "column"]

const SequencesModifier: React.FC<SequencesModifierProps> = ({ maxRowModifyIndex, maxColumnModifyIndex, modifyRow, modifyColumn }) => {

  const [selectedOption, setSelectedOption] = useState<string>("row")
  const [modifyIndex, setModifyIndex] = useState<number>(0)
  const [maxModifyIndex, setMaxModifyIndex] = useState<number>(maxRowModifyIndex)
  const [sequences, setSequences] = useState<string>("")
  const [cleanUp, setCleanUp] = useState<boolean>(false)

  const digits = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
  const separators = [" ", ","]
  const allowedKeys = ["Backspace", "ArrowLeft", "ArrowRight"]

  const handleOptionChange = (
    event: React.MouseEvent<HTMLElement>,
    newOption: string,
  ) => {
    setSelectedOption(newOption)
  }

  useEffect(() => {
    let maxIndex = selectedOption === "row" ? maxRowModifyIndex : maxColumnModifyIndex
    setMaxModifyIndex(maxIndex)
    //eslint-disable-next-line
  }, [selectedOption])

  useEffect(() => {
    if (modifyIndex > maxModifyIndex) {
      setModifyIndex(0)
    }
    //eslint-disable-next-line
  }, [maxModifyIndex])

  useEffect(() => {
    
  }, [sequences])

  useEffect(() => {
    console.log("modify index: " + modifyIndex)
  }, [modifyIndex])

  const changeIndexHandler = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    let updatedIndex = Math.min(maxModifyIndex, parseInt(event.target.value))
    console.log("index handler!")
    if (updatedIndex < 0 || updatedIndex > maxModifyIndex) {
      event.preventDefault()
    } else {
      setModifyIndex(updatedIndex)
    }
  }

  const sequencesInputHandler = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (digits.includes(event.key) || separators.includes(event.key) || allowedKeys.includes(event.key)) {
      setCleanUp(false)
    } else {
      event.preventDefault()
    }
  }

  const sequencesChangeHandler = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    let currentSequencesValue = event.target.value
    setSequences(currentSequencesValue)
  }

  const cleanSequences = () => {
    setSequences(
      sequences
        .replace(/,/g, " ")      //  ","->" "
        .replace(/\s\s+/g, " ") // "\s{2,inf}"->"\s"
        .split(" ") // "1 2 3 4 5" -> [1, 2, 3, 4, 5]
        .map((sequenceAsString) => { return sequenceAsString.replace(/^0+/, "") }) // remove zeros at the beginning
        .join(" ")
    )
    setCleanUp(true)
  }

  const dispatchModifyAction = () => {
    let sequencesArrOfIntegers = sequencesInDesiredFormat(sequences)
    if (selectedOption === "row") {
      modifyRow(modifyIndex, sequencesArrOfIntegers)
    } else {
      modifyColumn(modifyIndex, sequencesArrOfIntegers)
    }
    if(modifyIndex < maxModifyIndex) {
      setModifyIndex(modifyIndex + 1)
    }
  }

  const sequencesInDesiredFormat = (sequences: string) => {
    return sequences.trim().split(" ").map((sequence) => parseInt(sequence))
  }

  return (
    <React.Fragment>
      <h4>Enter sequences:</h4>
      <div style={{display: "flex", flexDirection: "row", alignItems: "center"}}>
        <ToggleButtonGroup
          color="primary"
          value={selectedOption}
          exclusive
          onChange={handleOptionChange}
          aria-label="Platform"
        >
          {
            options.map((option) => {
              return <ToggleButton value={option}>{option}</ToggleButton>
            })
          }
        </ToggleButtonGroup>
        <TextField
          style={{ width: "80%", marginTop: "5px" }}
          label={"index"}
          disabled={false}
          value={modifyIndex}
          onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => changeIndexHandler(event)}
          type="number"
        />
      </div>
      <div style={{ display: "flex", height: "fit-content" }}>
        <Tooltip title={"Clean!"} placement="top">
          <Button onClick={() => cleanSequences()} style={{ marginTop: "5px" }} variant={"contained"}>
            <FontAwesomeIcon icon={faBroom} />
          </Button>
        </Tooltip>
        <TextField
          style={{ width: "60%", marginTop: "5px" }}
          value={sequences}
          onKeyDown={(event: React.KeyboardEvent<HTMLDivElement>) => sequencesInputHandler(event)}
          onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => sequencesChangeHandler(event)}
          disabled={false}
        />
        <Button disabled={!cleanUp} onClick={() => dispatchModifyAction()} style={{ marginTop: "5px" }} variant={"contained"}>
          MODIFY!
        </Button>
      </div>
    </React.Fragment>
  )
}

export default connector(SequencesModifier)