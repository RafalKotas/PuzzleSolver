//react
import React, {  useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { hitoriInformation, SetHitorisList, SetSelectedHitori } from "../../../store/data/hitori"

// (sub) components
import HitoriReadPanel from "./HitoriReadPanel/HitoriReadPanel"
import HitoriCreatePanel from "./HitoriCreatePanel/HitoriCreatePanel"

//mui
import Tabs from "@mui/material/Tabs"
import Tab from "@mui/material/Tab"
import Box from "@mui/material/Box"

const mapStateToProps = (state: AppState) => ({
    hitorisList: state.hitoriDataReducer.hitorisList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({

    setHitorisList: (hitorisList: hitoriInformation[]) =>
        dispatch(SetHitorisList(hitorisList)),

    setSelectedHitori: (hitori: hitoriInformation) => 
        dispatch(SetSelectedHitori(hitori))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriPanelPropsFromRedux = ConnectedProps<typeof connector>

type HitoriPanelProps = HitoriPanelPropsFromRedux

const HitoriPanel : React.FC<HitoriPanelProps> = ({hitorisList, setHitorisList}) => {

    const [mode, setMode] = useState("READ")
    const [dataFetched, setDataFetched] = useState<boolean>(false)

    useEffect(() => {
        if(hitorisList.length > 0) {
            setDataFetched(true)
        }
    }, [hitorisList])

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setMode(newValue === 0 ? "READ" : "CREATE")
    }

    const puzzlePath = "../resources/Hitori/"

    const axios = require("axios")

    // 
    useEffect(() => {
        
        fetchAllFiles()

        // eslint-disable-next-line
    }, [])

    const fetchAllFiles = async () => {

        const response = await axios.get(puzzlePath)
        
        let sortedNames = response.data.sort((puzzleNameA : string, puzzleNameB : string) => puzzleNameA.localeCompare(puzzleNameB))

        let puzzleTemporaryList = [] as hitoriInformation[]
        
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

            let hitoriListSorted = puzzleTemporaryList.sort((a: hitoriInformation, b: hitoriInformation) => {
                return a.value.localeCompare(b.value)
            })

            setHitorisList(hitoriListSorted)
        })
    }

    return (
        <div id="hitori-panel">
            <Box sx={{ bgcolor: "background.paper" }}>
            <Tabs value={mode === "READ" ? 0 : 1} onChange={handleChange} centered>
                <Tab label="READ" />
                <Tab label="CREATE" />
            </Tabs>
            </Box>
            {
                mode === "READ" && <HitoriReadPanel dataFetched={dataFetched}/> /*:   <HitoriCreatePanel/>*/
            }
            {
                mode === "CREATE" && <HitoriCreatePanel />
            }
        </div>
    )
}

export default connector(HitoriPanel)