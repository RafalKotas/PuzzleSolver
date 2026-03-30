// react
import React, { 
    useEffect, 
    useState 
} from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    modes, 
    SetMode 
} from "store/display"

// (sub)component(s)
import AkariFiltersSection from "PuzzleSpecific/AkariContent/AkariPanel/AkariReadPanel/AkariFiltersSection/AkariFiltersSection"

// other
import bubbleLoaderIcon from  "assets/Loaders/Bubble-Loader-Icon/96x96.gif"


interface OwnAkariReadPanelProps {
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

type AkariReadPanelPropsFromRedux = ConnectedProps<typeof connector>

type AkariReadPanelProps = AkariReadPanelPropsFromRedux & OwnAkariReadPanelProps

const AkariReadPanel : React.FC<AkariReadPanelProps> = ({
    dataFetched, setMode}) => {

    const [filtersCollected, setFiltersCollected] = useState<boolean>(false)

    useEffect(() => {
        setMode("READ")
        //eslint-disable-next-line
    }, [])

    return (
                <React.Fragment>
                    {
                            (dataFetched ? <AkariFiltersSection
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

export default connector(AkariReadPanel)