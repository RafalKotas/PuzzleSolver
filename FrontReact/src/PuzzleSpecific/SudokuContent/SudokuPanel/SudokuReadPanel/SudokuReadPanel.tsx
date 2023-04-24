// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"
import { AppState } from "../../../../store"

// (sub)components
import SudokuFiltersSection from "./SudokuFiltersSection/SudokuFiltersSection"

// images etc
import bubbleLoaderIcon from  "../../../../assets/Loaders/Bubble-Loader-Icon/96x96.gif"
import { modes, SetMode } from "../../../../store/display"


interface OwnSudokuReadPanelProps {
    dataFetched: boolean
}

const mapStateToProps = (state: AppState) => ({
    akarisList: state.akariDataReducer.akarisList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setMode: (mode: modes) => 
        dispatch(SetMode(mode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuReadPanelPropsFromRedux = ConnectedProps<typeof connector>

type SudokuReadPanelProps = SudokuReadPanelPropsFromRedux & OwnSudokuReadPanelProps

const SudokuReadPanel : React.FC<SudokuReadPanelProps> = ({
    dataFetched, setMode}) => {

    const [filtersCollected, setFiltersCollected] = useState<boolean>(false)

    useEffect(() => {
        setMode("READ")
        //eslint-disable-next-line
    }, [])

    return (
                <React.Fragment>
                    {
                            (dataFetched ? <SudokuFiltersSection
                                key={"pfs-" + filtersCollected}
                                markFiltersCollected={() => {setFiltersCollected(true)}}
                                showFilters={filtersCollected}
                            /> 
                                    : 
                                <img style={{
                                    width: "50px",
                                    height: "50px"
                                    }} 
                                    src={bubbleLoaderIcon} 
                                    alt="loading-circle"
                                />
                            )
                        
                    }
                </React.Fragment>
    )
}

export default connector(SudokuReadPanel)