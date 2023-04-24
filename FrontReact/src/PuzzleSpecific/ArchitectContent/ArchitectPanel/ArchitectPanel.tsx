//react
import React, {  useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { architectInformation, SetArchitectsList, SetSelectedArchitect } from "../../../store/data/architect"

// (sub) components
import ArchitectReadPanel from "./ArchitectReadPanel/ArchitectReadPanel"
import ArchitectCreatePanel from "./ArchitectCreatePanel/ArchitectCreatePanel"

//mui
import Tabs from "@mui/material/Tabs"
import Tab from "@mui/material/Tab"
import Box from "@mui/material/Box"

const mapStateToProps = (state: AppState) => ({
    architectsList: state.architectDataReducer.architectsList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({

    setArchitectsList: (architectsList: architectInformation[]) =>
        dispatch(SetArchitectsList(architectsList)),

    setSelectedArchitect: (architect: architectInformation) => 
        dispatch(SetSelectedArchitect(architect))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectPanelPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectPanelProps = ArchitectPanelPropsFromRedux

const ArchitectPanel : React.FC<ArchitectPanelProps> = ({architectsList, setArchitectsList}) => {

    const [mode, setMode] = useState("READ")
    const [dataFetched, setDataFetched] = useState<boolean>(false)

    useEffect(() => {
        if(architectsList.length > 0) {
            setDataFetched(true)
        }
    }, [architectsList])

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setMode(newValue === 0 ? "READ" : "CREATE")
    }

    const puzzlePath = "../resources/Architect/"

    const axios = require("axios")

    // 
    useEffect(() => {
        
        fetchAllFiles()

        // eslint-disable-next-line
    }, [])

    const fetchAllFiles = async () => {

        const response = await axios.get(puzzlePath)
        
        let sortedNames = response.data.sort((puzzleNameA : string, puzzleNameB : string) => puzzleNameA.localeCompare(puzzleNameB))

        let puzzleTemporaryList = [] as architectInformation[]
        
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

            let architectListSorted = puzzleTemporaryList.sort((a: architectInformation, b: architectInformation) => {
                return a.value.localeCompare(b.value)
            })

            setArchitectsList(architectListSorted)
        })
    }

    return (
        <div id="architect-panel">
            <Box sx={{ bgcolor: "background.paper" }}>
            <Tabs value={mode === "READ" ? 0 : 1} onChange={handleChange} centered>
                <Tab label="READ" />
                <Tab label="CREATE" />
            </Tabs>
            </Box>
            {
                mode === "READ" && <ArchitectReadPanel dataFetched={dataFetched}/>
            }
            {
                mode === "CREATE" && <ArchitectCreatePanel/>
            }
        </div>
    )
}

export default connector(ArchitectPanel)