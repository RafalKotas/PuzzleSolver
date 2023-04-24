// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddSlitherlinkDetail, RemoveSlitherlinkDetail, ChangeSlitherlinkDetail, SaveTemporarySlitherlink, 
    slitherlinkInformation } from "../../../../store/data/slitherlink"
import { modes, SetMode } from "../../../../store/display"

//mui
import { Button, ButtonProps, styled } from "@mui/material"
import { grey } from "@mui/material/colors"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faPlus } from "@fortawesome/free-solid-svg-icons"

//other
import { slitherlinkStringProps } from "./types"
import StringPropTextField from "../../../../CommonComponents/StringPropTextField/StringPropTextField"
import SlitherlinkSliders from "./SlitherlinkSliders"

interface OwnSlitherlinkCreatePanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedSlitherlink: state.slitherlinkDataReducer.selectedSlitherlink,
    slitherlinkCorrect: state.slitherlinkDataReducer.slitherlinkCorrect,
    detailsSet: state.slitherlinkDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addSlitherlinkDetail: (property : keyof slitherlinkInformation) => dispatch(AddSlitherlinkDetail(property)),
    removeSlitherlinkDetail: (property : keyof slitherlinkInformation) => dispatch(RemoveSlitherlinkDetail(property)),
    changeSlitherlinkDetail: (property : keyof slitherlinkInformation, value: string | number | number[]) => 
        dispatch(ChangeSlitherlinkDetail(property, value)),
    saveCreatedTemplate: () => dispatch(SaveTemporarySlitherlink()),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkCreatePanelPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkCreatePanelProps = SlitherlinkCreatePanelPropsFromRedux & OwnSlitherlinkCreatePanelProps

const SlitherlinkCreatePanel : React.FC<SlitherlinkCreatePanelProps> = ({addSlitherlinkDetail, removeSlitherlinkDetail, 
    changeSlitherlinkDetail, saveCreatedTemplate, detailsSet, setMode}) => {

    useEffect(() => {
        setMode("CREATE")
        // eslint-disable-next-line
    }, [])

    const handleStringProperty = (property: string, value: string, valid: boolean) => {
        if(!valid) {
            if(detailsSet.includes(property)) {
                removeSlitherlinkDetail(property as keyof slitherlinkInformation)
            }
        } else if(valid) {
            if(!detailsSet.includes(property)) {
                addSlitherlinkDetail(property as keyof slitherlinkInformation)
            }
            changeSlitherlinkDetail(property as keyof slitherlinkInformation, value)
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
            <SlitherlinkSliders />
            {
                slitherlinkStringProps.map((prop) => {
                    return <StringPropTextField
                                prop={prop}
                                passValueToParent={handleStringProperty}
                            />
                })
            }
            <ColorButton onClick={() => saveCreatedTemplate()}
                disabled={detailsSet.length < 6} color="success">
                ADD<FontAwesomeIcon icon={faPlus}/>
            </ColorButton>
        </section>
    )
}

export default connector(SlitherlinkCreatePanel)