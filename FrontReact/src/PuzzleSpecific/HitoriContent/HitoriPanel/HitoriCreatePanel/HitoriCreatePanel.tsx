// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddHitoriDetail, hitoriInformation, ChangeHitoriDetail, RemoveHitoriDetail, SaveTemporaryHitori } from "../../../../store/data/hitori"
import { modes, SetMode } from "../../../../store/display"

// (sub)component(s)

//mui
import { Button, ButtonProps, styled } from "@mui/material"
import { grey } from "@mui/material/colors"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faPlus } from "@fortawesome/free-solid-svg-icons"

// sub(components)
import StringPropTextField from "../../../../CommonComponents/StringPropTextField/StringPropTextField"
import HitoriSliders from "./HitoriSliders"

// other
import { propDetails } from "../../../../CommonComponents/StringPropTextField/types"
import { HitoriStringProps } from "./types"

interface OwnHitoriCreatePanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedHitori: state.hitoriDataReducer.selectedHitori,
    HitoriCorrect: state.hitoriDataReducer.hitoriCorrect,
    detailsSet: state.hitoriDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addHitoriDetail: (property : keyof hitoriInformation) => dispatch(AddHitoriDetail(property)),
    removeHitoriDetail: (property : keyof hitoriInformation) => dispatch(RemoveHitoriDetail(property)),
    changeHitoriDetail: (property : keyof hitoriInformation, value: string | number | number[]) => 
        dispatch(ChangeHitoriDetail(property, value)),
    saveCreatedTemplate: () => dispatch(SaveTemporaryHitori()),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriCreatePanelPropsFromRedux = ConnectedProps<typeof connector>

type HitoriCreatePanelProps = HitoriCreatePanelPropsFromRedux & OwnHitoriCreatePanelProps

const HitoriCreatePanel : React.FC<HitoriCreatePanelProps> = ({addHitoriDetail, removeHitoriDetail, 
    changeHitoriDetail, saveCreatedTemplate, detailsSet, setMode}) => {

    useEffect(() => {
        setMode("CREATE")
        // eslint-disable-next-line
    }, [])

    const handleStringProperty = (property: string, value: string, valid: boolean) => {
        if(!valid) {
            if(detailsSet.includes(property)) {
                removeHitoriDetail(property as keyof hitoriInformation)
            }
        } else if(valid) {
            if(!detailsSet.includes(property)) {
                addHitoriDetail(property as keyof hitoriInformation)
            }
            changeHitoriDetail(property as keyof hitoriInformation, value)
        }
    }

    
    const ColorButton = styled(Button)<ButtonProps>(({ theme }) => ({
        color: theme.palette.getContrastText(grey[700]),
        backgroundColor: grey[500],
        "&:hover": {
          backgroundColor: grey[700],
        },
    }));

    return (
        <section className="puzzle-options-section">
            <HitoriSliders />
            {
                HitoriStringProps.map((prop : propDetails) => {
                    return <StringPropTextField 
                                prop={prop}
                                passValueToParent={handleStringProperty}
                            />
                })
            }
            <ColorButton onClick={() => saveCreatedTemplate()}
                disabled={detailsSet.length < 4} color="success">
                ADD<FontAwesomeIcon icon={faPlus}/>
            </ColorButton>
        </section>
    )
}

export default connector(HitoriCreatePanel)