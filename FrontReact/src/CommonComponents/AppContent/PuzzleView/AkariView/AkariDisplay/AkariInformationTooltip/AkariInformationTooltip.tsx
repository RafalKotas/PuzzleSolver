// react
import React from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"

// (sub)component(s)
import InfoCardTooltip from "CommonComponents/InfoCardTooltip/InfoCardTooltip"

// fortawesome
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

interface OwnAkariInformationTooltipProps {
}

const mapStateToProps = (state: AppState) => ({
    selectedAkari: state.akariDataReducer.selectedAkari
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariInformationTooltipPropsFromRedux = ConnectedProps<typeof connector>

type AkariInformationTooltipProps = AkariInformationTooltipPropsFromRedux & OwnAkariInformationTooltipProps

const AkariInformationTooltip : React.FC<AkariInformationTooltipProps> = ({selectedAkari}) => {
    
    return (<InfoCardTooltip
        placement="left"
        title={
            <React.Fragment>
                <b>{selectedAkari.label}</b> <br />
                Source: {selectedAkari.source} <br />
                Height: {selectedAkari.height} <br />
                Width: {selectedAkari.width} <br />
                Difficulty: {selectedAkari.difficulty}
            </React.Fragment>
        }
    >
        <div style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
            alignItems: "center"
        }}>
            <FontAwesomeIcon icon={faCircleInfo} style={{
                fontSize: "15px",
                cursor: "help",
                height: "20px"
            }} />
        </div>
    </InfoCardTooltip>)
}

export default connector(AkariInformationTooltip)