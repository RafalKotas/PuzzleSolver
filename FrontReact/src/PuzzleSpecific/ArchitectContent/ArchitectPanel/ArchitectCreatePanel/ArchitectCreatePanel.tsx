// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddArchitectDetail, architectInformation, ChangeArchitectDetail, RemoveArchitectDetail, SaveTemporaryArchitect } from "../../../../store/data/architect"
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
import ArchitectSliders from "./ArchitectSliders"

// other
import { propDetails } from "../../../../CommonComponents/StringPropTextField/types"
import { ArchitectStringProps } from "./types"

interface OwnArchitectCreatePanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedArchitect: state.architectDataReducer.selectedArchitect,
    detailsSet: state.architectDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addArchitectDetail: (property : keyof architectInformation) => dispatch(AddArchitectDetail(property)),
    removeArchitectDetail: (property : keyof architectInformation) => dispatch(RemoveArchitectDetail(property)),
    changeArchitectDetail: (property : keyof architectInformation, value: string | number | number[]) => 
        dispatch(ChangeArchitectDetail(property, value)),
    saveCreatedTemplate: () => dispatch(SaveTemporaryArchitect()),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectCreatePanelPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectCreatePanelProps = ArchitectCreatePanelPropsFromRedux & OwnArchitectCreatePanelProps

const ArchitectCreatePanel : React.FC<ArchitectCreatePanelProps> = ({addArchitectDetail, removeArchitectDetail, 
    changeArchitectDetail, saveCreatedTemplate, detailsSet, setMode}) => {

    useEffect(() => {
        setMode("CREATE")
        // eslint-disable-next-line
    }, [])

    const handleStringProperty = (property: string, value: string, valid: boolean) => {
        if(!valid) {
            if(detailsSet.includes(property)) {
                removeArchitectDetail(property as keyof architectInformation)
            }
        } else if(valid) {
            if(!detailsSet.includes(property)) {
                addArchitectDetail(property as keyof architectInformation)
            }
            changeArchitectDetail(property as keyof architectInformation, value)
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
            <ArchitectSliders />
            {
                ArchitectStringProps.map((prop : propDetails) => {
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

export default connector(ArchitectCreatePanel)