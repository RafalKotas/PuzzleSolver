// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux-store
import { AppState } from "../../../../store"

// (sub)components
import NonogramFiltersSection from "./NonogramFiltersSection/NonogramFiltersSection"

// images etc
import bubbleLoaderIcon from  "../../../../assets/Loaders/Bubble-Loader-Icon/96x96.gif"
import { modes, SetMode } from "../../../../store/display"


interface OwnNonogramReadPanelProps {
    dataFetched: boolean
}

const mapStateToProps = (state: AppState) => ({
    nonogramsList: state.nonogramDataReducer.nonogramsList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setMode: (mode: modes) => 
        dispatch(SetMode(mode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramReadPanelPropsFromRedux = ConnectedProps<typeof connector>

type NonogramReadPanelProps = NonogramReadPanelPropsFromRedux & OwnNonogramReadPanelProps

const NonogramReadPanel : React.FC<NonogramReadPanelProps> = ({
    dataFetched, setMode}) => {

    const [filtersCollected, setFiltersCollected] = useState<boolean>(false)

    useEffect(() => {
        setMode("READ")
        //eslint-disable-next-line
    }, [])

    return (
                <React.Fragment>
                    {
                            (dataFetched ? <NonogramFiltersSection
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

export default connector(NonogramReadPanel)