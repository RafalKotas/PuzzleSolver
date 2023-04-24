// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"

//fontawesome
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

// (sub) components
import InfoCardTooltip from "../../../../../InfoCardTooltip/InfoCardTooltip"

interface OwnSlitherlinkInformationTooltipProps {
}

const mapStateToProps = (state: AppState) => ({
    selectedSlitherlink: state.slitherlinkDataReducer.selectedSlitherlink
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkInformationTooltipPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkInformationTooltipProps = SlitherlinkInformationTooltipPropsFromRedux & OwnSlitherlinkInformationTooltipProps

const SlitherLinkInformationTooltip : React.FC<SlitherlinkInformationTooltipProps> = ({selectedSlitherlink}) => {
    
    return (<InfoCardTooltip
        title={
            <React.Fragment>
                <b>{selectedSlitherlink.label}</b> <br />
                Source: {selectedSlitherlink.source} <br />
                Height: {selectedSlitherlink.height} <br />
                Width: {selectedSlitherlink.width} <br />
                {
                    selectedSlitherlink.source === "Logi" &&
                    <React.Fragment>
                        Year: {selectedSlitherlink.year} <br />
                        Month: {selectedSlitherlink.month} <br />
                    </React.Fragment>
                }
                Difficulty: {selectedSlitherlink.difficulty} <br />
            </React.Fragment>
        }
    >
        <div style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center"
        }}>
            <FontAwesomeIcon icon={faCircleInfo} style={{
                fontSize: "15px",
                cursor: "help",
                height: "20px",
                width: "20px"
            }} />
        </div>
    </InfoCardTooltip>)
}

export default connector(SlitherLinkInformationTooltip)