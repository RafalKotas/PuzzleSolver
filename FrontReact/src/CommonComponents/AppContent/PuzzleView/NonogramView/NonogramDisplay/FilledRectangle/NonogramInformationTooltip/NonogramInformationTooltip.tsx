// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"
import { calculateSequencesSectionDimensionInPx } from "../../../../../../../store/layout/nonogram"

// redux - store
import { AppState } from "../../../../../../../store"

// (sub) components
import InfoCardTooltip from "../../../../../../InfoCardTooltip/InfoCardTooltip"

//fontawesome
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

interface OwnNonogramInformationTooltipProps {
}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    columnSequencesHeight: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "column")
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramInformationTooltipPropsFromRedux = ConnectedProps<typeof connector>

type NonogramInformationTooltipProps = NonogramInformationTooltipPropsFromRedux & OwnNonogramInformationTooltipProps

const NonogramInformationTooltip : React.FC<NonogramInformationTooltipProps> = ({selectedNonogram, columnSequencesHeight}) => {
    
    return (<InfoCardTooltip
        title={
            <React.Fragment>
                {
                    selectedNonogram && 
                        <React.Fragment>
                            <b>{selectedNonogram.filename}</b> <br />
                            Source: {selectedNonogram.source} <br />
                            Height: {selectedNonogram.height} <br />
                            Width: {selectedNonogram.width} <br />
                            {
                                selectedNonogram.source === "logi" &&
                                <React.Fragment>
                                    Year: {selectedNonogram.year} <br />
                                    Month: {selectedNonogram.month} <br />
                                </React.Fragment>
                            }
                            Difficulty: {selectedNonogram.difficulty}
                        </React.Fragment>
                }
            </React.Fragment>
        }
    >
        <div style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center"
        }}>
            <FontAwesomeIcon icon={faCircleInfo} style={{
                fontSize: columnSequencesHeight / 5,
                cursor: "help"
            }} />
        </div>
    </InfoCardTooltip>)
}

export default connector(NonogramInformationTooltip)