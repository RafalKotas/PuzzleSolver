// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"

// redux store
import { Dispatch } from "redux"
import { AppState } from "../../../../store"

// (sub)components
import HitoriFiltersSection from "./HitoriFiltersSection/HitoriFiltersSection"

// images etc
import bubbleLoaderIcon from  "../../../../assets/Loaders/Bubble-Loader-Icon/96x96.gif"
import { modes, SetMode } from "../../../../store/display"


interface OwnHitoriReadPanelProps {
    dataFetched: boolean
}

const mapStateToProps = (state: AppState) => ({
    hitorisList: state.hitoriDataReducer.hitorisList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setMode: (mode: modes) => 
        dispatch(SetMode(mode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriReadPanelPropsFromRedux = ConnectedProps<typeof connector>

type HitoriReadPanelProps = HitoriReadPanelPropsFromRedux & OwnHitoriReadPanelProps

const HitoriReadPanel : React.FC<HitoriReadPanelProps> = ({
    dataFetched, setMode}) => {

    const [filtersCollected, setFiltersCollected] = useState<boolean>(false)

    useEffect(() => {
        setMode("READ")
        //eslint-disable-next-line
    }, [])

    return (
                <React.Fragment>
                    {
                            (dataFetched ? <HitoriFiltersSection
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

export default connector(HitoriReadPanel)