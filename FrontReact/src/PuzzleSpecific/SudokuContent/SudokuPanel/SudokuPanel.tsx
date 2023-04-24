//react
import React, {  useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { sudokuInformation, SetSudokusList, SetSelectedSudoku } from "../../../store/data/sudoku"

// (sub) components
import SudokuReadPanel from "./SudokuReadPanel/SudokuReadPanel"
import SudokuCreatePanel from "./SudokuCreatePanel/SudokuCreatePanel"

//mui
import Tabs from "@mui/material/Tabs"
import Tab from "@mui/material/Tab"
import Box from "@mui/material/Box"

const mapStateToProps = (state: AppState) => ({
    sudokusList: state.sudokuDataReducer.sudokusList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({

    setSudokusList: (sudokusList: sudokuInformation[]) =>
        dispatch(SetSudokusList(sudokusList)),

    setSelectedSudoku: (sudoku: sudokuInformation) => 
        dispatch(SetSelectedSudoku(sudoku))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuPanelPropsFromRedux = ConnectedProps<typeof connector>

type SudokuPanelProps = SudokuPanelPropsFromRedux

const SudokuPanel : React.FC<SudokuPanelProps> = ({sudokusList, setSudokusList}) => {

    const [mode, setMode] = useState("READ")
    const [dataFetched, setDataFetched] = useState<boolean>(false)

    useEffect(() => {
        if(sudokusList.length > 0) {
            setDataFetched(true)
        }
    }, [sudokusList])

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setMode(newValue === 0 ? "READ" : "CREATE")
    }

    const puzzlePath = "../resources/Sudoku/"

    const axios = require("axios")

    // 
    useEffect(() => {
        
        fetchAllFiles()

        // eslint-disable-next-line
    }, [])

    const fetchAllFiles = async () => {

        const response = await axios.get(puzzlePath)
        
        let sortedNames = response.data.sort((puzzleNameA : string, puzzleNameB : string) => puzzleNameA.localeCompare(puzzleNameB))

        let puzzleTemporaryList = [] as sudokuInformation[]
        
        let puzzleFilesToLoadPromises: any[] = [];

        for (let puzzleNameInd = 0; puzzleNameInd < sortedNames.length; puzzleNameInd++) {
            puzzleFilesToLoadPromises.push(
                axios.get(puzzlePath + sortedNames[ puzzleNameInd ])
                    .then((response: any) => {
                        let puzzleInfo = response.data
                        puzzleInfo.value = sortedNames[ puzzleNameInd ]
                        puzzleInfo.label = sortedNames[ puzzleNameInd ]
                        puzzleTemporaryList.push(puzzleInfo)
                    })
                    .catch((error: { message: any }) => {
                        if (axios.isAxiosError(error)) {
                            return error.message
                        } else {
                            return "An unexpected error occurred"
                        }
                    })
            )
        }

        Promise.all(puzzleFilesToLoadPromises).then(() => {

            let sudokuListSorted = puzzleTemporaryList.sort((a: sudokuInformation, b: sudokuInformation) => {
                return a.value.localeCompare(b.value)
            })

            setSudokusList(sudokuListSorted)
        })
    }

    return (
        <div id="sudoku-panel">
            <Box sx={{ bgcolor: "background.paper" }}>
            <Tabs value={mode === "READ" ? 0 : 1} onChange={handleChange} centered>
                <Tab label="READ" />
                <Tab label="CREATE" />
            </Tabs>
            </Box>
            {
                mode === "READ" && <SudokuReadPanel dataFetched={dataFetched}/> /*:   <SudokuCreatePanel/>*/
            }
            {
                mode === "CREATE" && <SudokuCreatePanel />
            }
        </div>
    )
}

export default connector(SudokuPanel)