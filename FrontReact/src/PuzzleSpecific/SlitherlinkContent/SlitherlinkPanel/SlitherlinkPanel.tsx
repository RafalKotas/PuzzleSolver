//react
import React, {  useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { slitherlinkInformation, SetSlitherlinksList, SetSelectedSlitherlink } from "../../../store/data/slitherlink"

// (sub) components
import SlitherlinkReadPanel from "./SlitherlinkReadPanel/SlitherlinkReadPanel"
import SlitherlinkCreatePanel from "./SlitherlinkCreatePanel/SlitherlinkCreatePanel"

//mui
import Tabs from "@mui/material/Tabs"
import Tab from "@mui/material/Tab"
import Box from "@mui/material/Box"

const mapStateToProps = (state: AppState) => ({
    slitherlinksList: state.slitherlinkDataReducer.slitherlinksList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({

    setSlitherlinksList: (slitherlinksList: slitherlinkInformation[]) =>
        dispatch(SetSlitherlinksList(slitherlinksList)),

    setSelectedSlitherlink: (slitherlink: slitherlinkInformation) => 
        dispatch(SetSelectedSlitherlink(slitherlink))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkPanelPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkPanelProps = SlitherlinkPanelPropsFromRedux

const SlitherlinkPanel : React.FC<SlitherlinkPanelProps> = ({slitherlinksList, setSlitherlinksList}) => {

    const [mode, setMode] = useState("READ")
    const [dataFetched, setDataFetched] = useState<boolean>(false)

    useEffect(() => {
        if(slitherlinksList.length > 0) {
            setDataFetched(true)
        }
    }, [slitherlinksList])

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setMode(newValue === 0 ? "READ" : "CREATE")
    }

    const puzzlePath = "../resources/Slitherlink/"

    const axios = require("axios")

    // 
    useEffect(() => {
        
        fetchAllFiles()

        // eslint-disable-next-line
    }, [])

    const fetchAllFiles = async () => {

        const response = await axios.get(puzzlePath)
        
        let sortedNames = response.data.sort((puzzleNameA : string, puzzleNameB : string) => puzzleNameA.localeCompare(puzzleNameB))

        let puzzleTemporaryList = [] as slitherlinkInformation[]
        
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

            let slitherlinkListSorted = puzzleTemporaryList.sort((a: slitherlinkInformation, b: slitherlinkInformation) => {
                return a.value.localeCompare(b.value)
            })

            setSlitherlinksList(slitherlinkListSorted)
        })
    }

    return (
        <div id="slitherlink-panel">
            <Box sx={{ bgcolor: "background.paper" }}>
            <Tabs value={mode === "READ" ? 0 : 1} onChange={handleChange} centered>
                <Tab label="READ" />
                <Tab label="CREATE" />
            </Tabs>
            </Box>
            {
                mode === "READ" && <SlitherlinkReadPanel dataFetched={dataFetched}/> /*:   <SlitherlinkCreatePanel/>*/
            }
            {
                mode === "CREATE" && <SlitherlinkCreatePanel/>
            }
        </div>
    )
}

export default connector(SlitherlinkPanel)