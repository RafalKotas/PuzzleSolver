// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux store
import { AppState } from "../../../../store"
import { modes, SetMode } from "../../../../store/display"

// (sub)components
import ArchitectFiltersSection from "./ArchitectFiltersSection/ArchitectFiltersSection"

// images etc
import bubbleLoaderIcon from  "../../../../assets/Loaders/Bubble-Loader-Icon/96x96.gif"


interface OwnArchitectReadPanelProps {
    dataFetched: boolean
}

const mapStateToProps = (state: AppState) => ({
    architectsList: state.architectDataReducer.architectsList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setMode: (mode: modes) => 
        dispatch(SetMode(mode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectReadPanelPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectReadPanelProps = ArchitectReadPanelPropsFromRedux & OwnArchitectReadPanelProps

const ArchitectReadPanel : React.FC<ArchitectReadPanelProps> = ({
    dataFetched, setMode}) => {

    const [filtersCollected, setFiltersCollected] = useState<boolean>(false)

    useEffect(() => {
        setMode("READ")
        //eslint-disable-next-line
    }, [])

    return (
                <React.Fragment>
                    {
                            (dataFetched ? <ArchitectFiltersSection
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

export default connector(ArchitectReadPanel)