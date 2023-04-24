// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"

// (sub) components
import InfoCardTooltip from "../../../../InfoCardTooltip/InfoCardTooltip"

//fontawesome
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

interface OwnHitoriInformationTooltipProps {
}

const mapStateToProps = (state: AppState) => ({
    selectedHitori: state.hitoriDataReducer.selectedHitori
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriInformationTooltipPropsFromRedux = ConnectedProps<typeof connector>

type HitoriInformationTooltipProps = HitoriInformationTooltipPropsFromRedux & OwnHitoriInformationTooltipProps

const HitoriInformationTooltip : React.FC<HitoriInformationTooltipProps> = ({selectedHitori}) => {
    
    return (<InfoCardTooltip
        placement="left-end"
        title={
            <React.Fragment>
                <b>{selectedHitori.label}</b> <br />
                Source: {selectedHitori.source} <br />
                Height: {selectedHitori.height} <br />
                Width: {selectedHitori.width} <br />
                Difficulty: {selectedHitori.difficulty}
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

export default connector(HitoriInformationTooltip)