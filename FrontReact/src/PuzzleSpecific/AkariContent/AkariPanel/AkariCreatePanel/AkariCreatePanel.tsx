// react
import React, { useEffect } from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    AddAkariDetail, 
    akariInformation, 
    ChangeAkariDetail, 
    RemoveAkariDetail, 
    SaveTemporaryAkari 
} from "store/data/akari"
import { 
    modes, 
    SetMode 
} from "store/display"

// (sub)component(s)
import AkariSliders from "PuzzleSpecific/AkariContent/AkariPanel/AkariCreatePanel/AkariSliders"
import StringPropTextField from "CommonComponents/StringPropTextField/StringPropTextField"

// mui - components
import { Button, ButtonProps, styled } from "@mui/material"
import { grey } from "@mui/material/colors"

// fortawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faPlus } from "@fortawesome/free-solid-svg-icons"

// other
import { AkariStringProps } from "PuzzleSpecific/AkariContent/AkariPanel/AkariCreatePanel/types"
import { propDetails } from "CommonComponents/StringPropTextField/types"

interface OwnAkariCreatePanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedAkari: state.akariDataReducer.selectedAkari,
    AkariCorrect: state.akariDataReducer.akariCorrect,
    detailsSet: state.akariDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addAkariDetail: (property : keyof akariInformation) => dispatch(AddAkariDetail(property)),
    removeAkariDetail: (property : keyof akariInformation) => dispatch(RemoveAkariDetail(property)),
    changeAkariDetail: (property : keyof akariInformation, value: string | number | number[]) => 
        dispatch(ChangeAkariDetail(property, value)),
    saveCreatedTemplate: () => dispatch(SaveTemporaryAkari()),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariCreatePanelPropsFromRedux = ConnectedProps<typeof connector>

type AkariCreatePanelProps = AkariCreatePanelPropsFromRedux & OwnAkariCreatePanelProps

const AkariCreatePanel : React.FC<AkariCreatePanelProps> = ({addAkariDetail, removeAkariDetail, 
    changeAkariDetail, saveCreatedTemplate, detailsSet, setMode}) => {

    useEffect(() => {
        setMode("CREATE")
        // eslint-disable-next-line
    }, [])

    const handleStringProperty = (property: string, value: string, valid: boolean) => {
        if (!valid) {
            if (detailsSet.includes(property)) {
                removeAkariDetail(property as keyof akariInformation)
            }
        } else if (valid) {
            if (!detailsSet.includes(property)) {
                addAkariDetail(property as keyof akariInformation)
            }
            changeAkariDetail(property as keyof akariInformation, value)
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
            <AkariSliders />
            {
                AkariStringProps.map((prop : propDetails) => {
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

export default connector(AkariCreatePanel)