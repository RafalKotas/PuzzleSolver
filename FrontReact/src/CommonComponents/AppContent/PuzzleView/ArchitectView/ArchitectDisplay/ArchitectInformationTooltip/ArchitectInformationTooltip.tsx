// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"

// (sub) components
import InfoCardTooltip from "../../../../../InfoCardTooltip/InfoCardTooltip"

//fontawesome
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

interface OwnArchitectInformationTooltipProps {
}

const mapStateToProps = (state: AppState) => ({
    selectedArchitect: state.architectDataReducer.selectedArchitect
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectInformationTooltipPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectInformationTooltipProps = ArchitectInformationTooltipPropsFromRedux & OwnArchitectInformationTooltipProps

const ArchitectInformationTooltip : React.FC<ArchitectInformationTooltipProps> = ({selectedArchitect}) => {
    
    return (<InfoCardTooltip
        placement="left"
        title={
            <React.Fragment>
                <b>{selectedArchitect.label}</b> <br />
                Source: {selectedArchitect.source} <br />
                Height: {selectedArchitect.height} <br />
                Width: {selectedArchitect.width} <br />
                Difficulty: {selectedArchitect.difficulty}
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

export default connector(ArchitectInformationTooltip)