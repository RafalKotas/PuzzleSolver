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
import { calculateSequencesSectionDimensionInPx } from "store/layout/nonogram"

// (sub)component(s)
import InfoCardTooltip from "CommonComponents/InfoCardTooltip/InfoCardTooltip"

// fortawesome
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
                                ["logi", "logiMix"].includes(selectedNonogram.source) &&
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