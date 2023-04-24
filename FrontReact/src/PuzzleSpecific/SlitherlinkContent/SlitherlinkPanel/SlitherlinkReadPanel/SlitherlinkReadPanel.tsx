// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"
import { AppState } from "../../../../store"

// (sub)components
import SlitherlinkFiltersSection from "./SlitherlinkFiltersSection/SlitherlinkFiltersSection"

// images etc
import bubbleLoaderIcon from  "../../../../assets/Loaders/Bubble-Loader-Icon/96x96.gif"
import { modes, SetMode } from "../../../../store/display"


interface OwnSlitherlinkReadPanelProps {
    dataFetched: boolean
}

const mapStateToProps = (state: AppState) => ({
    slitherlinksList: state.slitherlinkDataReducer.slitherlinksList
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setMode: (mode: modes) => 
        dispatch(SetMode(mode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkReadPanelPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkReadPanelProps = SlitherlinkReadPanelPropsFromRedux & OwnSlitherlinkReadPanelProps

const SlitherlinkReadPanel : React.FC<SlitherlinkReadPanelProps> = ({
    dataFetched, setMode}) => {

    const [filtersCollected, setFiltersCollected] = useState<boolean>(false)

    useEffect(() => {
        setMode("READ")
        //eslint-disable-next-line
    }, [])

    return (
                <React.Fragment>
                    {
                            (dataFetched ? <SlitherlinkFiltersSection
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

export default connector(SlitherlinkReadPanel)