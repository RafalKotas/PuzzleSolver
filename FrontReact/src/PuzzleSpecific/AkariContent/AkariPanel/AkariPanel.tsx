//react
import React, {  useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { akariInformation, SetAkarisList, SetSelectedAkari } from "../../../store/data/akari"

// (sub) components
import AkariReadPanel from "./AkariReadPanel/AkariReadPanel"
//import AkariCreatePanel from "./AkariCreatePanel/AkariCreatePanel"

//mui
import Tabs from "@mui/material/Tabs"
import Tab from "@mui/material/Tab"
import Box from "@mui/material/Box"
import AkariCreatePanel from "./AkariCreatePanel/AkariCreatePanel"

const mapStateToProps = (state: AppState) => ({
    akarisList: state.akariDataReducer.akarisList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({

    setAkarisList: (akarisList: akariInformation[]) =>
        dispatch(SetAkarisList(akarisList)),

    setSelectedAkari: (akari: akariInformation) => 
        dispatch(SetSelectedAkari(akari))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariPanelPropsFromRedux = ConnectedProps<typeof connector>

type AkariPanelProps = AkariPanelPropsFromRedux

const AkariPanel : React.FC<AkariPanelProps> = ({akarisList, setAkarisList}) => {

    const [mode, setMode] = useState("READ")
    const [dataFetched, setDataFetched] = useState<boolean>(false)

    useEffect(() => {
        if(akarisList.length > 0) {
            setDataFetched(true)
        }
    }, [akarisList])

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setMode(newValue === 0 ? "READ" : "CREATE")
    }

    const puzzlePath = "../resources/Akari/"

    const axios = require("axios")

    // 
    useEffect(() => {
        
        fetchAllFiles()

        // eslint-disable-next-line
    }, [])

    const fetchAllFiles = async () => {

        const response = await axios.get(puzzlePath)
        
        let sortedNames = response.data.sort((puzzleNameA : string, puzzleNameB : string) => puzzleNameA.localeCompare(puzzleNameB))

        let puzzleTemporaryList = [] as akariInformation[]
        
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

            let akariListSorted = puzzleTemporaryList.sort((a: akariInformation, b: akariInformation) => {
                return a.value.localeCompare(b.value)
            })

            setAkarisList(akariListSorted)
        })
    }

    return (
        <div id="akari-panel">
            <Box sx={{ bgcolor: "background.paper" }}>
            <Tabs value={mode === "READ" ? 0 : 1} onChange={handleChange} centered>
                <Tab label="READ" />
                <Tab label="CREATE" />
            </Tabs>
            </Box>
            {
                mode === "READ" && <AkariReadPanel dataFetched={dataFetched}/> /*:   <AkariCreatePanel/>*/
            }
            {
                mode === "CREATE" && <AkariCreatePanel />
            }
        </div>
    )
}

export default connector(AkariPanel)