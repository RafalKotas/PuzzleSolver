//react
import React, {  useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { nonogramInformation, SetNonogramsList} from "../../../store/data/nonogram"
import { modes, SetMode } from "../../../store/display"

// (sub) components
import NonogramReadPanel from "./NonogramReadPanel/NonogramReadPanel"
import NonogramCreatePanel from "./NonogramCreatePanel/NonogramCreatePanel"

//mui
import Tabs from "@mui/material/Tabs"
import Tab from "@mui/material/Tab"
import Box from "@mui/material/Box"

//services
import NonogramService from "../../../services/nonogram/nonogram.service"

//styles
import "./NonogramPanel.css"

const mapStateToProps = (state: AppState) => ({
    mode: state.displayReducer.mode,
    nonogramsList: state.nonogramDataReducer.nonogramsList,
    selectedDiffs: state.nonogramFiltersReducer.selectedDifficulties
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({

    setNonogramsList: (nonogramsList: nonogramInformation[]) =>
        dispatch(SetNonogramsList(nonogramsList)),

    setMode: (mode: modes) => 
        dispatch(SetMode(mode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramPanelPropsFromRedux = ConnectedProps<typeof connector>

type NonogramPanelProps = NonogramPanelPropsFromRedux

const NonogramPanel : React.FC<NonogramPanelProps> = ({nonogramsList, selectedDiffs, mode, setNonogramsList, setMode}) => {

    const [dataFetched, setDataFetched] = useState<boolean>(false)

    useEffect(() => {
        if(nonogramsList.length > 0) {
            setDataFetched(true)
        }
    }, [nonogramsList])

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setMode(newValue === 0 ? "READ" : "CREATE")
    }

    // 
    useEffect(() => {

        console.log(selectedDiffs)
        
        fetchAllFiles()

        // eslint-disable-next-line
    }, [])

    const fetchAllFiles = async () => {

        //let dbQueryFetchStartTime = new Date().getTime()

        NonogramService.getNonogramsList().then((nonogramsListResponse) => {
            setNonogramsList(nonogramsListResponse.data as nonogramInformation[])
        })

        /*let dbQueryFetchEndTime = new Date().getTime()

        let dbQueryFetchSpentTime = (dbQueryFetchEndTime - dbQueryFetchStartTime)

        console.log("dbQueryFetch spent time: " + dbQueryFetchSpentTime)
        console.log("accell")*/
    }

    return (
        <div id="nonogram-panel">
            <Box sx={{ bgcolor: "background.paper" }}>
            <Tabs value={mode === "READ" ? 0 : 1} onChange={handleChange} centered>
                <Tab label="READ" />
                <Tab label="CREATE" />
            </Tabs>
            </Box>
            {
                mode === "READ" && <NonogramReadPanel dataFetched={dataFetched}/>
            }
            {
                mode === "CREATE" && <NonogramCreatePanel/>
            }
        </div>
    )
}

export default connector(NonogramPanel)